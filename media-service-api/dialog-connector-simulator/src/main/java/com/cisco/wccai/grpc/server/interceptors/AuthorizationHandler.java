package com.cisco.wccai.grpc.server.interceptors;

public interface AuthorizationHandler {
    /**
     * Validates the given token.
     *
     * @param token the token to validate
     * @return true if the token is valid, false otherwise
     * @throws AccessTokenException if any validation error occurs
     */
    boolean validateToken(String token) throws AccessTokenException;
}
