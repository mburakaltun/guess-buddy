package com.mburakaltun.guessbuddy.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secretKey;
    private AccessToken accessToken = new AccessToken();
    private RefreshToken refreshToken = new RefreshToken();

    @Data
    public static class AccessToken {
        private int standardUserExpirationMinutes = 60;
        private int adminExpirationMinutes = 1440;
    }

    @Data
    public static class RefreshToken {
        private int expirationDays = 30;
    }
}