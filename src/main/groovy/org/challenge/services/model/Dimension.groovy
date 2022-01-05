package org.challenge.services.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import org.challenge.utils.Constants

/**
 * Common representation of dimensions like Daily, datasource, campaign
 */
class Dimension{
    @JsonValue
    private String name;
    @JsonValue
    String getName() {
        return name;
    }

    @JsonCreator
    Dimension(String name) {
        this.name = name;
    }

    @Override
    String toString() {
        return name;
    }

    @JsonIgnore
    Types getType() {
        if(this.name == Constants.TimeDimensions.DAILY)
            return Types.TIME_DIMENSION;
        else return Types.STRING_DIMENSION;
    }

    enum Types{
        STRING_DIMENSION("StringDimension"),
        TIME_DIMENSION("TimeDimension")

        private final String type

        Types(String val) {
            type = val
        }
    }
}
