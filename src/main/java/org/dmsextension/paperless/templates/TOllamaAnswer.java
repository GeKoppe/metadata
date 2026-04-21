package org.dmsextension.paperless.templates;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class TOllamaAnswer implements IDto {

    private Message message;

    public static class Message {
        private String role;
        private String content;

        public Message() { }


        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Map<String, Object> getContentAsMap(Moshi moshi) throws IOException {
            Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
            JsonAdapter<Map<String, Object>> adapter = moshi.adapter(type);
            return adapter.fromJson(content);
        }
    }
    @Override
    public String toJsonString() {
        return null;
    }

    public Message getMessage() {
        return this.message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
