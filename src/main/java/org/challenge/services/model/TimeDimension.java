package org.challenge.services.model;

import org.challenge.rest.req.DateRange;

import java.time.LocalDate;

public class TimeDimension extends Dimension{

    DateRange value;

    public TimeDimension(String name, LocalDate from) {
        super(name);
        this.value.setFrom(from);
    }

    public TimeDimension(String name, DateRange value) {
        super(name);
        this.value = value;
    }
    public LocalDate getFrom() {
        return value.getFrom();
    }
    public LocalDate getTo() {
        return value.getTo();
    }
}
