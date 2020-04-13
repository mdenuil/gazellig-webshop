package com.kantilever.dsklantbeheer.config;

import com.gazellig.amqpservice.amqp.config.AmqpConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AmqpConfig.class})
public class Config {
}

