package org.challenge.services.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import org.challenge.utils.Constants;

/**
 * Common representation of dimensions like Daily, datasource, campaign
 */
public class Dimension{

    private String name;
    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public Dimension(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @JsonIgnore
    public Types getType() {
        if(this.getName().equals(Constants.Dimensions.TimeDimensions.DAILY))
            return Types.TIME_DIMENSION;
        else return Types.STRING_DIMENSION;
    }

    public enum Types{
        STRING_DIMENSION("StringDimension"),
        TIME_DIMENSION("TimeDimension");

        Types(String val) {
            type = val;
        }

        private final String type;

    }
}
