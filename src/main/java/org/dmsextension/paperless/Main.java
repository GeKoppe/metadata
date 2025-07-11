package org.dmsextension.paperless;

import org.dmsextension.paperless.system.cache.Cache;
import org.dmsextension.paperless.system.cache.PaperlessCache;
import org.dmsextension.paperless.system.cache.SystemCache;
import org.dmsextension.paperless.templates.TCustomFieldTemplate;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        initCaches();
        TCustomFieldTemplate cf = PaperlessCache.getCustomField("1");
        logger.info(cf.toString());
    }

    private static void initCaches() {
        logger.info("Initializing caches...");
        SystemCache.initCaches();
        PaperlessCache.initCaches();
        logger.info("Successfully initialized caches");
    }
}