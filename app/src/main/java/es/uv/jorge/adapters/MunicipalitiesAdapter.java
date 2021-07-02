package es.uv.jorge.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;

import java.util.ArrayList;

import es.uv.jorge.DetailsMunicipalityActivity;
import es.uv.jorge.model.Municipality;
import es.uv.jorge.R;
import es.uv.jorge.constants.Constants;

public class MunicipalitiesAdapter extends RecyclerView.Adapter<MunicipalityViewHolder>
        implements Filterable {
    private ArrayList<Municipality> municipalities;
    private ArrayList<Municipality> municipalitiesFiltered;

    private String[] municipalitiesNames;

    public final static int FILTERED = 1;
    public final static int NOT_FILTERED = 0;
    private int status;

    private Context context;

    public MunicipalitiesAdapter(Context context, ArrayList<Municipality> municipalities, String[] municipalitiesNames) {
        this.context = context;
        this.municipalities = municipalities;
        this.municipalitiesNames = municipalitiesNames;
        municipalitiesFiltered = new ArrayList<>();
        this.status = NOT_FILTERED;
    }

    @NonNull
    @Override
    public MunicipalityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_municipality_covid, parent, false);
        return new MunicipalityViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MunicipalityViewHolder holder, int position) {
        Municipality municipality;
        if (status == NOT_FILTERED) {
            municipality = municipalities.get(position);
        } else {
            municipality = municipalitiesFiltered.get(position);
        }

        holder.getTextViewMunicipality().setText(municipality.getNameMunicipality());
        holder.getTextViewTestPCR().setText(""+municipality.getNumPCR());
        holder.getTextViewCumulativeIncidence().setText(municipality.getCumulativePCR());

        String cumulativeIncidence = municipality.getCumulativePCR();
        cumulativeIncidence = cumulativeIncidence.replace(',','.');
        double cumulativeIncidenceParsed = Double.parseDouble(cumulativeIncidence);
        if (cumulativeIncidenceParsed < 1000){
            holder.getContraintLayoutWrapper().setBackgroundColor(context.getColor(R.color.low));
        }
        else if(cumulativeIncidenceParsed < 5000){
            holder.getContraintLayoutWrapper().setBackgroundColor(context.getColor(R.color.medium));
        }
        else {
            holder.getContraintLayoutWrapper().setBackgroundColor(context.getColor(R.color.high));
        }

        holder.getContraintLayoutWrapper().setOnClickListener(v ->{
            Gson gson = new Gson();
            String sMunicipality = gson.toJson(municipality);

            Intent intent = new Intent(context, DetailsMunicipalityActivity.class);
            intent.putExtra(Constants.EXTRA_MUNICIPALITY_OBJECT, sMunicipality);

            String sListMunicipalitiesNames = gson.toJson(municipalitiesNames);
            intent.putExtra(Constants.EXTRA_LIST_MUNICIPALITIES_NAMES, sListMunicipalitiesNames);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        if (status == NOT_FILTERED){
            return municipalities.size();
        }
        else{
            return municipalitiesFiltered.size();
        }
    }

    public ArrayList<Municipality> getMunicipalities() {
        if (status == NOT_FILTERED){
            return municipalities;
        }
        else{
            return municipalitiesFiltered;
        }
    }

    public void setMunicipalities(ArrayList<Municipality> municipalities) {
        if (status == NOT_FILTERED){
            this.municipalities = municipalities;
        }
        else{
            this.municipalitiesFiltered = municipalities;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()){
                    status = NOT_FILTERED;
                    municipalitiesFiltered = municipalities;
                }
                else {
                    status = FILTERED;
                    ArrayList<Municipality> filteredList = new ArrayList<>();
                    for (Municipality municipality:
                         municipalities) {
                        if (municipality.getNameMunicipality().toLowerCase()
                        .contains(charString.toLowerCase())){
                            filteredList.add(municipality);
                        }
                    }
                    municipalitiesFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = municipalitiesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                municipalitiesFiltered = (ArrayList<Municipality>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public String[] getMunicipalitiesNames() {
        return municipalitiesNames;
    }
}
