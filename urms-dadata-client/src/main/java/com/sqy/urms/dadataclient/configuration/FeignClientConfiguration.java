package com.sqy.urms.dadataclient.configuration;


import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"com.sqy.urms.dadataclient"})
public class FeignClientConfiguration {

}
