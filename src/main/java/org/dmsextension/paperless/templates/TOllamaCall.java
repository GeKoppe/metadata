package org.dmsextension.paperless.templates;

import org.dmsextension.paperless.system.cache.SystemCache;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TOllamaCall implements IDto {
    private final Logger logger = LoggerFactory.getLogger(TOllamaCall.class);
    private final String content;
    private final String documentType;
    private final List<TCustomFieldTemplate> customFields = new ArrayList<>();
    private final String model = SystemCache.getEnvironmentCacheValue("OLLAMA_MODEL");
    private final String request = "{\"model\":\"%s\",\"stream\":false,\"messages\":[{\"role\":\"user\",\"content\":\"%s\"}],%s,%s}";

    private final String baseMessage = "Antworte NUR mit JSON, kein Fließtext. Wenn keiner der Rechnungssteller auf dem Dokument ist, wähle Sonstige. Felder: %s. Das Dokument ist ein %s. Inhalt:%s";
    public TOllamaCall(String content, String documentType) {
        this.content = content;
        this.documentType = documentType;
    }

    @Override
    public String toJsonString() {
        String messageContent = String.format(this.baseMessage, this.getJsonFormat(), this.documentType, this.replaceJsonCharsInContent());
        return String.format(this.request, this.model, messageContent, this.getFormat(), this.getOptions());
    }

    @NotNull
    private String replaceJsonCharsInContent() {
        return this.content
                .replaceAll("%", "%%")
                .replaceAll("\n", " ")
                .replace("\"", "\\\"");
    }

    private String getOptions() {
        return String.format("\"options\": {\"temperature\": 0, \"num_predict\": %s}", (this.customFields.size() + 1) * 35);
    }

    /**
     * @deprecated
     * @return Fields to be returned by the api
     */
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
            if (t.getName().equals("Verschlagwortung") || t.getName().equals("Bereit für Dokumentation")) continue;
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

    @NotNull
    private String getFieldOptions(@NotNull TCustomFieldTemplate t) {
        StringBuilder sb = new StringBuilder();
        sb.append("Optionen: ");
        for (TSelectOptions e : t.getExtraData().getSelectOptions()) {
            sb.append("'")
                    .append(e.getLabel())
                    .append("'")
                    .append(",");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    @NotNull
    private String getFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"format\": { \"type\":\"object\",\"properties\":{");
        List<String> req = new ArrayList<>();
        for (var x : this.customFields) {
            if (x.getName().equals("Verschlagwortung") || x.getName().equals("Bereit für Dokumentation")) continue;
            req.add(x.getName());
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
        sb.setLength(sb.length() - 1);
        sb.append("},");
        sb.append("\"required\": [");
        for (var r : req) {
            sb.append("\"").append(r).append("\"");
            sb.append(",");
        }
        sb.setLength(sb.length() - 1);
        sb.append("]}");
        logger.debug("Built call: {}", sb);
        return sb.toString();
    }

    @NotNull
    private String getJsonFormat() {
        StringBuilder sb = new StringBuilder();

        sb.append("");
        for (var x : this.customFields) {
            if (x.getName().equals("Verschlagwortung") || x.getName().equals("Bereit für Dokumentation")) continue;
            sb.append(x.getName())
                    .append(" (");
            switch (x.getDataType()) {
                case "string":
                    sb.append("string (max: 254 Zeichen)");
                    break;
                case "date":
                    sb.append("YYYY-MM-DD");
                    break;
                case "float", "int":
                    sb.append("number");
                    break;
                case "select":
                    sb.append(this.getFieldOptions(x));
                    break;
                default:
                    sb.append("string");
                    break;
            }
            sb.append(")")
                .append(",");
        }
        sb.setLength(sb.length() -1);

        return sb.toString();
    }

    public void addCustomFieldTemplate(TCustomFieldTemplate t) {
        this.customFields.add(t);
    }
}
