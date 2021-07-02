package es.uv.jorge.constants;

public class Flags {
    public static class MunicipalitiesActivity {
        public static final int REQUEST_FROM_MUNICIPALITIES_ACTIVITY = 100;
        public static final int RESULT_REPORT_CREATED = 101;
        public static final int RESULT_REPORT_NOT_CREATED = 102;
    }

    public static class DetailsMunicipalityActivity {
        public static final int REQUEST_FROM_DETAILS_MUNICIPALITY_ACTIVITY = 200;

        public static final int RESULT_REPORT_CREATED = 101;
        public static final int RESULT_REPORT_NOT_CREATED = 102;

        public static final int RESULT_REPORT_UPDATED = 211;
        public static final int RESULT_REPORT_NOT_UPDATED = 212;

        public static final int RESULT_REPORT_DELETED = 221;
        public static final int RESULT_REPORT_NOT_DELETED = 222;

        public static final int FRAGMENT_DETAILS_MUNICIPALITY = 901;
        public static final int FRAGMENT_LIST_REPORTS_MUNICIPALITY = 902;
    }
}
