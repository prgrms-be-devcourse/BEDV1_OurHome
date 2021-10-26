package com.armand.ourhome.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EntityScan(basePackageClasses = OurHomeDomainConfig.class)
@EnableJpaRepositories(basePackageClasses = OurHomeDomainConfig.class)
@ComponentScan(basePackageClasses = OurHomeDomainConfig.class)
@Configuration
public class OurHomeDomainConfig {
}
