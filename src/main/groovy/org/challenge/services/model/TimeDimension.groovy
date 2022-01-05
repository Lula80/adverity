package org.challenge.services.model;

import org.challenge.rest.req.DateRange

import java.time.LocalDate;

 class TimeDimension extends Dimension{

    DateRange value;

    TimeDimension(String name, DateRange value) {
        super(name);
        this.value = value;
    }
     LocalDate getFrom() {
        return value.getFrom();
    }
     LocalDate getTo() {
        return value.getTo();
    }
}
