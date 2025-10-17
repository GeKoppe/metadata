package org.dmsextension.paperless;

import org.dmsextension.paperless.analyze.Analyzer;
import org.dmsextension.paperless.fetch.DocumentFetcher;
import org.dmsextension.paperless.system.cache.PaperlessCache;
import org.dmsextension.paperless.system.cache.SystemCache;
import org.dmsextension.paperless.upload.Uploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        initCaches();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
        scheduler.scheduleAtFixedRate(new DocumentFetcher(), 0, 5, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(new Analyzer(), 1, 5, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(new Uploader(), 1, 1, TimeUnit.MINUTES);

    }

    private static void initCaches() {
        logger.info("Initializing caches...");
        SystemCache.initCaches();
        PaperlessCache.initCaches();
        logger.info("Successfully initialized caches");
    }
}