package org.dmsextension.paperless.templates;

import org.dmsextension.paperless.system.cache.SystemCache;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TOllamaCall implements IDto {

    private String content;
    private final String documentType;
    private final List<TCustomFieldTemplate> customFields = new ArrayList<>();
    private final String model = SystemCache.getEnvironmentCacheValue("OLLAMA_MODEL");
    private final String request = "{\"model\":\"%s\",\"stream\":false,\"messages\":[{\"role\":\"user\",\"content\":\"%s\"}],%s}";

    private final String baseMessage = "Bitte gebe mir ein JSON Objekt mit den Eigenschaften %s zurück. Alle Datumsfelder sollen ausschließlich das Format YYYY-MM-DD haben. %s Das Dokument ist vom Typ %s. Das Dokument hat folgenden Inhalt:%s";
    public TOllamaCall(String content, String documentType) {
        this.content = content;
        this.documentType = documentType;
    }

    @Override
    public String toJsonString() {
        String messageContent = String.format(this.baseMessage, this.getFields(), this.getFieldOptions(), this.documentType, this.content.replaceAll("%", "%%").replaceAll("\n", " "));
        return String.format(this.request, this.model, messageContent, this.getFormat());
    }

    @NotNull
    private String getFields() {
        StringBuilder fields = new StringBuilder();
        for (TCustomFieldTemplate t : this.customFields) {
            fields.append(t.getName()).append(",");
        }

        fields.setLength(fields.length() -1);
        return fields.toString();
    }

    @NotNull
    private String getFieldOptions() {
        StringBuilder sb = new StringBuilder();

        for (TCustomFieldTemplate t : this.customFields) {
            if (!t.getDataType().equals("select")) continue;
            if (t.getName().equals("Verschlagwortung")) continue;
            sb.append("Für das Feld ")
                    .append(t.getName())
                    .append(" hast du folgende Optionen: [");
            for (TSelectOptions e : t.getExtraData().getSelectOptions()) {
                sb.append("'")
                        .append(e.getLabel())
                        .append("'")
                        .append(",");
            }
            sb.setLength(sb.length() -1);
            sb.append("]. ");
        }
        return sb.toString();
    }

    private String getFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"format\": { \"type\":\"object\",\"properties\":{");
        for (var x : this.customFields) {
            if (x.getName().equals("Verschlagwortung")) continue;
            sb.append("\"")
                    .append(x.getName())
                    .append("\": {")
                    .append("\"type\":\"");
            switch (x.getDataType()) {
                case "string", "select", "date":
                    sb.append("string");
                    break;
                case "float", "int":
                    sb.append("number");
                    break;
                default:
                    sb.append("string");
                    break;
            }
            sb.append("\"},");
        }
        sb.setLength(sb.length() -1);
        sb.append("}}");
        return sb.toString();
    }

    public void addCustomFieldTemplate(TCustomFieldTemplate t) {
        this.customFields.add(t);
    }
}
