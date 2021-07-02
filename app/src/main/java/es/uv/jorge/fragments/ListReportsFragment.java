package es.uv.jorge.fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import es.uv.jorge.R;
import es.uv.jorge.adapters.ReportsAdapter;
import es.uv.jorge.data.ReportsDbHelper;

public class ListReportsFragment extends Fragment {
    private static final String ARG_PARAM_MUNICIPALITY_NAME = "MUNICIPALITY_NAME";
    private static final String ARG_PARAM_MUNICIPALITIES_NAMES = "MUNICIPALITIES_NAMES";

    private ReportsDbHelper reportsDbHelper;
    private ReportsAdapter reportsAdapter;

    private String[] municipalitiesNames;
    private String municipalityName;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout_recyclerView;
    private SwipeRefreshLayout refreshLayout_empty_list_reports;

    public ListReportsFragment() {
        // Required empty public constructor
    }

    public static ListReportsFragment newInstance(String municipalityName, String[] municipalitiesNames) {
        ListReportsFragment fragment = new ListReportsFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();

        args.putString(ARG_PARAM_MUNICIPALITY_NAME, municipalityName);

        String sListMunicipalitiesNames = gson.toJson(municipalitiesNames);
        args.putString(ARG_PARAM_MUNICIPALITIES_NAMES, sListMunicipalitiesNames);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();

            this.municipalityName = getArguments().getString(ARG_PARAM_MUNICIPALITY_NAME);

            String sListMunicipalitiesNames =  getArguments().getString(ARG_PARAM_MUNICIPALITIES_NAMES);
            municipalitiesNames = gson.fromJson(sListMunicipalitiesNames, String[].class);

            reportsDbHelper = new ReportsDbHelper(getContext());
            Cursor cursor = reportsDbHelper.doFindReportByMunicipality( municipalityName.trim().toUpperCase());
            cursor.moveToFirst();
            reportsAdapter = new ReportsAdapter(getContext(), cursor, municipalitiesNames, getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_reports, container, false);
        recyclerView = view.findViewById(R.id.rvReports);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),
                2, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(reportsAdapter);

        refreshLayout_recyclerView = view.findViewById(R.id.refreshLayoutRecyclerView);
        refreshLayout_recyclerView.setOnRefreshListener(this::refreshListReports);

        refreshLayout_empty_list_reports = view.findViewById(R.id.refreshLayoutEmptyListReports);
        refreshLayout_empty_list_reports.setOnRefreshListener(this::refreshListReports);

        if (reportsAdapter.getItemCount() == 0){
            refreshLayout_recyclerView.setVisibility(View.GONE);
            refreshLayout_empty_list_reports.setVisibility(View.VISIBLE);
        }
        return view;
    }
    public void refreshListReports(){
        Cursor cursor = reportsDbHelper.doFindReportByMunicipality( municipalityName.trim().toUpperCase());
        reportsAdapter = new ReportsAdapter(getContext(), cursor, municipalitiesNames, getActivity());
        recyclerView.setAdapter(reportsAdapter);

        if (reportsAdapter.getItemCount() == 0){
            refreshLayout_recyclerView.setVisibility(View.GONE);
            refreshLayout_empty_list_reports.setVisibility(View.VISIBLE);
            refreshLayout_empty_list_reports.setRefreshing(false);
        } else {
            refreshLayout_recyclerView.setVisibility(View.VISIBLE);
            refreshLayout_empty_list_reports.setVisibility(View.GONE);
            refreshLayout_recyclerView.setRefreshing(false);
        }
    }
}