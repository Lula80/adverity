package org.challenge.rest.req

import io.swagger.annotations.ApiModelProperty
import org.challenge.services.model.Dimension
import org.challenge.services.model.Metric

import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class GetGroupedMetricRq {
  @ApiModelProperty( position = 1, required = true)
  @Enumerated(EnumType.STRING)
  @NotNull
  Metric metric

  @ApiModelProperty( example = "['Datasource']",  position = 2)
  @NotNull
  @NotEmpty
  List<Dimension> dimensions

  @ApiModelProperty(example = "SUM", required = true, position = 3)
  @NotNull
  GetMetricsRq.Aggregate aggregator

  @ApiModelProperty( position = 4)
  @Valid
  MetricRange having

}
