package org.dmsextension.paperless.analyze;

import com.squareup.moshi.JsonAdapter;
import okhttp3.*;
import org.dmsextension.paperless.communication.ApiCommunicator;
import org.dmsextension.paperless.queue.AnalyzeQueue;
import org.dmsextension.paperless.queue.UploadQueue;
import org.dmsextension.paperless.system.cache.PaperlessCache;
import org.dmsextension.paperless.system.cache.SystemCache;
import org.dmsextension.paperless.templates.TDocument;
import org.dmsextension.paperless.templates.TOllamaAnswer;
import org.dmsextension.paperless.templates.TOllamaCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;

public class Analyzer extends ApiCommunicator implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(Analyzer.class);
    private final ArrayList<TDocument> documents = new ArrayList<>();

    public Analyzer() {

    }

    @Override
    public void run() {
        try {
            this.getQueuedDocuments();
            this.analyzeDocuments();
        } catch (Exception ex) {
            this.logger.info("Exception occurred in execution: " + ex);
        }
    }

    private void getQueuedDocuments() {
        TDocument doc;
        int docs = 0;
        do {
            doc = AnalyzeQueue.retrieveDocument();
            if (doc != null) this.documents.add(doc);
            docs++;
        } while (doc != null && docs < 5);
    }

    private void analyzeDocuments() {
        for (var document : this.documents) {
            if (document.getAnalyzeTries() >= 3) {
                UploadQueue.addDocumentToFailures(document.getId());
                continue;
            }
            logger.debug(String.format("Analyzing document %s", document.toString()));
            try {
                TOllamaCall call = new TOllamaCall(document.getContent(), PaperlessCache.getDocumentType(document.getDocumentType()).getName());
                logger.debug("Adding custom fields to ollama call");
                for (var field : document.getCustomFields()){
                    call.addCustomFieldTemplate(PaperlessCache.getCustomField("" + field.getField()));
                }

                logger.debug("Creating http request");
                OkHttpClient client = this.getClient();
                MediaType json = MediaType.get("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(call.toJsonString(), json);

                Request req = new Request.Builder()
                        .url(SystemCache.getEnvironmentCacheValue("OLLAMA_CHAT_URL"))
                        .header("Content-Type", "application/json")
                        .post(body)
                        .build();

                logger.debug("Using body " + call.toJsonString());
                try (Response r = client.newCall(req).execute()) {
                    if (r.isSuccessful() && r.body() != null) {
                        logger.debug("Request successful, parsing body");

                        JsonAdapter<TOllamaAnswer> adapter = this.getMoshi().adapter(TOllamaAnswer.class);
                        TOllamaAnswer answer = adapter.fromJson(r.body().string());

                        if (answer != null && answer.getMessage() != null) {
                            Map<String, Object> result = answer.getMessage().getContentAsMap(this.getMoshi());
                            result.put("id", "" + document.getId());
                            result.put("upload_tries", 0);
                            result.put("document_type", document.getDocumentType());
                            logger.debug("Result: " + result);

                            UploadQueue.addDocumentToQueue(result);
                            logger.debug("Added map to upload queue");
                        } else {
                            logger.debug("Request not successful, adding document back to queue");
                            document.addAnalyzeTry();
                            AnalyzeQueue.addDocument(document);
                        }
                    } else {
                        logger.debug("Request not successful, adding document back to queue");
                        document.addAnalyzeTry();
                        AnalyzeQueue.addDocument(document);
                    }
                } catch (Exception ex) {
                    logger.info("Exception in method api call, adding document back to queue: " + ex);
                    document.addAnalyzeTry();
                    AnalyzeQueue.addDocument(document);
                }
            } catch (Exception ex) {
                logger.info("Exception occurred while trying to analyze document, adding document " + document + " back to queue. Exception: " + ex);
                document.addAnalyzeTry();
                AnalyzeQueue.addDocument(document);
            }
        }
        this.documents.clear();
    }
}
