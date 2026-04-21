package org.dmsextension.paperless.templates;

import com.squareup.moshi.Json;

import java.util.List;

public class TExtraData implements IDto {
    @Json(name="select_options")
    private List<TSelectOptions> selectOptions;

    public TExtraData() {
    }

    @Override
    public String toString() {
        return "TExtraData{" +
                "selectOptions=" + selectOptions +
                '}';
    }

    @Override
    public String toJsonString() {
        return null;
    }

    public List<TSelectOptions> getSelectOptions() {
        return selectOptions;
    }

    public void setSelectOptions(List<TSelectOptions> selectOptions) {
        this.selectOptions = selectOptions;
    }
}
