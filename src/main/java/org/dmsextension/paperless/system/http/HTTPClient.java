package org.dmsextension.paperless.system.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public abstract class HTTPClient {

    protected Logger logger = LoggerFactory.getLogger(HTTPClient.class);
    protected final OkHttpClient client = new OkHttpClient();
    protected HashMap<String, String> headers;
    protected String url;
    protected HTTPClient(String url) {
        this.url = url;
    }

    public HashMap<String,String> get() {
        Request.Builder builder = new Request.Builder();
        builder.url(this.url);

        for (String key : this.headers.keySet()) {
            builder.header(key, this.headers.get(key));
        }
        builder.get();

        Request request = builder.build();
        HashMap<String, String> map = new HashMap<>();
        try (Response response = this.client.newCall(request).execute()) {
            this.logger.trace(response.toString());
            map.put("code", String.format("%s", response.code()));
            if (response.body() != null) {
                map.put("body", response.body().string());
            }
        }  catch (Exception exception) {

        }
        return map;
    }

    public HashMap<String,String> get(HashMap<String, String> headers) {
        this.headers = headers;
        return this.get();
    }
}
