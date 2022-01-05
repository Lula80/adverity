package org.challenge.utils;

class Constants {

    public static final String ERROR_MESSAGES = 'ErrorMessages';

    class RFC3986 {
        public static final String APPLICATION_NAME = 'adverity';
        public static final String API_VERSION = 'v1';
    }
    class REST{

        public static final String APP_REQ_URI = 'ads'
        public static final String METRIC_FILTERED = '/metrics/filtered'
        public static final String METRIC_AGGREGATED = '/metrics/aggregated'
        public static final String METRIC_GROUPED = '/metric/grouped'
    }

    class StringDimensions {
            public static final String DATASOURCE = 'Datasource';
            public static final String CAMPAIGN = 'Campaign';
    }
    class TimeDimensions {
        public static final String DAILY = 'Daily'
        public static final String DATE_FORMAT = "MM/dd/yy"
    }

}
