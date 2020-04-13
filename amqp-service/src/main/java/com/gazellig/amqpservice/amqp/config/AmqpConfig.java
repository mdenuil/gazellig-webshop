package com.gazellig.amqpservice.amqp.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * AmqpConfig is required as an import in your own configuration. This Config
 * loads all components required by this library.
 */
@Configuration
@ComponentScan(basePackages = "com.gazellig.amqpservice")
public class AmqpConfig {

}
