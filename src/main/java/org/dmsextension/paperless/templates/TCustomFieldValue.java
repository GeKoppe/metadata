package org.dmsextension.paperless.templates;

public class TCustomFieldValue implements IDto{
    private String value;
    private int field;

    public TCustomFieldValue () { }

    @Override
    public String toJsonString() {
        return null;
    }

    @Override
    public String toString() {
        return "TCustomFieldValue{" +
                "value='" + value + '\'' +
                ", field=" + field +
                '}';
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }
}
