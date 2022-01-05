package org.challenge.rest.controller

import org.challenge.rest.req.DateRange
import org.challenge.rest.req.GetGroupedMetricRq
import org.challenge.rest.req.GetMetricsRq
import org.challenge.services.model.DimensionsDto
import org.challenge.services.model.Metric
import org.challenge.services.model.StringDimension
import org.challenge.services.model.TimeDimension
import org.challenge.utils.Constants
import org.springframework.stereotype.Service

@Service
class Rest2DtoMappingService {

    DimensionsDto mapToNonNullDimensions(GetMetricsRq req){
        DimensionsDto dto = constructDimensionsDto(req.getMetric())
        initDimensionsAggregate(dto, req)
        return dto
    }

    DimensionsDto mapToNonNullDimensionsFiltered(GetMetricsRq req){
        DimensionsDto dto = constructDimensionsDto(req.getMetricToFilter())
        initDimensionsAggregate(dto, req)
        return dto
    }

    DimensionsDto mapToNonNullDimensions(GetGroupedMetricRq req){
        DimensionsDto dto = constructDimensionsDto(req.getMetric())
        dto.setDimensions(req.getDimensions())
        dto.setAggregate(req.getAggregator().name())
        return dto
    }

    private DimensionsDto constructDimensionsDto(Metric metricPar) {
        DimensionsDto dto = new DimensionsDto()
        dto.setMetric(metricPar)
        dto.setDimensions(new ArrayList<>())
        return dto
    }

    private def isStringSet = { String dataSource ->
        dataSource && !dataSource.isBlank()
    }

    private def isDailySet = { DateRange dates->
      dates && (dates.getFrom() || dates.getTo())
    }

    private void initDimensionsAggregate (DimensionsDto dto, GetMetricsRq req){
        if(isDailySet(req.getDaily())) {
            dto.getDimensions()<<new TimeDimension(Constants.TimeDimensions.DAILY, req.getDaily())
        }
        if(isStringSet(req.getDataSource())) {
            dto.getDimensions()<<new StringDimension(Constants.StringDimensions.DATASOURCE, req.getDataSource())
        }
        if(isStringSet(req.getCampaign())) {
            dto.getDimensions()<<new StringDimension(Constants.StringDimensions.CAMPAIGN, req.getCampaign())
        }
        if(req.getAggregator()) {
            dto.setAggregate(req.getAggregator().name())
        }
    }
}
