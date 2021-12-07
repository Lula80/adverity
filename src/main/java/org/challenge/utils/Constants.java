package org.challenge.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public final String ERROR_MESSAGES = "ErrorMessages";

    @UtilityClass
    public class RFC3986 {
        public final String APPLICATION_NAME = "adverity";
        public final String API_VERSION = "v1";
    }
    @UtilityClass
    public class REST{

        public final String APP_REQ_URI = "ads";
        public final String METRIC_FILTERD = "/metrics/filtered";
        public final String CTR_AGGR = "/ctrs/aggr";
        public final String CLICKS_AGGR = "/clicks/aggr";
        public final String IMPRESSIONS_AGGR = "/impressions/aggr";
        public final String METRIC_GROUPED = "/metric/grouped";
    }
    @UtilityClass
    public class Dimensions {

        public class StringDimensions {
            public final static String DATASOURCE = "Datasource";
            public final static String CAMPAIGN = "Campaign";
        }
        public class TimeDimensions {
            public final static String DAILY = "Daily";
        }
    }

}
