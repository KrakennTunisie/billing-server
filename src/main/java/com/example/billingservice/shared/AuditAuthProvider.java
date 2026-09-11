package com.example.billingservice.shared;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuditAuthProvider {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token"; // adapte au nom réel du cookie

    private final JwtDecoder jwtDecoder;
    private final HttpServletRequest request;

    public AuditActor getCurrentActor() {

        String token = extractTokenFromCookies();
        log.info("token: {}", token);
        if (token == null || token.isBlank()) {
            log.warn("No access token cookie found to resolve current actor");
            return null;
        }

        Jwt jwt = jwtDecoder.decode(token);

        return new AuditActor(
                jwt.getSubject(),
                jwt.getClaimAsString("given_name"),
                jwt.getClaimAsString("family_name"),
                extractRoles(jwt)
        );
    }

    private String extractTokenFromCookies() {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        log.info("Cookies received: {}", Arrays.stream(cookies)
                .map(Cookie::getName)
                .toList());

        return Arrays.stream(cookies)
                .filter(cookie -> ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private List<String> extractRoles(Jwt jwt) {

        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        if (realmAccess == null) {
            return Collections.emptyList();
        }

        Object roles = realmAccess.get("roles");

        if (roles instanceof List<?> roleList) {
            return roleList.stream()
                    .map(Object::toString)
                    .toList();
        }

        return Collections.emptyList();
    }
}