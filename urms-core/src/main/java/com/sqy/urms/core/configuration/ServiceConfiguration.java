package com.sqy.urms.core.configuration;

import com.sqy.urms.dadataclient.configuration.FeignClientConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"com.sqy.urms.core.service", "com.sqy.urms.core.mapper"})
@Import(FeignClientConfiguration.class)
public class ServiceConfiguration {
}
