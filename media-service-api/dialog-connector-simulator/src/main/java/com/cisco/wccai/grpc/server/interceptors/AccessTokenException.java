package com.cisco.wccai.grpc.server.interceptors;

public class AccessTokenException extends Exception {
    public AccessTokenException(String message) {
        super(message);
    }

    public AccessTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
