package com.myweddi.config;

import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;

public class GlobalConfig {

    public static ZoneId zid = ZoneId.of("Europe/Warsaw");
}
