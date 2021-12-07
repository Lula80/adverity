package org.challenge.rest.req;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.challenge.rest.req.validaion.RequestValidationGroups;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonView(Views.FullView.class)
public class GetMetricsRq {
    @ApiModelProperty( position = 1, required = true)
    @Enumerated(EnumType.STRING)
    @JsonView(Views.FilterView.class)
    @NotNull(groups = {RequestValidationGroups.AggregatedGroup.class, RequestValidationGroups.FilterGroup.class})
    GetGroupedMetricRq.Metric metric;

    @ApiModelProperty( position = 2)
    @JsonView(Views.FilterView.class)
    @Valid
    DateRange daily;

    @ApiModelProperty( example = "Google Ads", position = 3)
    @JsonView(Views.FilterView.class)
    String dataSource;

    @ApiModelProperty( example = "Adventmarkt Touristik", position = 4)
    @JsonView(Views.FilterView.class)
    String campaign;

    @ApiModelProperty( position = 5, required = true)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = RequestValidationGroups.AggregatedGroup.class)
    Aggregate aggregator;

    @ApiModel
    public enum Aggregate {
        AVG,
        COUNT,
        MAX,
        MIN,
        SUM;
    }
}
