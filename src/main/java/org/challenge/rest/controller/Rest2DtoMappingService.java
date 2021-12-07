package org.challenge.rest.controller;

import org.challenge.rest.req.DateRange;
import org.challenge.rest.req.GetGroupedMetricRq;
import org.challenge.rest.req.GetMetricsRq;
import org.challenge.services.model.DimensionsDto;
import org.challenge.services.model.Metric;
import org.challenge.services.model.StringDimension;
import org.challenge.services.model.TimeDimension;
import org.challenge.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class Rest2DtoMappingService {

    DimensionsDto mapToNonNullDimensions(GetMetricsRq req){

      DimensionsDto dto = DimensionsDto.builder()
                .metric(Metric.valueOf(req.getMetric().name()))
                .dimensions(new ArrayList<>()).build();

        if(isDailySet(req.getDaily())) {
            dto.getDimensions().add(new TimeDimension(Constants.Dimensions.TimeDimensions.DAILY, req.getDaily()));
        }
        if(isStringSet(req.getDataSource())) {
            dto.getDimensions().add(
                new StringDimension(Constants.Dimensions.StringDimensions.DATASOURCE, req.getDataSource()));
        }
        if(isStringSet(req.getCampaign())) {
            dto.getDimensions().add(
                new StringDimension(Constants.Dimensions.StringDimensions.CAMPAIGN, req.getCampaign()));
        }
        if(null != req.getAggregator()) {
            dto.setAggregate(req.getAggregator().name());
        }
        return dto;
    }

    private boolean isStringSet(String dataSource) {
        return null != dataSource && !dataSource.isBlank();
    }

    private boolean isDailySet(DateRange dates){
        return null != dates &&
            (null != dates.getFrom() || null != dates.getTo());
    }

    DimensionsDto mapToNonNullDimensions(GetGroupedMetricRq req){
        DimensionsDto dto = DimensionsDto.builder()
                .metric(Metric.valueOf(req.getMetric().name()))
                .dimensions(new ArrayList<>())
                .build();
        if(null != req.getDimensions() && req.getDimensions().size() > 0) {
          dto.setDimensions(req.getDimensions());
        }
        if(null != req.getAggregator()) {
          dto.setAggregate(req.getAggregator().name());
        }
        return dto;
    }
}
