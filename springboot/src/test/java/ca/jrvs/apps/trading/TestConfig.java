package ca.jrvs.apps.trading;

import org.springframework.boot.test.context.TestConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@TestConfiguration
@ComponentScan(basePackages = "ca.jrvs.apps.trading")
@PropertySource("classpath:application-test.properties")
public class TestConfig {
    private Logger logger = LoggerFactory.getLogger(TestConfig.class);


}

