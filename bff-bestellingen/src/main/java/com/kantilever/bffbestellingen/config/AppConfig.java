package com.kantilever.bffbestellingen.config;

import com.gazellig.amqpservice.amqp.config.AmqpConfig;
import com.kantilever.bffbestellingen.advice.RestLogger;
import java.time.Clock;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AmqpConfig.class})
@EnableAspectJAutoProxy
public class AppConfig {
    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Europe/Paris"));
    }

    @Bean
    public RestLogger restLogger() {
        return new RestLogger();
    }

}
