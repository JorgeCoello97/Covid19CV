package es.uv.jorge.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import es.uv.jorge.R;
import es.uv.jorge.model.Municipality;

public class DetailsMunicipalityFragment extends Fragment {

    private static final String ARG_PARAM_MUNICIPALITY = "MUNICIPALITY";

    private Municipality municipality;
    private DetailsMunicipalityFragmentListener callback;

    private EditText editTextID;
    private EditText editTextCode;
    private EditText editTextName;
    private EditText editText_PCR_Before14Days;
    private EditText editText_AcumulativeIncidence_Before14Days;
    private EditText editText_PCR_After14Days;
    private EditText editText_AcumulativeIncidence_After14Days;
    private EditText editText_deaths;
    private EditText editText_deathRate;

    private FloatingActionButton fab_addReport;
    private Button button_showReports;

    public DetailsMunicipalityFragment() {
        // Required empty public constructor
    }

    public interface DetailsMunicipalityFragmentListener{
        void performButtonShowReports();
        void performFloatButtonAddReport();
    }

    public static DetailsMunicipalityFragment newInstance(Municipality municipality) {
        DetailsMunicipalityFragment fragment = new DetailsMunicipalityFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();

        String sMunicipality = gson.toJson(municipality);
        args.putString(ARG_PARAM_MUNICIPALITY, sMunicipality);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();

            String sMunicipality = getArguments().getString(ARG_PARAM_MUNICIPALITY);
            municipality = gson.fromJson(sMunicipality, Municipality.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_municipality, container, false);
        initializeViews(view);

        button_showReports.setOnClickListener(v -> {
            callback.performButtonShowReports();
        });

        fab_addReport.setOnClickListener(v -> {
            callback.performFloatButtonAddReport();
        });

        renderData();
        return view;
    }

    private void initializeViews(View view) {

        TextInputLayout textInputLayoutID = view.findViewById(R.id.textInputLayout_ID_Municipality);
        TextInputLayout textInputLayoutCode = view.findViewById(R.id.textInputLayout_Code_Municipality);
        TextInputLayout textInputLayoutName = view.findViewById(R.id.textInputLayout_Name_Municipality);
        editTextID = textInputLayoutID.getEditText();
        editTextCode = textInputLayoutCode.getEditText();
        editTextName = textInputLayoutName.getEditText();

        TextInputLayout textInputLayout_PCR_Before14Days = view.findViewById(R.id.textInputLayout_PCR_Before14Days);
        TextInputLayout textInputLayout_AcumulativeIncidence_Before14Days = view.findViewById(R.id.textInputLayout_AcumulativeIncidence_Before14Days);
        editText_PCR_Before14Days = textInputLayout_PCR_Before14Days.getEditText();
        editText_AcumulativeIncidence_Before14Days = textInputLayout_AcumulativeIncidence_Before14Days.getEditText();

        TextInputLayout textInputLayout_PCR_After14Days = view.findViewById(R.id.textInputLayout_PCR_After14Days);
        TextInputLayout textInputLayout_AcumulativeIncidence_After14Days = view.findViewById(R.id.textInputLayout_AcumulativaIncidence_After14Days);
        editText_PCR_After14Days = textInputLayout_PCR_After14Days.getEditText();
        editText_AcumulativeIncidence_After14Days = textInputLayout_AcumulativeIncidence_After14Days.getEditText();

        TextInputLayout textInputLayout_deaths = view.findViewById(R.id.textInputLayout_deaths_Deaths);
        TextInputLayout textInputLayout_deathRate = view.findViewById(R.id.textInputLayout_deathRate_Deaths);
        editText_deaths = textInputLayout_deaths.getEditText();
        editText_deathRate = textInputLayout_deathRate.getEditText();

        fab_addReport = view.findViewById(R.id.fab_addReport);
        button_showReports = view.findViewById(R.id.button_showReports);
    }

    @SuppressLint("SetTextI18n")
    private void renderData() {
        editTextID.setText(""+municipality.getId());
        editTextCode.setText(""+municipality.getCodMunicipality());
        editTextName.setText(municipality.getNameMunicipality());

        editText_PCR_Before14Days.setText(""+municipality.getNumPCR());
        editText_AcumulativeIncidence_Before14Days.setText(municipality.getCumulativePCR());


        editText_PCR_After14Days.setText(""+municipality.getNumPCR14());
        editText_AcumulativeIncidence_After14Days.setText(municipality.getCumulativePCR14());

        editText_deaths.setText(""+municipality.getDeaths());
        editText_deathRate.setText(municipality.getDeathRate());
    }

    public void setCallback(DetailsMunicipalityFragmentListener callback) {
        this.callback = callback;
    }
}