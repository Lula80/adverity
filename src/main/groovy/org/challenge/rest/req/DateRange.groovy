package org.challenge.rest.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import org.challenge.rest.req.validaion.DatesMatch;
import org.challenge.rest.req.validaion.RequestValidationGroups;
import org.challenge.utils.Constants
import java.time.LocalDate;

@DatesMatch(from = "from", to = "to", groups =
        [RequestValidationGroups.AggregatedGroup.class, RequestValidationGroups.FilterGroup.class])
 class DateRange  {

    @ApiModelProperty( example = "11/01/19", position = 1)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.TimeDimensions.DATE_FORMAT)
    @JsonView([Views.FilterView.class, Views.AggregatedView.class])
    LocalDate from


    @ApiModelProperty( example = "11/30/19", position = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.TimeDimensions.DATE_FORMAT)
    @JsonView([Views.FilterView.class, Views.AggregatedView.class])
    LocalDate to

}
