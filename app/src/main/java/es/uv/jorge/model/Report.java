package es.uv.jorge.model;

import android.database.Cursor;

import static android.provider.BaseColumns._ID;
import static es.uv.jorge.data.ReportsContract.ReportsEntry.COLUMN_CLOSE_CONTACT;
import static es.uv.jorge.data.ReportsContract.ReportsEntry.COLUMN_MUNICIPALITY;
import static es.uv.jorge.data.ReportsContract.ReportsEntry.COLUMN_SYMPTOM_BODY_ACHES;
import static es.uv.jorge.data.ReportsContract.ReportsEntry.COLUMN_SYMPTOM_CONGESTION_NOSE;
import static es.uv.jorge.data.ReportsContract.ReportsEntry.COLUMN_SYMPTOM_COUGH;
import static es.uv.jorge.data.ReportsContract.ReportsEntry.COLUMN_SYMPTOM_DIARRHEA;
import static es.uv.jorge.data.ReportsContract.ReportsEntry.COLUMN_SYMPTOM_DIFFICULTY_BREATHING;
import static es.uv.jorge.data.ReportsContract.ReportsEntry.COLUMN_SYMPTOM_FATIGUE;
import static es.uv.jorge.data.ReportsContract.ReportsEntry.COLUMN_SYMPTOM_FEVER;
import static es.uv.jorge.data.ReportsContract.ReportsEntry.COLUMN_SYMPTOM_HEADACHE;
import static es.uv.jorge.data.ReportsContract.ReportsEntry.COLUMN_SYMPTOM_LOSS_TASTE_OR_SMELL;
import static es.uv.jorge.data.ReportsContract.ReportsEntry.COLUMN_SYMPTOM_NAUSEA;
import static es.uv.jorge.data.ReportsContract.ReportsEntry.COLUMN_SYMPTOM_SORE_THROAT;
import static es.uv.jorge.data.ReportsContract.ReportsEntry.COLUMN_SYMPTOM_STARTDATE;

public class Report {
    private int id;
    private String municipalityName;
    private String startDate;
    private int closeContact;
    private int symptomFever;
    private int symptomCough;
    private int symptomDifficultyBreathing;
    private int symptomFatigue;
    private int symptomBodyAche;
    private int symptomHeadache;
    private int symptomLossTasteOrSmell;
    private int symptomSoreThroat;
    private int symptomCongestionNose;
    private int symptomNausea;
    private int symptomDiarrhea;

    public Report(int id, String municipalityName, String startDate, int closeContact,
                  int symptomFever, int symptomCough, int symptomDifficultyBreathing,
                  int symptomFatigue, int symptomBodyAche, int symptomHeadache, int symptomLossTasteOrSmell,
                  int symptomSoreThroat, int symptomCongestionNose, int symptomNausea, int symptomDiarrhea) {
        this.id = id;
        this.municipalityName = municipalityName;
        this.startDate = startDate;
        this.closeContact = closeContact;
        this.symptomFever = symptomFever;
        this.symptomCough = symptomCough;
        this.symptomDifficultyBreathing = symptomDifficultyBreathing;
        this.symptomFatigue = symptomFatigue;
        this.symptomBodyAche = symptomBodyAche;
        this.symptomHeadache = symptomHeadache;
        this.symptomLossTasteOrSmell = symptomLossTasteOrSmell;
        this.symptomSoreThroat = symptomSoreThroat;
        this.symptomCongestionNose = symptomCongestionNose;
        this.symptomNausea = symptomNausea;
        this.symptomDiarrhea = symptomDiarrhea;
    }

    public Report(String municipalityName, String startDate, int closeContact,
                  int symptomFever, int symptomCough, int symptomDifficultyBreathing,
                  int symptomFatigue, int symptomBodyAche, int symptomHeadache,
                  int symptomLossTasteOrSmell, int symptomSoreThroat, int symptomCongestionNose,
                  int symptomNausea, int symptomDiarrhea) {
        this.municipalityName = municipalityName;
        this.startDate = startDate;
        this.closeContact = closeContact;
        this.symptomFever = symptomFever;
        this.symptomCough = symptomCough;
        this.symptomDifficultyBreathing = symptomDifficultyBreathing;
        this.symptomFatigue = symptomFatigue;
        this.symptomBodyAche = symptomBodyAche;
        this.symptomHeadache = symptomHeadache;
        this.symptomLossTasteOrSmell = symptomLossTasteOrSmell;
        this.symptomSoreThroat = symptomSoreThroat;
        this.symptomCongestionNose = symptomCongestionNose;
        this.symptomNausea = symptomNausea;
        this.symptomDiarrhea = symptomDiarrhea;
    }

