package org.dmsextension.paperless.upload;

import okhttp3.*;
import org.dmsextension.paperless.communication.ApiCommunicator;
import org.dmsextension.paperless.queue.UploadQueue;
import org.dmsextension.paperless.system.cache.PaperlessCache;
import org.dmsextension.paperless.system.cache.SystemCache;
import org.dmsextension.paperless.templates.TCustomFieldTemplate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Uploader extends ApiCommunicator implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(Uploader.class);
    private final List<Map<String, Object>> documentUploads = new ArrayList<>();

    public Uploader() {

    }
    private void getDocumentUploads() {
        Map<String, Object> upload;
        do {
            upload = UploadQueue.getDocumentFromQueue();
            if (upload != null) documentUploads.add(upload);
        } while (upload != null);
    }

    private String parseDocumentName(@NotNull Map<String, Object> info) {
        switch (Integer.parseInt("" + info.get("document_type"))) {
            case 1:
                String value = "" + info.get("Rechnungsnummer");
                if (value.length() > 8) value = value.substring(0, 8);
                return String.format("Rechnung %s - %s - %s",  value, (String) info.get("Rechnungssteller"), info.get("Belegdatum"));

            default:
                return "";
        }
    }
    private void uploadDocuments() {
        for (var d : this.documentUploads) {
            if ((Integer) d.get("upload_tries") >= 3) {
                continue;
            }
            d.put("Verschlagwortung", "Automatisiert");
            StringBuilder bodyString = new StringBuilder();
            bodyString.append("{");

            String customFieldBody = this.buildCustomFieldBody(d);
            bodyString.append(customFieldBody)
                    .append(",")
                    .append("\"title\":\"")
                    .append(this.parseDocumentName(d))
                    .append("\"")
                .append("}");

            logger.debug(String.format("Built body %s", customFieldBody));
            OkHttpClient client = this.getClient();
            MediaType json = MediaType.get("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(bodyString.toString(), json);

            Request req = new Request.Builder()
                    .url(
                            SystemCache.getEnvironmentCacheValue("PAPERLESS_API_PROTOCOL")
                                    + "://"
                                    + SystemCache.getEnvironmentCacheValue("PAPERLESS_HOST")
                                    + ":"
                                    + SystemCache.getEnvironmentCacheValue("PAPERLESS_PORT")
                                    + "/api/documents/"
                                    + d.get("id")
                                    + "/"
                    )
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Token " + SystemCache.getUserCacheValue(SystemCache.getEnvironmentCacheValue("PAPERLESS_SYSTEM_USER")))
                    .patch(body)
                    .build();

            try (Response r = client.newCall(req).execute()) {
                if (r.isSuccessful()) {
                    logger.info("Successfully updated document with id " + d.get("id"));
                } else {
                    logger.info("Update was not successful");
                }
            } catch (Exception ex) {

            }
        }
        this.documentUploads.clear();
    }

    private String buildCustomFieldBody(Map<String, Object> info) {
        this.logger.debug("Building custom field body for " + info);
        StringBuilder sb = new StringBuilder();

        sb.append("\"custom_fields\": [");
        boolean first = true;
        for (var name : info.keySet()) {
            if (name.equals("id") || name.equals("upload_tries")) continue;
            StringBuilder currentFIeld = new StringBuilder();

            if (info.get(name) == null || info.get(name).equals("")) continue;
            if (!first) currentFIeld.append(",");
            else first = false;

            currentFIeld.append("{");

            TCustomFieldTemplate template = PaperlessCache.getCustomFieldByName(name);
            if (template == null) continue;

            currentFIeld.append("\"field\":")
                    .append(template.getId())
                    .append(",");
            if (!template.getDataType().equals("select")) {
                switch (template.getDataType()) {
                    case "string", "date":
                        String value = "" + info.get(name);
                        if (value.length() > 254) value = value.substring(0,254);
                        currentFIeld
                                .append("\"value\":")
                                .append("\"")
                                .append(value)
                                .append("\"");
                        break;
                    case "int", "float":
                        currentFIeld
                                .append("\"value\":")
                                .append(info.get(name));
                        break;
                }
            } else {
                boolean added = false;
                for (var x : template.getExtraData().getSelectOptions()) {
                    if (x.getLabel().equals(info.get(name))) {
                        added = true;
                        currentFIeld
                                .append("\"value\":")
                                .append("\"")
                                .append(x.getId())
                                .append("\"");
                        break;
                    }
                }
                if (!added) continue;
            }
            currentFIeld.append("}");
            sb.append(currentFIeld.toString());
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public void run() {
        try {
            this.getDocumentUploads();
            this.uploadDocuments();
        } catch (Exception ex) {
            this.logger.debug("Exception occurred in execution: " + ex);
        }
    }


}
