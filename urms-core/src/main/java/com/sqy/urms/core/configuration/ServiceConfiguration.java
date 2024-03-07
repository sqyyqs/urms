package com.sqy.urms.core.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.sqy.urms.core.service")
public class ServiceConfiguration {
}