package org.challenge.services.model;

public class StringDimension extends Dimension{

    private String value;

    public StringDimension(String name, String value) {
        super(name);
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
