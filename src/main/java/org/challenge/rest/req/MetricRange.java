package org.challenge.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import javax.validation.constraints.PositiveOrZero;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MetricRange {
  @ApiModelProperty( example = "90",  position = 1)
  @PositiveOrZero
  Integer minimum;
  @ApiModelProperty( example = "1000", position = 2)
  @PositiveOrZero
  Integer maximum;

  public String getSqlCondition(){
    if(minimum == null && maximum == null){
      return "";
    }
    return  minimum != null?">="+minimum: "<="+maximum;
  }

  public boolean testMetric(int metric){
    if(minimum != null && maximum != null){
      return metric >= minimum && metric <= maximum;
    }
    if(minimum != null)
      return  metric >= minimum;
    if(maximum !=null)
      return metric <= maximum;
    return true;
  }
}
