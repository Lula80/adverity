package org.challenge.services.model;

class StringDimension extends Dimension{

    private String value;

    StringDimension(String name, String value) {
        super(name);
        this.value = value;
    }

    void setValue(String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }

}
