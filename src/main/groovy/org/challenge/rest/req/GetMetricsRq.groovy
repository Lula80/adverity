package org.challenge.rest.req;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.challenge.rest.req.validaion.RequestValidationGroups
import org.challenge.services.model.Metric;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


 class GetMetricsRq {

    @ApiModelProperty( position = 1, required = true)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = [RequestValidationGroups.AggregatedGroup.class])
    @JsonView(Views.AggregatedView.class)
    private Metric metric

    @ApiModelProperty( position = 1)
    @JsonView(Views.FilterView.class)
    private Metric metricToFilter

    @ApiModelProperty( position = 2)
    @JsonView([Views.FilterView.class, Views.AggregatedView.class])
    @Valid
    private DateRange daily

    @ApiModelProperty( example = "Google Ads", position = 3)
    @JsonView([Views.FilterView.class, Views.AggregatedView.class])
    private String dataSource

    @ApiModelProperty( example = "Adventmarkt Touristik", position = 4)
    @JsonView([Views.FilterView.class, Views.AggregatedView.class])
    private String campaign

    @ApiModelProperty( position = 5, required = true)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = RequestValidationGroups.AggregatedGroup.class)
    @JsonView(Views.AggregatedView.class)
    private Aggregate aggregator

    Metric getMetric() {
        return metric
    }

    DateRange getDaily() {
        return daily
    }

    String getDataSource() {
        return dataSource
    }

    String getCampaign() {
        return campaign
    }

    Aggregate getAggregator() {
        return aggregator
    }

    Metric getMetricToFilter() {
        return metricToFilter
    }
    @ApiModel
    enum Aggregate {
        AVG,
        COUNT,
        MAX,
        MIN,
        SUM;
    }
}
