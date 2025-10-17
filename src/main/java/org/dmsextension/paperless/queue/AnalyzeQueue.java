package org.dmsextension.paperless.queue;

import com.squareup.moshi.JsonAdapter;
import org.dmsextension.paperless.templates.TDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AnalyzeQueue extends Queue {

    private static final Logger logger = LoggerFactory.getLogger(AnalyzeQueue.class);
    private static final java.util.Queue<TDocument> documents = new LinkedBlockingQueue<>();

    private static final List<TDocument> alreadyInQueue = new ArrayList<>();

    public AnalyzeQueue() { }

    public static boolean addDocument(TDocument doc) {
        if (alreadyInQueue.contains(doc)) {
            logger.debug(String.format("Document %s already in queue", doc.getTitle()));
            return false;
        }
        logger.debug(String.format("Adding document, %s documents in queue", documents.size() + 1));
        alreadyInQueue.add(doc);
        return documents.offer(doc);
    }
    public static boolean addJsonDocument(String doc) {
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

    public static boolean containsDocument(TDocument doc) {
        return alreadyInQueue.contains(doc) || documents.contains(doc);
    }

    public static TDocument retrieveDocument() {
        return documents.poll();
    }
}
