package com.mburakaltun.guessbuddy.authentication.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "authentication")
public class AuthenticationProperties {
    private List<String> whitelistedEndpoints;
    private String resetPasswordDeepLinkUrl;
}
