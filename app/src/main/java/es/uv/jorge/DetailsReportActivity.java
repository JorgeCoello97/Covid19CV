package es.uv.jorge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Objects;

import es.uv.jorge.data.ReportsDbHelper;
import es.uv.jorge.fragments.DatePickerDialogFragment;
import es.uv.jorge.model.Report;
import es.uv.jorge.constants.Constants;
import es.uv.jorge.constants.Flags;

import static es.uv.jorge.constants.Constants.CHECKED;
import static es.uv.jorge.constants.Constants.NOT_CHECKED;

public class DetailsReportActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    private final int CREATION_REPORT = 0;
    private final int UPDATE_REPORT = 1;

    private int currentMode;
    private int reportID;
    private boolean providedMunicipalityName;
    private String municipalityName;
    private String[] municipalitiesNames;

    private ReportsDbHelper reportsDbHelper;

    private MaterialAutoCompleteTextView autoCompleteTextViewMunicipality;
    private TextInputLayout textInputLayoutID, textInputLayoutSymptomStartDate;
    private EditText editTextID, editTextSymptomStartDate;
    private ImageView imageViewCalendar;
    private CheckBox checkBoxFever, checkBoxCough, checkBoxDifficultyBreathing,
    checkBoxFatigue, checkBoxBodyAche, checkBoxHeadache, checkBoxLossTasteOrSmell,
    checkBoxSoreThroat, checkBoxCongestionNose, checkBoxNausea, checkBoxDiarrhea;

    private Spinner spinnerCloseContact;
    private ArrayAdapter<CharSequence> adapterSpinner;
    private ArrayAdapter<String> adapterAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_report);

        initializeViews();

        Gson gson = new Gson();
        String sListMunicipalitiesNames = getIntent().getExtras().getString(Constants.EXTRA_LIST_MUNICIPALITIES_NAMES);
        municipalitiesNames = gson.fromJson(sListMunicipalitiesNames, String[].class);


        reportID = getIntent().getExtras().getInt(Constants.EXTRA_REPORT_ID, 0);
        if (reportID != 0){
            currentMode = UPDATE_REPORT;
            setTitle(getString(R.string.title_update_report));
        }
        else {
            currentMode = CREATION_REPORT;
            setTitle(getString(R.string.title_create_report));
            municipalityName = getIntent().getExtras().getString(Constants.EXTRA_MUNICIPALITY_NAME,"");
            providedMunicipalityName = (municipalityName != null) && (!municipalityName.isEmpty());
        }

        imageViewCalendar.setOnClickListener(v -> {
            DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
            datePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");
        });
        reportsDbHelper = new ReportsDbHelper(this);

        renderData();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        autoCompleteTextViewMunicipality = findViewById(R.id.autoCompleteTextView_municipality);
        autoCompleteTextViewMunicipality.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence text) {
                Arrays.sort(municipalitiesNames);
                return Arrays.binarySearch(municipalitiesNames, text.toString()) > 0;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText) {
                return "";
            }
        });
        autoCompleteTextViewMunicipality.setOnFocusChangeListener((v, hasFocus) -> {
            if (v.getId() == R.id.autoCompleteTextView_municipality && !hasFocus){
                autoCompleteTextViewMunicipality.performValidation();
            }
        });

        textInputLayoutID = findViewById(R.id.textInputLayout_ID);
        editTextID = textInputLayoutID.getEditText();

        textInputLayoutSymptomStartDate = findViewById(R.id.textInputLayout_symptom_startdate);
        editTextSymptomStartDate = textInputLayoutSymptomStartDate.getEditText();

        imageViewCalendar = findViewById(R.id.imageView_calendar);

        checkBoxFever = findViewById(R.id.checkBox_fever);
        checkBoxCough = findViewById(R.id.checkBox_cough);
        checkBoxDifficultyBreathing = findViewById(R.id.checkBox_difficulty_breathing);
        checkBoxFatigue = findViewById(R.id.checkBox_fatigue);
        checkBoxBodyAche = findViewById(R.id.checkBox_body_aches);
        checkBoxHeadache = findViewById(R.id.checkBox_headache);
        checkBoxLossTasteOrSmell = findViewById(R.id.checkBox_loss_taste_or_smell);
        checkBoxSoreThroat = findViewById(R.id.checkBox_sore_throat);
        checkBoxCongestionNose = findViewById(R.id.checkBox_congestion_nose);
        checkBoxNausea = findViewById(R.id.checkBox_nausea);
        checkBoxDiarrhea = findViewById(R.id.checkBox_diarrhea);

        spinnerCloseContact = findViewById(R.id.spinner_closecontact);
    }

    private void renderData() {
        adapterAutoComplete = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, municipalitiesNames);
        autoCompleteTextViewMunicipality.setAdapter(adapterAutoComplete);

        adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.closecontact_array, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCloseContact.setAdapter(adapterSpinner);
        if (currentMode == UPDATE_REPORT){
            Cursor cursor = reportsDbHelper.doFindReportByID(reportID);
            cursor.moveToFirst();
            Report report = new Report(cursor);

            editTextID.setText(String.valueOf(report.getId()));
            autoCompleteTextViewMunicipality.setText(report.getMunicipalityName());

            editTextSymptomStartDate.setText(report.getStartDate());
            imageViewCalendar.setVisibility(View.VISIBLE);

            if (report.getSymptomFever() == CHECKED){
                checkBoxFever.setChecked(true);
            }
            if (report.getSymptomCough() == CHECKED){
                checkBoxCough.setChecked(true);
            }
            if (report.getSymptomDifficultyBreathing() == CHECKED){
                checkBoxDifficultyBreathing.setChecked(true);
            }
            if (report.getSymptomFatigue() == CHECKED){
                checkBoxFatigue.setChecked(true);
            }
            if (report.getSymptomBodyAche() == CHECKED){
                checkBoxBodyAche.setChecked(true);
            }
            if (report.getSymptomHeadache() == CHECKED){
                checkBoxHeadache.setChecked(true);
            }
            if (report.getSymptomLossTasteOrSmell() == CHECKED){
                checkBoxLossTasteOrSmell.setChecked(true);
            }
            if (report.getSymptomSoreThroat() == CHECKED){
                checkBoxSoreThroat.setChecked(true);
            }
            if (report.getSymptomCongestionNose() == CHECKED){
                checkBoxCongestionNose.setChecked(true);
            }
            if (report.getSymptomNausea() == CHECKED){
                checkBoxNausea.setChecked(true);
            }
            if (report.getSymptomDiarrhea() == CHECKED){
                checkBoxDiarrhea.setChecked(true);
            }

            spinnerCloseContact.setSelection(report.getCloseContact()); // 0->NO , 1->YES
        }
        else {  //currentMode == CREATION_REPORT
            textInputLayoutID.setVisibility(View.GONE);
            if (providedMunicipalityName){
                autoCompleteTextViewMunicipality.setText(municipalityName);
            }
            else {
                autoCompleteTextViewMunicipality.setEnabled(true);
            }
            imageViewCalendar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details_report, menu);
        if (currentMode == UPDATE_REPORT){
            menu.getItem(0).setVisible(false);
        }
        else { // currentMode == CREATION_REPORT
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if ( id == R.id.menu_item_createNewReport || id == R.id.menu_item_updateReport) {
            String auxMunicipalityName;
            if (!providedMunicipalityName){
                auxMunicipalityName = autoCompleteTextViewMunicipality.getText().toString().trim().toUpperCase();
            } else {
              auxMunicipalityName = municipalityName.trim().toUpperCase();
            }
            String symptom_startdate = editTextSymptomStartDate.getText().toString();
            int close_contact = spinnerCloseContact.getSelectedItemPosition();

            int symptom_fever = checkBoxFever.isChecked() ? CHECKED : NOT_CHECKED;
            int symptom_cough = checkBoxCough.isChecked() ? CHECKED : NOT_CHECKED;
            int symptom_difficulting_breathing = checkBoxDifficultyBreathing.isChecked() ? CHECKED : NOT_CHECKED;
            int symptom_fatigue = checkBoxFatigue.isChecked() ? CHECKED : NOT_CHECKED;
            int symptom_body_aches = checkBoxBodyAche.isChecked() ? CHECKED : NOT_CHECKED;
            int symptom_headache = checkBoxHeadache.isChecked() ? CHECKED : NOT_CHECKED;
            int symptom_loss_taste_or_smell = checkBoxLossTasteOrSmell.isChecked() ? CHECKED : NOT_CHECKED;
            int symptom_sore_throat = checkBoxSoreThroat.isChecked() ? CHECKED : NOT_CHECKED;
            int symptom_congestion_nose = checkBoxCongestionNose.isChecked() ? CHECKED : NOT_CHECKED;
            int symptom_nausea = checkBoxNausea.isChecked() ? CHECKED : NOT_CHECKED;
            int symptom_diarrhea = checkBoxDiarrhea.isChecked() ? CHECKED : NOT_CHECKED;

            if (id == R.id.menu_item_createNewReport) {
               if (isValidMunicipalityName() && isValidSymptomStartDate()){
                   Report report = new Report(auxMunicipalityName, symptom_startdate, close_contact,
                           symptom_fever, symptom_cough, symptom_difficulting_breathing,
                           symptom_fatigue, symptom_body_aches, symptom_headache,
                           symptom_loss_taste_or_smell, symptom_sore_throat,
                           symptom_congestion_nose, symptom_nausea, symptom_diarrhea);

                   long newReportID = reportsDbHelper.doInsertReport(report);

                   if (newReportID != 0){
                       setResult(Flags.MunicipalitiesActivity.RESULT_REPORT_CREATED); // === Flags.DetailsMunicipalityActivity.RESULT_REPORT_CREATED
                   }
                   else {
                       setResult(Flags.MunicipalitiesActivity.RESULT_REPORT_NOT_CREATED); // === Flags.DetailsMunicipalityActivity.RESULT_REPORT_NOT_CREATED
                   }
                   finish();
               }
            }
            else { // id == R.id.menu_item_updateReport
                if (isValidSymptomStartDate()){
                    Report report = new Report(reportID,
                            auxMunicipalityName, symptom_startdate, close_contact,
                            symptom_fever, symptom_cough, symptom_difficulting_breathing,
                            symptom_fatigue, symptom_body_aches, symptom_headache,
                            symptom_loss_taste_or_smell, symptom_sore_throat,
                            symptom_congestion_nose, symptom_nausea, symptom_diarrhea);

                    int rowsAffected = reportsDbHelper.doUpdateReport(report);
                    if (rowsAffected != 0){
                        setResult(Flags.DetailsMunicipalityActivity.RESULT_REPORT_UPDATED);
                    }
                    else {
                        setResult(Flags.DetailsMunicipalityActivity.RESULT_REPORT_NOT_UPDATED);
                    }
                    finish();
                }
            }
        }
        else if (id == R.id.menu_item_deleteReport){
            int rowsAffected = reportsDbHelper.doDeleteReport(reportID);
            if (rowsAffected != 0){
                setResult(Flags.DetailsMunicipalityActivity.RESULT_REPORT_DELETED);
            }
            else {
                setResult(Flags.DetailsMunicipalityActivity.RESULT_REPORT_NOT_DELETED);
            }
            finish();
        }
        else if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (currentMode == UPDATE_REPORT){
            setResult(Flags.DetailsMunicipalityActivity.RESULT_REPORT_NOT_UPDATED);
        }
        else { //currentMode == CREATION_REPORT
            setResult(Flags.MunicipalitiesActivity.RESULT_REPORT_NOT_CREATED); // === Flags.DetailsMunicipalityActivity.RESULT_REPORT_NOT_CREATED
        }
        finish();
    }

    private boolean isValidSymptomStartDate(){
        if (editTextSymptomStartDate.getText().toString().isEmpty()){
            showMessage(getString(R.string.must_fill_startdate));
            return false;
        }
        return true;
    }
    private boolean isValidMunicipalityName(){
        String name = autoCompleteTextViewMunicipality.getText().toString();
        if (name.isEmpty()){
            showMessage(getString(R.string.must_fill_municipality));
            return false;
        }
        else {
            int i = 0;
            boolean exist = false;
            while(i < municipalitiesNames.length && !exist) {
                if (municipalitiesNames[i].equals(name)){
                    exist = true;
                }
                i++;
            }
            if (!exist){
                showMessage(getString(R.string.must_exist_municipality));
            }
            return exist;
        }
    }
    private void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String sDay = String.valueOf(dayOfMonth).length() == 1 ? "0"+dayOfMonth : String.valueOf(dayOfMonth);
        String sMonth = String.valueOf(month).length() == 1 ? "0"+month : String.valueOf(month);
        String dateSelected = sDay + "/" + sMonth + "/" + year;
        editTextSymptomStartDate.setText(dateSelected);
    }
}