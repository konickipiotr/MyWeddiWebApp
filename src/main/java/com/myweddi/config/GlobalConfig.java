package com.myweddi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;

@Configuration
public class GlobalConfig {

    public static ZoneId zid = ZoneId.of("Europe/Warsaw");
    public static final String domain = "http://localhost:8080";

    public static long NORMAL_ACCOUNT_SPACE = 250_000_000;
    public static long PREMIUM_ACCOUNT_SPACE = 500_000_000;

    @Bean
    public RestTemplate getRestTemplate(){
        return  new RestTemplate();
    }
}
