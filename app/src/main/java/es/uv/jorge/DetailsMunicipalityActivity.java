package es.uv.jorge;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.Objects;

import es.uv.jorge.constants.Constants;
import es.uv.jorge.constants.Flags;
import es.uv.jorge.fragments.DetailsMunicipalityFragment;
import es.uv.jorge.fragments.ListReportsFragment;
import es.uv.jorge.model.Municipality;

public class DetailsMunicipalityActivity extends AppCompatActivity
        implements DetailsMunicipalityFragment.DetailsMunicipalityFragmentListener {
    private Municipality municipality;
    private String[] municipalitiesNames;

    private int currentFragment;
    private DetailsMunicipalityFragment detailsMunicipalityFragment;
    private ListReportsFragment listReportsFragment;

    private TextView textViewTitle;
    private ImageView imageViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_municipality);

        initializeViews();

        Gson gson = new Gson();

        String sListMunicipalitiesNames = getIntent().getExtras().getString(Constants.EXTRA_LIST_MUNICIPALITIES_NAMES);
        municipalitiesNames = gson.fromJson(sListMunicipalitiesNames, String[].class);

        String sMunicipality = getIntent().getExtras().getString(Constants.EXTRA_MUNICIPALITY_OBJECT);
        municipality = gson.fromJson(sMunicipality, Municipality.class);

        imageViewTitle.setOnClickListener(v -> {
            View parentLayout = findViewById(R.id.relativeLayout_container_details_municipality);
            String message = getString(R.string.icon_title_municipality) + " " + municipality.getNameMunicipality().trim().toUpperCase();
            Snackbar.make(parentLayout, message, Snackbar.LENGTH_SHORT).show();
        });

        showDetailsMunicipalityFragment();

        renderData();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        textViewTitle = findViewById(R.id.textViewTitle);
        imageViewTitle = findViewById(R.id.imageViewTitle);
    }

    private void renderData() {
        textViewTitle.setText(municipality.getNameMunicipality().toUpperCase());
    }

    private void showDetailsMunicipalityFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        detailsMunicipalityFragment = DetailsMunicipalityFragment.newInstance(municipality);
        detailsMunicipalityFragment.setCallback(this);
        fragmentTransaction.replace(R.id.frameLayout_container_details_municipality, detailsMunicipalityFragment);
        fragmentTransaction.commit();
        currentFragment = Flags.DetailsMunicipalityActivity.FRAGMENT_DETAILS_MUNICIPALITY;
    }

    private void showListReportsMunicipalityFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        listReportsFragment = ListReportsFragment.newInstance(municipality.getNameMunicipality(), municipalitiesNames);
        fragmentTransaction.replace(R.id.frameLayout_container_details_municipality, listReportsFragment);
        fragmentTransaction.commit();
        currentFragment = Flags.DetailsMunicipalityActivity.FRAGMENT_LIST_REPORTS_MUNICIPALITY;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details_municipality,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_seeOnMap) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String encodedNameMunicipality = Uri.encode(municipality.getNameMunicipality());
            intent.setData( Uri.parse("geo:0,0?q="+encodedNameMunicipality));
            startActivity(intent);
        }
        else if(item.getItemId() == android.R.id.home){
            if (currentFragment == Flags.DetailsMunicipalityActivity.FRAGMENT_LIST_REPORTS_MUNICIPALITY){
                showDetailsMunicipalityFragment();
            }
            else {
                onBackPressed();
            }
        }
        return true;
    }

    @Override
    public void performButtonShowReports() {
        showListReportsMunicipalityFragment();
    }

    @Override
    public void performFloatButtonAddReport() {
        Intent intent = new Intent(this, DetailsReportActivity.class);

        Gson gson = new Gson();
        String sListMunicipalitiesNames = gson.toJson(municipalitiesNames);
        intent.putExtra(Constants.EXTRA_LIST_MUNICIPALITIES_NAMES, sListMunicipalitiesNames);

        intent.putExtra(Constants.EXTRA_MUNICIPALITY_NAME, municipality.getNameMunicipality());

        startActivityForResult(intent, Flags.DetailsMunicipalityActivity.REQUEST_FROM_DETAILS_MUNICIPALITY_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Flags.DetailsMunicipalityActivity.REQUEST_FROM_DETAILS_MUNICIPALITY_ACTIVITY){
            switch (resultCode){
                case Flags.DetailsMunicipalityActivity.RESULT_REPORT_CREATED:
                    showMessage(getString(R.string.success_create_report));
                    break;

                case Flags.DetailsMunicipalityActivity.RESULT_REPORT_UPDATED:
                    showMessage(getString(R.string.success_update_report));
                    listReportsFragment.refreshListReports();
                    break;
                case Flags.DetailsMunicipalityActivity.RESULT_REPORT_DELETED:
                    showMessage(getString(R.string.success_delete_report));
                    listReportsFragment.refreshListReports();
                    break;

                case Flags.DetailsMunicipalityActivity.RESULT_REPORT_NOT_UPDATED:
                case Flags.DetailsMunicipalityActivity.RESULT_REPORT_NOT_DELETED:
                case Flags.DetailsMunicipalityActivity.RESULT_REPORT_NOT_CREATED:
                    break;
            }
        }
    }
    private void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}