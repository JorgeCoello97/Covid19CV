package es.uv.jorge.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import es.uv.jorge.model.Report;

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
import static es.uv.jorge.data.ReportsContract.ReportsEntry.TABLE;
import static es.uv.jorge.data.ReportsContract.SQL_CREATE_ENTRIES;
import static es.uv.jorge.data.ReportsContract.SQL_DELETE_ENTRIES;

public class ReportsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Reports.db";

    public ReportsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long doInsertReport(Report report){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getAllReportColumns(report);
        return db.insert(TABLE, null, values); //RETURN ROW ID
    }

    public int doUpdateReport(Report report){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getAllReportColumns(report);

        String whereClause = ReportsContract.ReportsEntry._ID+" = ?";
        String[] whereArgs = new String[]{report.getId()+""};

        return db.update(TABLE, values, whereClause, whereArgs); // RETURN NUM ROWS AFFECTED
    }

    public int doDeleteReport(int reportID){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = ReportsContract.ReportsEntry._ID+" = ?";
        String[] whereArgs = new String[]{reportID+""};
        return db.delete(TABLE, whereClause, whereArgs);  // RETURN NUM ROWS AFFECTED
    }

    public Cursor doFindReportByID(int reportID){
        SQLiteDatabase db = getReadableDatabase();
        String selection = ReportsContract.ReportsEntry._ID+" = ?";
        String[] selectionArgs = new String[]{reportID+""};
        return db.query(TABLE,null, selection, selectionArgs, null, null, null);
    }

    public Cursor doFindReportByMunicipality(String municipality){
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_MUNICIPALITY+" = ?";
        String[] selectionArgs = new String[]{municipality};
        return db.query(TABLE,null, selection, selectionArgs, null, null, null);
    }

    private ContentValues getAllReportColumns(Report report){
        ContentValues values = new ContentValues();
        values.put(COLUMN_MUNICIPALITY, report.getMunicipalityName());
        values.put(COLUMN_SYMPTOM_STARTDATE, report.getStartDate());
        values.put(COLUMN_CLOSE_CONTACT, report.getCloseContact());

        values.put(COLUMN_SYMPTOM_FEVER, report.getSymptomFever());
        values.put(COLUMN_SYMPTOM_COUGH, report.getSymptomCough());
        values.put(COLUMN_SYMPTOM_DIFFICULTY_BREATHING, report.getSymptomDifficultyBreathing());
        values.put(COLUMN_SYMPTOM_FATIGUE, report.getSymptomFatigue());
        values.put(COLUMN_SYMPTOM_BODY_ACHES, report.getSymptomBodyAche());
        values.put(COLUMN_SYMPTOM_HEADACHE, report.getSymptomHeadache());

        values.put(COLUMN_SYMPTOM_LOSS_TASTE_OR_SMELL, report.getSymptomLossTasteOrSmell());
        values.put(COLUMN_SYMPTOM_SORE_THROAT, report.getSymptomSoreThroat());
        values.put(COLUMN_SYMPTOM_CONGESTION_NOSE, report.getSymptomCongestionNose());
        values.put(COLUMN_SYMPTOM_NAUSEA, report.getSymptomNausea());
        values.put(COLUMN_SYMPTOM_DIARRHEA, report.getSymptomDiarrhea());
        return values;
    }
}
