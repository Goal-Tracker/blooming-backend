package com.backend.blooming.authentication.configuration;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan
@EnableConfigurationProperties({KakaoClientConfigurationProperties.class})
public class OAuthPropertiesConfiguration {
}
