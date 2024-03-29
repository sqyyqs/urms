package com.sqy.urms.app;

import com.sqy.urms.app.configuration.ApplicationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@Import(ApplicationConfiguration.class)
public class UrmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrmsApplication.class, args);
    }
}
