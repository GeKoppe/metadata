package org.dmsextension.paperless.queue;

import com.squareup.moshi.JsonAdapter;
import org.dmsextension.paperless.templates.TDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class AnalyzeQueue extends Queue {

    private static final Logger logger = LoggerFactory.getLogger(AnalyzeQueue.class);
    private static final java.util.Queue<TDocument> documents = new LinkedBlockingQueue<>();
    private static final java.util.Queue<String> ocr = new LinkedBlockingQueue<>();

    public AnalyzeQueue() { }

    public static boolean addDocument(TDocument doc) {
        return documents.offer(doc);
    }
    public static boolean addDocument(String doc) {
        JsonAdapter<TDocument> adapter = moshi.adapter(TDocument.class);
        TDocument document;
        try {
            document = adapter.fromJson(doc);
        } catch (IOException ex) {
            logger.info("Exception occurred while converting String to TDocument: " + ex.getMessage());
            return false;
        }
        return addDocument(document);
    }

    public static TDocument retrieveDocument() {
        return documents.poll();
    }

    public static boolean addOcr(String o) {
        return ocr.offer(o);
    }

    public static String retrieveOcr() {
        return ocr.poll();
    }
}
