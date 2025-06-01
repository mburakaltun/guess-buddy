package com.mburakaltun.guessbuddy.common.filter;

import com.mburakaltun.guessbuddy.common.util.JwtUtility;
import com.mburakaltun.guessbuddy.authentication.properties.AuthenticationProperties;
import com.mburakaltun.guessbuddy.common.model.request.CreateRequestLogRequest;
import com.mburakaltun.guessbuddy.common.service.RequestLogService;
import com.mburakaltun.guessbuddy.common.util.StringUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Order(2)
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final AuthenticationProperties authenticationProperties;
    private final RequestLogService requestLogService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        boolean unauthorized = false;

        try {
            String token = getTokenFromRequest(wrappedRequest);
            if (token == null) {
                log.warn("No JWT token found in request headers");
                unauthorized = true;
            } else {
                String username = JwtUtility.extractUsername(token);
                if (username == null || !handleAuthentication(wrappedRequest, token, username)) {
                    log.warn("Invalid JWT token or missing username");
                    unauthorized = true;
                }
            }
        } catch (Exception e) {
            log.error("Error during JWT processing", e);
            unauthorized = true;
        }

        if (unauthorized) {
            readRequestBody(wrappedRequest);
            long duration = System.currentTimeMillis() - startTime;
            createRequestLog(wrappedRequest, wrappedResponse, HttpServletResponse.SC_UNAUTHORIZED, duration);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            wrappedResponse.copyBodyToResponse();
            return;
        }

        filterChain.doFilter(wrappedRequest, wrappedResponse);
        wrappedResponse.copyBodyToResponse();
    }

    private void createRequestLog(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, int responseStatus, long duration) {
        String requestBody = getCachedBody(request.getContentAsByteArray());
        String responseBody = getCachedBody(response.getContentAsByteArray());

        CreateRequestLogRequest createRequestLogRequest = CreateRequestLogRequest.builder()
                .requestUrl(request.getRequestURI())
                .requestMethod(request.getMethod())
                .requestIp(request.getRemoteAddr())
                .requestPayload(requestBody)
                .responsePayload(responseBody)
                .responseStatus(responseStatus)
                .responseTime(duration)
                .build();

        requestLogService.createRequestLog(createRequestLogRequest);
    }

    private void readRequestBody(ContentCachingRequestWrapper request) throws IOException {
        if (request.getContentLength() > 0) {
            request.getReader().lines().forEach(line -> {});
        }
    }

    private String getCachedBody(byte[] content) {
        if (content == null || content.length == 0) {
            return StringUtility.EMPTY;
        }
        String body = new String(content, StandardCharsets.UTF_8);
        return body.replaceAll("\\s+", " ");
    }

    private boolean handleAuthentication(HttpServletRequest request, String token, String username) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (JwtUtility.validateToken(token, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                return true;
            }
            log.warn("Invalid JWT token for user: {}", username);
        }
        return false;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return authenticationProperties.getWhitelistedEndpoints().stream().anyMatch(requestURI::contains);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtility.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(StringUtility.BEARER_PREFIX)) {
            return authorizationHeader.substring(StringUtility.BEARER_PREFIX.length());
        }
        return null;
    }
}