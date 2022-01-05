package org.challenge.dao;

import org.challenge.rest.req.MetricRange;
import org.challenge.rest.resp.AdGroup;
import org.challenge.services.model.Dimension;
import org.challenge.services.model.DimensionsDto;
import org.challenge.services.model.Metric;

interface AdRepository {
    String TABLE = 'ADVERT';
    String AGGREGATE = 'AGGREGATE';

    List<AdItem> findAll();

    String findAggregatedMetricBy(DimensionsDto dto)

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
        String getSqlValue() {
            return sqlValue;
        }
    }

}
