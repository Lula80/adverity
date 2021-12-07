package org.challenge.rest.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.challenge.services.model.Dimension;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetGroupedMetricRq {
  @ApiModelProperty( position = 1, required = true)
  @Enumerated(EnumType.STRING)
  @NotNull
  Metric metric;

  @ApiModelProperty( example = "['Datasource']",  position = 2)
  List<Dimension> dimensions;

  @ApiModelProperty(example = "SUM", required = true, position = 3)
  @NotNull
  GetMetricsRq.Aggregate aggregator;

  @ApiModelProperty( position = 4)
  @Valid
  MetricRange having;

  @ApiModel
  public enum Metric {
    CLICKS("Clicks"),
    IMPRESSIONS("Impressions"),
    CTR("CTR");
    String name;
    Metric(String v){
      name = v;
    }
  }
}
