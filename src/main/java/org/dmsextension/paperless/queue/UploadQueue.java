package org.dmsextension.paperless.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class UploadQueue extends Queue {
    private static final Logger logger = LoggerFactory.getLogger(UploadQueue.class);
    private static final java.util.Queue<Map<String, Object>> documents = new LinkedBlockingQueue<>();

    private static final java.util.Queue<Integer> failedAnalysis = new LinkedBlockingQueue<>();

    public UploadQueue() { }

    public static void addDocumentToQueue(Map<String, Object> upload) {
        documents.offer(upload);
    }

    public static Map<String, Object> getDocumentFromQueue() {
        return documents.poll();
    }

    public static void addDocumentToFailures(int id) {
        failedAnalysis.offer(id);
    }

    public static Integer getFailure() {
        return failedAnalysis.poll();
    }
}
