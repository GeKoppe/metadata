package org.dmsextension.paperless.templates;

import com.squareup.moshi.Json;

import java.util.List;

public class TDocument implements IDto {
    private int id;
    @Json(name="document_type")
    private String documentType;
    @Json(name="storage_path")
    private String storagePath;
    private String title;
    private String content;
    private List<Integer> tags;
    private String created;
    private String modified;
    private String added;
    @Json(name="deleted_at")
    private String deletedAt;
    @Json(name="mime_type")
    private String mimeType;
    @Json(name="custom_fields")
    private List<TCustomFieldValue> customFields;

    public TDocument() { }

    @Override
    public String toJsonString() {
        return String.format(
                """
                {
                "id"=%s,
                "document_type"="%s",
                "title"="%s",
                "custom_fields"="%s"
                }        
                """,
                this.id,
                this.documentType,
                this.title,
                this.customFields
        );
    }
    @Override
    public String toString() {
        return "TDocument{" +
                "id=" + id +
                ", documentType='" + documentType + '\'' +
                ", storagePath='" + storagePath + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", tags=" + tags +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                ", added='" + added + '\'' +
                ", deletedAt='" + deletedAt + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", customFields=" + customFields +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Integer> getTags() {
        return tags;
    }

    public void setTags(List<Integer> tags) {
        this.tags = tags;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public List<TCustomFieldValue> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<TCustomFieldValue> customFields) {
        this.customFields = customFields;
    }
}
