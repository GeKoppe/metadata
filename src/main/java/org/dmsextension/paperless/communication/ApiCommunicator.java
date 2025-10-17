package org.dmsextension.paperless.communication;

import com.squareup.moshi.Moshi;
import okhttp3.OkHttpClient;
import org.dmsextension.paperless.system.cache.SystemCache;

import java.util.concurrent.TimeUnit;

public abstract class ApiCommunicator {
    /**
     * Http client for fetching documents from paperless api
     */
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.MINUTES)
            .callTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .build();

    private String baseUrl;
    private final String authenticationString;

    private final Moshi moshi = new Moshi.Builder().build();

    protected Moshi getMoshi() {
        return this.moshi;
    }

    protected ApiCommunicator() {
        this.authenticationString = String.format("Token %s", SystemCache.getUserCacheValue("sysuser"));
        this.setBaseUrl(
                SystemCache.getEnvironmentCacheValue("PAPERLESS_API_PROTOCOL")
                        + "://"
                        + SystemCache.getEnvironmentCacheValue("PAPERLESS_HOST")
                        + ":"
                        + SystemCache.getEnvironmentCacheValue("PAPERLESS_PORT")
                        + "/"
        );
    }

    public OkHttpClient getClient() {
        return client;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected String getAuthenticationString() {
        return this.authenticationString;
    }
}
