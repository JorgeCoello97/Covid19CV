package es.uv.jorge;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;

import es.uv.jorge.adapters.MunicipalitiesAdapter;
import es.uv.jorge.model.Municipality;
import es.uv.jorge.constants.Constants;
import es.uv.jorge.constants.Flags;
import es.uv.jorge.utils.ConnectivityReceiver;
import es.uv.jorge.utils.Utils;

public class MunicipalitiesActivity extends AppCompatActivity
        implements LocationListener, ConnectivityReceiver.ConnectivityReceiverListener {
    public static final int MUNICIPALITY = 0;
    public static final int TEST_PCR = 1;
    public static final int CUMULATIVE_INCIDENCE = 2;

    public static final int REQUESTCODE_PERMISSION_LOCATION = 100;

    protected LocationManager locationManager;

    private int orderBy;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private RadioGroup radioGroupOrder;
    private MunicipalitiesAdapter adapterMunicipalities;
    private RelativeLayout toolbarFiltrado_hidden, toolbarFiltrado_showed;
    private ImageView imageViewArrowDown, imageViewArrowUp, imageViewCovid,
            imageViewOrderedBy, imageViewLocation;
    private TextView textViewLocation, textViewTitleOrderBy;
    private ProgressBar progressLocation;
    private FloatingActionButton fab_addReport;
    private String currentLocation;
    private Button buttonLocation;
    private LinearLayout linearLayout_no_internet_connection;

    private boolean internetConnection;
    private boolean GPSisEnabled;
    private String[] municipalitiesNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipalities);

        initializeViews();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        imageViewCovid.startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.rotate)
        );

        refreshLayout.setOnRefreshListener(() -> {
            if (Utils.isNetworkAvailable(this) && Utils.isOnline(this)){
                internetConnection = true;

                textViewTitleOrderBy.setVisibility(View.VISIBLE);
                imageViewOrderedBy.setVisibility(View.VISIBLE);
                toolbarFiltrado_hidden.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                fab_addReport.setVisibility(View.VISIBLE);

                linearLayout_no_internet_connection.setVisibility(View.GONE);

                ObtainJSONAsyncTask obtainJSONAsyncTask = new ObtainJSONAsyncTask(this);
                obtainJSONAsyncTask.execute(getString(R.string.url_all_resources_covid));

            }else {
                internetConnection = false;
                textViewTitleOrderBy.setVisibility(View.GONE);
                imageViewOrderedBy.setVisibility(View.GONE);
                toolbarFiltrado_hidden.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                fab_addReport.setVisibility(View.GONE);
                linearLayout_no_internet_connection.setVisibility(View.VISIBLE);
            }
        });


        imageViewOrderedBy.setOnClickListener(v ->{
            switch (orderBy){
                case MUNICIPALITY:
                    Snackbar.make(fab_addReport, getString(R.string.ordered_by_municipalities), Snackbar.LENGTH_SHORT).show();
                    break;
                case TEST_PCR:
                    Snackbar.make(fab_addReport, getString(R.string.ordered_by_testsPCR), Snackbar.LENGTH_SHORT).show();
                    break;
                case CUMULATIVE_INCIDENCE:
                    Snackbar.make(fab_addReport, getString(R.string.ordered_by_acumulativeIncidence), Snackbar.LENGTH_SHORT).show();
                    break;
            }
        });

        orderBy = MUNICIPALITY;

        fab_addReport.setOnClickListener(v -> {
            Intent intent = new Intent(this, DetailsReportActivity.class);
            Gson gson = new Gson();
            if (GPSisEnabled){
                intent.putExtra(Constants.EXTRA_MUNICIPALITY_NAME, currentLocation);
            }else {
                intent.putExtra(Constants.EXTRA_MUNICIPALITY_NAME, "");
            }
            String[] municipalitiesNames = adapterMunicipalities.getMunicipalitiesNames();
            String sListMunicipalitiesNames = gson.toJson(municipalitiesNames);
            intent.putExtra(Constants.EXTRA_LIST_MUNICIPALITIES_NAMES, sListMunicipalitiesNames);

            startActivityForResult(intent, Flags.MunicipalitiesActivity.REQUEST_FROM_MUNICIPALITIES_ACTIVITY);
        });

        imageViewArrowDown.setOnClickListener(v -> {
            toolbarFiltrado_hidden.setVisibility(View.GONE);
            toolbarFiltrado_showed.setVisibility(View.VISIBLE);
        });

        imageViewArrowUp.setOnClickListener(v -> {
            toolbarFiltrado_hidden.setVisibility(View.VISIBLE);
            toolbarFiltrado_showed.setVisibility(View.GONE);
        });

        buttonLocation.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, REQUESTCODE_PERMISSION_LOCATION); //TODO: Only works first time...
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, this);
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(new ConnectivityReceiver(),filter);
    }

    private void initializeViews(){
        Toolbar toolbarMain = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbarMain);

        toolbarFiltrado_hidden = findViewById(R.id.toolbarFiltrado_hidden);
        toolbarFiltrado_showed = findViewById(R.id.toolbarFiltrado_showed);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.rvMunicipalities);
        radioGroupOrder = findViewById(R.id.radioGroupOrder);
        imageViewArrowDown = findViewById(R.id.imageViewArrowDown);
        imageViewArrowUp = findViewById(R.id.imageViewArrowUp);
        imageViewCovid = findViewById(R.id.logo_covid);
        fab_addReport = findViewById(R.id.fab_addReport);

        imageViewLocation = findViewById(R.id.imageViewLocation);
        textViewLocation = findViewById(R.id.textViewLocation);
        progressLocation = findViewById(R.id.progressLocation);
        buttonLocation = findViewById(R.id.buttonLocation);

        linearLayout_no_internet_connection = findViewById(R.id.linearLayout_no_internet_connection);

        textViewTitleOrderBy = findViewById(R.id.textViewTitleOrderBy);
        imageViewOrderedBy = findViewById(R.id.imageViewOrderedBy);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (internetConnection){
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setQueryHint(getString(R.string.hint_search));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    adapterMunicipalities.getFilter().filter(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapterMunicipalities.getFilter().filter(newText);
                    return true;
                }
            });
            searchView.setOnCloseListener(() -> {
                adapterMunicipalities.getFilter().filter("");
                return false;
            });

            menu.getItem(0).setVisible(true); //Search
            menu.getItem(2).setVisible(true); //Order By...
        }else {
            menu.getItem(0).setVisible(false); //Search
            menu.getItem(2).setVisible(false); //Order By...
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        if (internetConnection){
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setQueryHint(getString(R.string.hint_search));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    adapterMunicipalities.getFilter().filter(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapterMunicipalities.getFilter().filter(newText);
                    return true;
                }
            });
            searchView.setOnCloseListener(() -> {
                adapterMunicipalities.getFilter().filter("");
                return false;
            });

            menu.getItem(0).setVisible(true); //Search
            menu.getItem(2).setVisible(true); //Order By...
        }else {
            menu.getItem(0).setVisible(false); //Search
            menu.getItem(2).setVisible(false); //Order By...
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if ( (id == R.id.menu_item_filterMunicipality) ||
             (id == R.id.menu_item_filterTestPCR) ||
             (id == R.id.menu_item_filterAcumulativeIncidence) )
        {
            radioGroupOrder.check(R.id.radioButtonASC);
            ArrayList<Municipality> axuMunicipalities = adapterMunicipalities.getMunicipalities();

            if (id == R.id.menu_item_filterMunicipality){
                Collections.sort(axuMunicipalities, (o1, o2) -> {
                    String name1 = o1.getNameMunicipality();
                    String name2 = o2.getNameMunicipality();
                    name1 = Normalizer.normalize(name1, Normalizer.Form.NFD);
                    name2 = Normalizer.normalize(name2, Normalizer.Form.NFD);
                    return name1.compareTo(name2);
                });
                orderBy = MUNICIPALITY;
                imageViewOrderedBy.setImageResource(R.drawable.municipality);
            }
            else if (id == R.id.menu_item_filterTestPCR) {
                Collections.sort(axuMunicipalities, (o1, o2) -> {
                    if (o1.getNumPCR() < o2.getNumPCR()){
                        return -1;
                    }
                    if (o1.getNumPCR() > o2.getNumPCR()){
                        return 1;
                    }
                    return 0;
                });
                orderBy = TEST_PCR;
                imageViewOrderedBy.setImageResource(R.drawable.test_pcr);
            }
            else  {
                Collections.sort(axuMunicipalities, (o1, o2) -> {
                    String o1CumulativePCR = o1.getCumulativePCR().replace(',','.');
                    String o2CumulativePCR = o2.getCumulativePCR().replace(',','.');
                    double o1CumulativePCRparsed = Double.parseDouble(o1CumulativePCR);
                    double o2CumulativePCRparsed = Double.parseDouble(o2CumulativePCR);
                    if (o1CumulativePCRparsed < o2CumulativePCRparsed){
                        return -1;
                    }
                    if (o1CumulativePCRparsed > o2CumulativePCRparsed){
                        return 1;
                    }
                    return 0;
                });
                orderBy = CUMULATIVE_INCIDENCE;
                imageViewOrderedBy.setImageResource(R.drawable.issue);
            }

            adapterMunicipalities.setMunicipalities(axuMunicipalities);
            adapterMunicipalities.notifyDataSetChanged();
        }
        else {
            if (id == R.id.menu_item_openWebsite){
                String url = getString(R.string.url_website_gva);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        }
        return true;
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        ArrayList<Municipality> axuMunicipalities = adapterMunicipalities.getMunicipalities();
        switch(view.getId()) {
            case R.id.radioButtonASC:
                if (checked){

                    if (orderBy == MUNICIPALITY){
                        Collections.sort(axuMunicipalities, (o1, o2) ->
                                o1.getNameMunicipality().compareTo(o2.getNameMunicipality()));
                    }
                    else if (orderBy == TEST_PCR){
                        Collections.sort(axuMunicipalities, (o1, o2) -> {
                            if (o1.getNumPCR() < o2.getNumPCR()){
                                return -1;
                            }
                            if (o1.getNumPCR() > o2.getNumPCR()){
                                return 1;
                            }
                            return 0;
                        });
                    }
                    else {
                        Collections.sort(axuMunicipalities, (o1, o2) ->
                                o1.getCumulativePCR().compareTo(o2.getCumulativePCR()));
                    }

                    adapterMunicipalities.setMunicipalities(axuMunicipalities);
                    adapterMunicipalities.notifyDataSetChanged();
                }
                break;
            case R.id.radioButtonDESC:
                if (checked){

                    if (orderBy == MUNICIPALITY) {
                        Collections.sort(axuMunicipalities, (o1, o2) ->{
                            String name1 = o1.getNameMunicipality();
                            String name2 = o2.getNameMunicipality();
                            name1 = Normalizer.normalize(name1, Normalizer.Form.NFD);
                            name2 = Normalizer.normalize(name2, Normalizer.Form.NFD);
                            return name2.compareTo(name1);
                        });
                    }
                    else if(orderBy == TEST_PCR) {
                        Collections.sort(axuMunicipalities, (o1, o2) -> {
                            if (o2.getNumPCR() < o1.getNumPCR()){
                                return -1;
                            }
                            if (o2.getNumPCR() > o1.getNumPCR()){
                                return 1;
                            }
                            return 0;
                        });
                    }
                    else {
                        Collections.sort(axuMunicipalities, (o1, o2) ->
                        {
                            String o1CumulativePCR = o1.getCumulativePCR().replace(',','.');
                            String o2CumulativePCR = o2.getCumulativePCR().replace(',','.');
                            double o1CumulativePCRparsed = Double.parseDouble(o1CumulativePCR);
                            double o2CumulativePCRparsed = Double.parseDouble(o2CumulativePCR);
                            if (o2CumulativePCRparsed < o1CumulativePCRparsed){
                                return -1;
                            }
                            if (o2CumulativePCRparsed > o1CumulativePCRparsed){
                                return 1;
                            }
                            return 0;
                        });
                    }
                    adapterMunicipalities.setMunicipalities(axuMunicipalities);
                    adapterMunicipalities.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Flags.MunicipalitiesActivity.REQUEST_FROM_MUNICIPALITIES_ACTIVITY){
            switch (resultCode){
                case Flags.DetailsMunicipalityActivity.RESULT_REPORT_CREATED:
                    Snackbar.make(fab_addReport, getString(R.string.success_create_report), Snackbar.LENGTH_SHORT).show();
                    break;
                case Flags.DetailsMunicipalityActivity.RESULT_REPORT_NOT_CREATED:
                    break;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(),
                    1);

            String locationProvidedByGPS = addresses.get(0).getLocality().trim().toUpperCase();
            currentLocation = Utils.mostSimilar(locationProvidedByGPS, municipalitiesNames);
            GPSisEnabled = true;

            imageViewLocation.setImageResource(R.drawable.ic_location);
            String message = getString(R.string.location_current)+" "+currentLocation;
            textViewLocation.setText(message);
            progressLocation.setVisibility(View.GONE);
            buttonLocation.setVisibility(View.GONE);
        }
        catch (IOException e){
            e.printStackTrace();
            imageViewLocation.setImageResource(R.drawable.ic_location_off);
            textViewLocation.setText(getString(R.string.location_searching));
            progressLocation.setVisibility(View.VISIBLE);
            buttonLocation.setVisibility(View.GONE);
            onLocationChanged(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)){
            imageViewLocation.setImageResource(R.drawable.ic_location_off);
            textViewLocation.setText(getString(R.string.location_searching));
            progressLocation.setVisibility(View.VISIBLE);
            buttonLocation.setVisibility(View.GONE);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)){
            GPSisEnabled = false;
            imageViewLocation.setImageResource(R.drawable.ic_location_off);
            textViewLocation.setText(getString(R.string.location_GPS_disabled));
            buttonLocation.setVisibility(View.GONE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case REQUESTCODE_PERMISSION_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }

                } else {
                    imageViewLocation.setImageResource(R.drawable.ic_location_off);
                    textViewLocation.setText(getString(R.string.location_automatic_location_disabled));
                    progressLocation.setVisibility(View.GONE);
                    buttonLocation.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        invalidateOptionsMenu();
        if (isConnected){
            internetConnection = true;
            textViewTitleOrderBy.setVisibility(View.VISIBLE);
            imageViewOrderedBy.setVisibility(View.VISIBLE);
            toolbarFiltrado_hidden.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            fab_addReport.setVisibility(View.VISIBLE);

            linearLayout_no_internet_connection.setVisibility(View.GONE);

            ObtainJSONAsyncTask obtainJSONAsyncTask = new ObtainJSONAsyncTask(this);
            obtainJSONAsyncTask.execute(getString(R.string.url_all_resources_covid));

        }else {
            internetConnection = false;
            textViewTitleOrderBy.setVisibility(View.GONE);
            imageViewOrderedBy.setVisibility(View.GONE);
            toolbarFiltrado_hidden.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            fab_addReport.setVisibility(View.GONE);
            linearLayout_no_internet_connection.setVisibility(View.VISIBLE);
        }
    }

    class ObtainJSONAsyncTask extends AsyncTask<String,Void, ArrayList<Municipality>>{
        private MunicipalitiesActivity activity;

        public ObtainJSONAsyncTask(MunicipalitiesActivity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            refreshLayout.setRefreshing(true);
        }

        @Override
        protected ArrayList<Municipality> doInBackground(String... strings) {
            ArrayList<Municipality> municipalities = new ArrayList<>();

            try {
                int n = 0;
                Writer writerAllResources = new StringWriter();
                char[] bufferAllResources = new char[1024];
                URL urlAllResources = new URL(strings[0]);
                HttpsURLConnection connectionAllResources = (HttpsURLConnection) urlAllResources.openConnection();
                connectionAllResources.setRequestMethod("GET");
                connectionAllResources.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) "+
                        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
                connectionAllResources.setRequestProperty("accept", "application/json;");
                connectionAllResources.setRequestProperty("accept-language", "es");
                connectionAllResources.connect();

                BufferedReader bufferedReaderAllResources = new BufferedReader(
                        new InputStreamReader(connectionAllResources.getInputStream(), StandardCharsets.UTF_8));
                while ((n = bufferedReaderAllResources.read(bufferAllResources)) != -1) {
                    writerAllResources.write(bufferAllResources, 0, n);
                }
                bufferedReaderAllResources.close();

                String sJSONAllResources = writerAllResources.toString();
                JSONObject jsonObjectRoot = new JSONObject(sJSONAllResources);
                JSONObject jsonObjectResults = jsonObjectRoot.getJSONObject("result");
                JSONArray jsonArrayResources = jsonObjectResults.getJSONArray("resources");

                JSONObject jsonObjectResource = jsonArrayResources.getJSONObject(jsonArrayResources.length()-1); //El último suponinedo que el último de la lista es el más actualizado.
                String url_resource_more_updated = jsonObjectResource.getString("url");

                URL urlResource = new URL(url_resource_more_updated);
                HttpsURLConnection connectionResource = (HttpsURLConnection) urlResource.openConnection();
                connectionResource.setRequestMethod("GET");
                connectionResource.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) "+
                        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
                connectionResource.setRequestProperty("accept", "text/plain;");
                connectionResource.setRequestProperty("accept-language", "es");
                connectionResource.connect();
                BufferedReader bufferedReaderResource = new BufferedReader(
                        new InputStreamReader(connectionResource.getInputStream(), StandardCharsets.UTF_8));
                bufferedReaderResource.readLine(); //CABECERAS DEL CSV -->
                // CodMunicipio;Municipi;Casos PCR+;Incidència acumulada PCR+;Casos PCR+ 14 dies;Incidència acumulada PCR+14;Defuncions;Taxa de defunció
                String sMunicipality = bufferedReaderResource.readLine();
                int count = 0;
                while (sMunicipality != null){
                   if (!sMunicipality.isEmpty()){
                       count++;
                       Municipality municipality = new Municipality();
                       StringTokenizer tokenizer = new StringTokenizer(sMunicipality,";");

                       municipality.setId(count);

                       int codMunicipality = Integer.parseInt(tokenizer.nextToken().trim());
                       municipality.setCodMunicipality(codMunicipality);

                       String municipalityName = tokenizer.nextToken().trim().toUpperCase();
                       municipality.setNameMunicipality(municipalityName);

                       int numPCR = Integer.parseInt(tokenizer.nextToken().trim());
                       municipality.setNumPCR(numPCR);

                       String cumulativePCR = tokenizer.nextToken().trim();
                       municipality.setCumulativePCR(cumulativePCR);

                       int numPCR14 = Integer.parseInt(tokenizer.nextToken().trim());
                       municipality.setNumPCR14(numPCR14);

                       String cumulativePCR14 = tokenizer.nextToken().trim();
                       municipality.setCumulativePCR14(cumulativePCR14);

                       int deaths = Integer.parseInt(tokenizer.nextToken().trim());
                       municipality.setDeaths(deaths);

                       String deathRate = tokenizer.nextToken().trim();
                       municipality.setDeathRate(deathRate);

                       municipalities.add(municipality);
                   }
                    sMunicipality = bufferedReaderResource.readLine();
                }

                bufferedReaderResource.close();

            }catch (Exception e){
                e.printStackTrace();
            }

            return municipalities;
        }

        @Override
        protected void onPostExecute(ArrayList<Municipality> municipalities) {
            refreshLayout.setRefreshing(false);
            Toast.makeText(getBaseContext(), getString(R.string.obtained_json_updated), Toast.LENGTH_SHORT).show();

            municipalitiesNames = new String[municipalities.size()];
            for (int i = 0; i < municipalities.size(); i++) {
                municipalitiesNames[i] = municipalities.get(i).getNameMunicipality();
            }

            adapterMunicipalities = new MunicipalitiesAdapter(activity, municipalities, municipalitiesNames);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerView.setAdapter(adapterMunicipalities);

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, REQUESTCODE_PERMISSION_LOCATION);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, activity);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityReceiver.connectivityReceiverListener = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ConnectivityReceiver.connectivityReceiverListener = this;
    }
}