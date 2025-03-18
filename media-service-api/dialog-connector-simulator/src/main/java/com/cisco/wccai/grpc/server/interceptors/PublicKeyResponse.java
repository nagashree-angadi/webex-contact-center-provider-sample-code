package com.cisco.wccai.grpc.server.interceptors;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PublicKeyResponse {

    @JsonProperty("keys")
    private List<Key> keys;
    private Long expirationAt;

    @Getter
    @Setter
    public static class Key {
        @JsonProperty("kty")
        private String kty;

        @JsonProperty("e")
        private String e;

        @JsonProperty("use")
        private String use;

        @JsonProperty("kid")
        private String kid;

        @JsonProperty("n")
        private String n;

        @Override
        public String toString() {
            return "{" +
                    "\"kty\":\"" + kty + "\"," +
                    "\"e\":\"" + e + "\"," +
                    "\"use\":\"" + use + "\"," +
                    "\"kid\":\"" + kid + "\"," +
                    "\"n\":\"" + n + "\"" +
                    "}";
        }
    }
}
