package org.challenge.rest.req;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.PositiveOrZero;


class MetricRange {
  @ApiModelProperty( example = "90",  position = 1)
  @PositiveOrZero
  Integer minimum;
  @ApiModelProperty( example = "1000", position = 2)
  @PositiveOrZero
  Integer maximum;

  String getSqlCondition(GString metric){
    if(minimum && maximum){
      return "$metric >= $minimum AND $metric <= $maximum"
    }
    if(minimum)
      return  "$metric >= $minimum"
    if(maximum)
      return  "$metric <= $maximum"
    return "";

  }

  boolean testMetric(int metric){
    if(minimum && maximum ){
      return metric >= minimum && metric <= maximum;
    }
    if(minimum )
      return  metric >= minimum;
    if(maximum )
      return metric <= maximum;
    return true;
  }
}
