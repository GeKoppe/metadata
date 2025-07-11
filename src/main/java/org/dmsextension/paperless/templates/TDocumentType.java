package org.dmsextension.paperless.templates;

import com.squareup.moshi.Json;

public class TDocumentType {
    private int id;
    private String slug;
    private String name;
    private String match;
    @Json(name="document_count")
    private int documentCount;

    public TDocumentType() { }

    @Override
    public String toString() {
        return "TDocumentType{" +
                "id=" + id +
                ", slug='" + slug + '\'' +
                ", name='" + name + '\'' +
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
