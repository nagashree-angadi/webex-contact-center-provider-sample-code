package com.cisco.wccai.grpc.server.interceptors;

import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizationHandlerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationHandlerFactory.class);

    public enum AuthTokenType {
        JWT,     // JSON Web Token (JWT)
        OAUTH2,  // OAuth2 token
        NOAUTH   // No authorization token provided
    }
    public static AuthorizationHandler getAuthorizationHandler(String token) throws AccessTokenException {
        AuthTokenType tokenType = getAuthTokenType(token);  // Determine token type
        switch (tokenType) {
            case JWT:
                LOGGER.info("Found JWT Token");
                return new JWTAuthorizationHandler();
            default:
                throw new AccessTokenException("Invalid authorization token");
        }
    }

    // Helper method to determine the token type
    private static AuthTokenType getAuthTokenType(String token) {
        if (token == null || token.isEmpty()) {
            return AuthTokenType.NOAUTH;
        } else if (isJWT(token)) {
            return AuthTokenType.JWT;
        } else {
            return AuthTokenType.OAUTH2;
        }
    }

    // We simply Cannot rely on 3 .(DOTs),as event OAUTH token can have 3 .(DOT) in it
    // Since JWT is also OAuth token, we need to check the token_type claim to differentiate between JWT and OAUTH token
    private static boolean isJWT(String token) {
        try {
            var tokenType = SignedJWT.parse(token).getJWTClaimsSet().getClaim("token_type");
            // For JWS (Not jwt) token, token_type claim should not be present
            return tokenType == null || !tokenType.toString().equalsIgnoreCase("Bearer");
        } catch (Exception ex) {
            return false;
        }
    }

    // Token extraction logic
    public static String extractToken(String authHeader) {
        // Determine the token type
        AuthTokenType tokenType = getAuthTokenType(authHeader);

        switch (tokenType) {
            case OAUTH2:
                if (authHeader != null) {
                    return authHeader;
                } else {
                    throw new IllegalArgumentException("Invalid OAuth2 authorization header format");
                }
            case JWT:
                // Return JWT as is
                return authHeader;
            case NOAUTH:
                // Return an empty string for no authorization
                return "";
            default:
                throw new IllegalArgumentException("Invalid authorization header format or token type");
        }
    }
}