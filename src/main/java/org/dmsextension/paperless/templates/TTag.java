package org.dmsextension.paperless.templates;

import com.squareup.moshi.Json;

public class TTag implements IDto {

    private int id;
    private String slug;
    private String name;
    private String color;
    @Json(name="text_color")
    private String textColor;
    private String match;
    @Json(name="document_count")
    private int documentCount;

    public TTag() { }

    @Override
    public String toJsonString() {
        return null;
    }

    @Override
    public String toString() {
        return "TTag{" +
                "id=" + id +
                ", slug='" + slug + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", textColor='" + textColor + '\'' +
                ", match='" + match + '\'' +
                ", documentCount=" + documentCount +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public int getDocumentCount() {
        return documentCount;
    }

    public void setDocumentCount(int documentCount) {
        this.documentCount = documentCount;
    }
}
