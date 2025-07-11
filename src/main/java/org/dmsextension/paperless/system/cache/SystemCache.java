package org.dmsextension.paperless.system.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SystemCache extends Cache {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SystemCache.class);
    /**
     * Cache that holds all environment variables
     */
    private static LoadingCache<String, String> environemtCache;
    /**
     * Cache that holds user token information
     */
    private static LoadingCache<String, String> userCache;
    public SystemCache() {}

    public static void initCaches() {
        logger.info("Initializing system caches...");
        locks.put("env", new ReentrantLock());
        locks.put("user", new ReentrantLock());
        initEnvironmentCache();
        initUserCache();
        logger.info("System caches initialized");
    }

    /**
     * Initializes environment cache {@link SystemCache#environemtCache}.
     */
    private static void initEnvironmentCache() {
        logger.info("Initializing environment cache");
        CacheLoader<String, String> loader = new CacheLoader<>() {
            @NotNull
            @Override
            public String load(@NotNull String key)  {
                logger.debug(String.format("Key %s not in cache, fetching from environment variables", key));
                return System.getenv(key);
            }
        };
        environemtCache = CacheBuilder
                .newBuilder()
                .refreshAfterWrite(Integer.parseInt(System.getenv("CACHE_REFRESH")), TimeUnit.MINUTES)
                .build(loader);
        logger.info("Environment cache initialized");
    }

    /**
     * Initializes user cache
     */
    private static void initUserCache() {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            private static class Token {
                public String token;
            }
            @NotNull
            @Override
            public String load(@NotNull String userName) throws Exception {
                logger.debug(String.format("Getting new token for user %s", userName));
                String token;

                // Build json body
                String json = String.format("{ \"username\": \"%s\",\"password\": \"%s\" }",
                        environemtCache.getUnchecked("PAPERLESS_SYSTEM_USER"),
                        environemtCache.getUnchecked("PAPERLESS_SYSTEM_PW")
                );

                // Build request
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(
                                environemtCache.getUnchecked("PAPERLESS_API_PROTOCOL")
                                + "://"
                                + environemtCache.getUnchecked("PAPERLESS_HOST")
                                + ":"
                                + environemtCache.getUnchecked("PAPERLESS_PORT")
                                + "/api/token/"
                        )
                        .header("Content-Type", "application/json")
                        .post(RequestBody.create(json, MediaType.parse("application/json")))
                        .build();

                // Send request
                logger.debug("Sending request...");
                try (Response r = client.newCall(request).execute()) {
                    if (r.isSuccessful()) {
                        logger.debug(String.format("Request successful, getting token for user %s", userName));
                        JsonAdapter<Token> adapter = (new Moshi.Builder().build()).adapter(Token.class);
                        token = Objects.requireNonNull(adapter.fromJson(r.body().string())).token;
                    } else {
                        logger.info(String.format("Request not successful, could not retrieve token for user %s", userName));
                        token = "";
                    }
                } catch (Exception ex) {
                    logger.info(String.format("Exception occurred while retrieving token: %s", ex.getMessage()));
                    token = "";
                }
                return token;
            }
        };

        userCache = CacheBuilder
                .newBuilder()
                .refreshAfterWrite(Long.parseLong(environemtCache.getUnchecked("TOKEN_VALIDITY")), TimeUnit.MINUTES)
                .build(loader);
    }

    @NotNull
    public static String getEnvironmentCacheValue(String key) {
        return environemtCache.getUnchecked(key);
    }

    @NotNull
    public static String getUserCacheValue(String userName) {
        return userCache.getUnchecked(userName);
    }
}
