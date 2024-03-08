package com.sqy.urms.app.configuration;

import com.sqy.urms.core.configuration.ServiceConfiguration;
import com.sqy.urms.persistence.configuration.PersistenceConfiguration;
import com.sqy.urms.web.configuration.SecurityConfiguration;
import com.sqy.urms.web.configuration.WebConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ServiceConfiguration.class, PersistenceConfiguration.class, WebConfiguration.class, SecurityConfiguration.class})
public class ApplicationConfiguration {
}
