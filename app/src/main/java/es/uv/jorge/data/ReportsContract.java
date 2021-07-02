package es.uv.jorge.data;

import android.provider.BaseColumns;

public class ReportsContract {

    private ReportsContract(){}

    public static class ReportsEntry implements BaseColumns {
        public static final String TABLE = "reports";
        public static final String COLUMN_MUNICIPALITY = "municipality";
        public static final String COLUMN_SYMPTOM_STARTDATE = "symptom_startdate";
        public static final String COLUMN_CLOSE_CONTACT = "close_contact";

        //ALL SYMPTOMS
        public static final String COLUMN_SYMPTOM_FEVER = "symptom_fever";
        public static final String COLUMN_SYMPTOM_COUGH = "symptom_cough";
        public static final String COLUMN_SYMPTOM_DIFFICULTY_BREATHING = "symptom_difficulty_breathing";
        public static final String COLUMN_SYMPTOM_FATIGUE = "symptom_fatigue";
        public static final String COLUMN_SYMPTOM_BODY_ACHES = "symptom_body_aches";
        public static final String COLUMN_SYMPTOM_HEADACHE = "symptom_headache";

        public static final String COLUMN_SYMPTOM_LOSS_TASTE_OR_SMELL = "symptom_loss_taste_or_smell";
        public static final String COLUMN_SYMPTOM_SORE_THROAT = "symptom_sore_throat";
        public static final String COLUMN_SYMPTOM_CONGESTION_NOSE = "symptom_congestion_nose";
        public static final String COLUMN_SYMPTOM_NAUSEA = "symptom_nausea";
        public static final String COLUMN_SYMPTOM_DIARRHEA = "symptom_diarrhea";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ReportsEntry.TABLE + " (" +
                    ReportsEntry._ID + " INTEGER PRIMARY KEY," +
                    ReportsEntry.COLUMN_MUNICIPALITY + " TEXT," +
                    ReportsEntry.COLUMN_SYMPTOM_STARTDATE + " TEXT," +
                    ReportsEntry.COLUMN_CLOSE_CONTACT + " INTEGER," +

                    ReportsEntry.COLUMN_SYMPTOM_FEVER + " INTEGER," +
                    ReportsEntry.COLUMN_SYMPTOM_COUGH + " INTEGER," +
                    ReportsEntry.COLUMN_SYMPTOM_DIFFICULTY_BREATHING + " INTEGER," +
                    ReportsEntry.COLUMN_SYMPTOM_FATIGUE + " INTEGER," +
                    ReportsEntry.COLUMN_SYMPTOM_BODY_ACHES + " INTEGER," +
                    ReportsEntry.COLUMN_SYMPTOM_HEADACHE + " INTEGER," +

                    ReportsEntry.COLUMN_SYMPTOM_LOSS_TASTE_OR_SMELL + " INTEGER," +
                    ReportsEntry.COLUMN_SYMPTOM_SORE_THROAT + " INTEGER," +
                    ReportsEntry.COLUMN_SYMPTOM_CONGESTION_NOSE + " INTEGER," +
                    ReportsEntry.COLUMN_SYMPTOM_NAUSEA + " INTEGER," +
                    ReportsEntry.COLUMN_SYMPTOM_DIARRHEA + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ReportsEntry.TABLE;
}