    public Report (Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
        this.municipalityName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MUNICIPALITY));
        this.startDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SYMPTOM_STARTDATE));
        this.closeContact = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLOSE_CONTACT));
        this.symptomFever = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SYMPTOM_FEVER));
        this.symptomCough = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SYMPTOM_COUGH));
        this.symptomDifficultyBreathing = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SYMPTOM_DIFFICULTY_BREATHING));
        this.symptomFatigue = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SYMPTOM_FATIGUE));
        this.symptomBodyAche = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SYMPTOM_BODY_ACHES));
        this.symptomHeadache = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SYMPTOM_HEADACHE));
        this.symptomLossTasteOrSmell = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SYMPTOM_LOSS_TASTE_OR_SMELL));
        this.symptomSoreThroat = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SYMPTOM_SORE_THROAT));
        this.symptomCongestionNose = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SYMPTOM_CONGESTION_NOSE));
        this.symptomNausea = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SYMPTOM_NAUSEA));
        this.symptomDiarrhea = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SYMPTOM_DIARRHEA));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getCloseContact() {
        return closeContact;
    }

    public void setCloseContact(int closeContact) {
        this.closeContact = closeContact;
    }

    public int getSymptomFever() {
        return symptomFever;
    }

    public void setSymptomFever(int symptomFever) {
        this.symptomFever = symptomFever;
    }

    public int getSymptomCough() {
        return symptomCough;
    }

    public void setSymptomCough(int symptomCough) {
        this.symptomCough = symptomCough;
    }

    public int getSymptomDifficultyBreathing() {
        return symptomDifficultyBreathing;
    }

    public void setSymptomDifficultyBreathing(int symptomDifficultyBreathing) {
        this.symptomDifficultyBreathing = symptomDifficultyBreathing;
    }

    public int getSymptomFatigue() {
        return symptomFatigue;
    }

    public void setSymptomFatigue(int symptomFatigue) {
        this.symptomFatigue = symptomFatigue;
    }

    public int getSymptomBodyAche() {
        return symptomBodyAche;
    }

    public void setSymptomBodyAche(int symptomBodyAche) {
        this.symptomBodyAche = symptomBodyAche;
    }

    public int getSymptomHeadache() {
        return symptomHeadache;
    }

    public void setSymptomHeadache(int symptomHeadache) {
        this.symptomHeadache = symptomHeadache;
    }

    public int getSymptomLossTasteOrSmell() {
        return symptomLossTasteOrSmell;
    }

    public void setSymptomLossTasteOrSmell(int symptomLossTasteOrSmell) {
        this.symptomLossTasteOrSmell = symptomLossTasteOrSmell;
    }

    public int getSymptomSoreThroat() {
        return symptomSoreThroat;
    }

    public void setSymptomSoreThroat(int symptomSoreThroat) {
        this.symptomSoreThroat = symptomSoreThroat;
    }

    public int getSymptomCongestionNose() {
        return symptomCongestionNose;
    }

    public void setSymptomCongestionNose(int symptomCongestionNose) {
        this.symptomCongestionNose = symptomCongestionNose;
    }

    public int getSymptomNausea() {
        return symptomNausea;
    }

    public void setSymptomNausea(int symptomNausea) {
        this.symptomNausea = symptomNausea;
    }

    public int getSymptomDiarrhea() {
        return symptomDiarrhea;
    }

    public void setSymptomDiarrhea(int symptomDiarrhea) {
        this.symptomDiarrhea = symptomDiarrhea;
    }

    public int getTotalNumSymptoms(){
        return symptomFever+symptomCough+symptomDifficultyBreathing+symptomFatigue
                +symptomBodyAche+symptomHeadache+symptomLossTasteOrSmell+symptomSoreThroat
                +symptomCongestionNose+ symptomNausea + symptomDiarrhea;
    }
}
