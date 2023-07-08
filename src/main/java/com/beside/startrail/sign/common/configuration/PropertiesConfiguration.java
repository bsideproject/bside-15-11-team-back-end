package com.beside.startrail.sign.common.configuration;

import com.beside.startrail.sign.kakao.model.KakaoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({KakaoProperties.class})
@Configuration
public class PropertiesConfiguration {
}
