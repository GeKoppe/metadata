package org.dmsextension.paperless.system.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.dmsextension.paperless.templates.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class PaperlessCache extends Cache {
    private static final Logger logger = LoggerFactory.getLogger(PaperlessCache.class);
    private static LoadingCache<String, TCustomFieldTemplate> customFields;
    private static LoadingCache<String, TDocumentType> documentTypes;

    private static LoadingCache<String, TTag> tags;

    public static void initCaches() {
        logger.info("Initializing paperless caches...");
        initCustomfieldCache();
        initDocumentTypeCache();
        initTagCache();
        logger.info("Paperless caches initialized");
    }


    private static void initTagCache() {
        logger.info("Initializing tag cache");
        CacheLoader<String, TTag> loader = new CacheLoader<String, TTag>() {
            private final OkHttpClient client = new OkHttpClient.Builder().build();
            private final Moshi moshi = new Moshi.Builder().build();
            @NotNull
            @Override
            public TTag load(@NotNull String key) throws Exception {
                logger.debug(String.format("Key %s not in cache, retrieving tag from api", key));
                TTag template = new TTag();
                Request request = new Request.Builder()
                        .url(
                                SystemCache.getEnvironmentCacheValue("PAPERLESS_API_PROTOCOL")
                                        + "://"
                                        + SystemCache.getEnvironmentCacheValue("PAPERLESS_HOST")
                                        + ":"
                                        + SystemCache.getEnvironmentCacheValue("PAPERLESS_PORT")
                                        + "/api/tags/"
                                        + key
                                        + "/"
                        )
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Token " + SystemCache.getUserCacheValue(SystemCache.getEnvironmentCacheValue("PAPERLESS_SYSTEM_USER")))
                        .get()
                        .build();

                try (Response r = client.newCall(request).execute()) {
                    if (r.isSuccessful() && r.body() != null) {
                        logger.debug("Successfully executed request, parsing response");
                        JsonAdapter<TTag> adapter = moshi.adapter(TTag.class);
                        template = adapter.fromJson(r.body().string());
                        logger.debug(String.format("Template put into cache: %s", template));
                    } else {
                        logger.info("Request not successful: " + r);
                    }
                } catch (Exception ex) {
                    logger.info("Exception while fetching invoices: " + ex.getMessage());
                }
                return template;
            }
        };

        tags = CacheBuilder
                .newBuilder()
                .refreshAfterWrite(Long.parseLong(SystemCache.getEnvironmentCacheValue("TOKEN_VALIDITY")), TimeUnit.MINUTES)
                .build(loader);
        logger.info("Tag cache initialized");
    }

    private static void initCustomfieldCache() {
        logger.info("Initializing custom field cache");
        CacheLoader<String, TCustomFieldTemplate> loader = new CacheLoader<String, TCustomFieldTemplate>() {
            private final OkHttpClient client = new OkHttpClient.Builder().build();
            private final Moshi moshi = new Moshi.Builder().build();
            @NotNull
            @Override
            public TCustomFieldTemplate load(@NotNull String key)  {
                logger.debug(String.format("Key %s not in cache, retrieving custom field from api", key));
                TCustomFieldTemplate template = new TCustomFieldTemplate();
                Request request = new Request.Builder()
                        .url(
                            SystemCache.getEnvironmentCacheValue("PAPERLESS_API_PROTOCOL")
                                + "://"
                                + SystemCache.getEnvironmentCacheValue("PAPERLESS_HOST")
                                + ":"
                                + SystemCache.getEnvironmentCacheValue("PAPERLESS_PORT")
                                + "/api/custom_fields/"
                                + key
                                + "/"
                        )
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Token " + SystemCache.getUserCacheValue(SystemCache.getEnvironmentCacheValue("PAPERLESS_SYSTEM_USER")))
                        .get()
                        .build();

                try (Response r = client.newCall(request).execute()) {
                    if (r.isSuccessful() && r.body() != null) {
                        logger.debug("Successfully executed request, parsing response");
                        JsonAdapter<TCustomFieldTemplate> adapter = moshi.adapter(TCustomFieldTemplate.class);
                        template = adapter.fromJson(r.body().string());
                        logger.debug(String.format("Template put into cache: %s", template));
                    } else {
                        logger.info("Request not successful: " + r);
                    }
                } catch (Exception ex) {
                    logger.info("Exception while fetching invoices: " + ex.getMessage());
                }
                return template;
            }
        };

        customFields = CacheBuilder
                .newBuilder()
                .refreshAfterWrite(Long.parseLong(SystemCache.getEnvironmentCacheValue("TOKEN_VALIDITY")), TimeUnit.MINUTES)
                .build(loader);
        logger.info("Custom field cache initialized");
    }

    private static void initDocumentTypeCache() {
        logger.info("Initializing document type cache");
        CacheLoader<String, TDocumentType> loader = new CacheLoader<String, TDocumentType>() {
            private final OkHttpClient client = new OkHttpClient.Builder().build();
            private final Moshi moshi = new Moshi.Builder().build();
            @NotNull
            @Override
            public TDocumentType load(@NotNull String key) throws Exception {
                logger.debug(String.format("Key %s not in cache, retrieving document type from api", key));
                TDocumentType template = new TDocumentType();
                Request request = new Request.Builder()
                        .url(
                                SystemCache.getEnvironmentCacheValue("PAPERLESS_API_PROTOCOL")
                                        + "://"
                                        + SystemCache.getEnvironmentCacheValue("PAPERLESS_HOST")
                                        + ":"
                                        + SystemCache.getEnvironmentCacheValue("PAPERLESS_PORT")
                                        + "/api/document_types/"
                                        + key
                                        + "/"
                        )
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Token " + SystemCache.getUserCacheValue(SystemCache.getEnvironmentCacheValue("PAPERLESS_SYSTEM_USER")))
                        .get()
                        .build();

                try (Response r = client.newCall(request).execute()) {
                    if (r.isSuccessful() && r.body() != null) {
                        logger.debug("Successfully executed request, parsing response");
                        JsonAdapter<TDocumentType> adapter = moshi.adapter(TDocumentType.class);
                        template = adapter.fromJson(r.body().string());
                        logger.debug(String.format("Template put into cache: %s", template));
                    } else {
                        logger.info("Request not successful: " + r);
                    }
                } catch (Exception ex) {
                    logger.info("Exception while fetching invoices: " + ex.getMessage());
                }
                return template;
            }
        };

        documentTypes = CacheBuilder
                .newBuilder()
                .refreshAfterWrite(Long.parseLong(SystemCache.getEnvironmentCacheValue("TOKEN_VALIDITY")), TimeUnit.MINUTES)
                .build(loader);
        logger.info("Document type cache initialized");
    }
    public static TCustomFieldTemplate getCustomField(String id) {
        return customFields.getUnchecked(id);
    }

    /**
     * Returns {@link TDocumentType} instance for document type of given id. Contains all information for that document type.
     * @param id Id of the tag.
     * @return {@link TDocumentType} instance
     */
    public static TDocumentType getDocumentType(String id) {
        return documentTypes.getUnchecked(id);
    }

    /**
     * Returns {@link TTag} instance for tag of given id. Contains all information for that tag.
     * @param id Id of the tag.
     * @return {@link TTag} instance
     */
    public static TTag getTag(String id) {
        return tags.getUnchecked(id);
    }
}
