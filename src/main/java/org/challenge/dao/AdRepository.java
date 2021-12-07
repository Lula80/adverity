package org.challenge.dao;



import org.challenge.rest.req.MetricRange;
import org.challenge.rest.resp.AdGroup;
import org.challenge.services.model.Dimension;
import org.challenge.services.model.DimensionsDto;
import org.challenge.services.model.Metric;

import java.util.List;
import java.util.Optional;

public interface AdRepository {
    String FROM_CLAUSE = " FROM ADVERT";
    String SELECT_CTR = "CAST(" + Metric.CLICKS.getSqlName() +"AS DECIMAL)/ NULLIF(" + Metric.IMPRESSIONS.getSqlName() + ", 0)";
    String AGGREGATE = "AGGREGATE";

    List<AdItem> findAll();

    String findClickBy(List<Dimension> dimensions, String aggregator);

    String findImpressionBy(List<Dimension> dimensions, String aggregator);

    String findCtrBy(List<Dimension> dimensions, String aggregator);

    List<String> findMetricsBy(List<Dimension> dimensions, Metric metric);

    List<AdGroup> findMetricGroupedBy(DimensionsDto dto, Optional<MetricRange> metricRange);

    enum Aggregator{
        AVG("AVG"),
        COUNT("COUNT"),
        MAX("MAX"),
        MIN("MIN"),
        SUM("SUM");

        Aggregator(String aggregator) {
            sqlValue = aggregator;
        }

        private final String sqlValue;
        public String getSqlValue() {
            return sqlValue;
        }
    }

}
