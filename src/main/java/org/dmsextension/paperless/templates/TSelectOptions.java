package org.dmsextension.paperless.templates;

public class TSelectOptions implements IDto {

    private String id;
    private String label;
    public TSelectOptions() {
        super();
    }

    @Override
    public String toJsonString() {
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
