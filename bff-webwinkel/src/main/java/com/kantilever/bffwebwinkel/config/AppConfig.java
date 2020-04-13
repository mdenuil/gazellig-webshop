package com.kantilever.bffwebwinkel.config;

import com.gazellig.amqpservice.amqp.config.AmqpConfig;
import com.kantilever.bffwebwinkel.advice.RestLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AmqpConfig.class})
@EnableAspectJAutoProxy
public class AppConfig {
    @Bean
    public RestLogger restLogger() {
        return new RestLogger();
    }
}
