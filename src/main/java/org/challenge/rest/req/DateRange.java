package org.challenge.rest.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.challenge.rest.req.validaion.DatesMatch;
import org.challenge.rest.req.validaion.RequestValidationGroups;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@DatesMatch(from = "from", to = "to", groups =
        {RequestValidationGroups.AggregatedGroup.class, RequestValidationGroups.FilterGroup.class})
public class DateRange  {
    @ApiModelProperty( example = "11/01/19", position = 1)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yy")
    @JsonView({Views.FilterView.class,Views.FullView.class})
    LocalDate from;

    @ApiModelProperty( example = "11/30/19", position = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yy")
    @JsonView({Views.FilterView.class,Views.FullView.class})
    LocalDate to;

    public LocalDate getFrom() {
        return from;
    }

    public DateRange setFrom(LocalDate from) {
        this.from = from;
        return this;
    }

    public LocalDate getTo() {
        return to;
    }

    public DateRange setTo(LocalDate to) {
        this.to = to;
        return this;
    }
}
