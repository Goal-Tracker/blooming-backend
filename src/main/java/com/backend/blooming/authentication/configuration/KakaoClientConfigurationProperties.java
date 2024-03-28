package com.backend.blooming.authentication.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("oauth.client.kakao")
public record KakaoClientConfigurationProperties(String userInfoUri, String userUnlinkUri, String adminKey) {
}
