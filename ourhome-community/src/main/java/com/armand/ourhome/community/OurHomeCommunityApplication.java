package com.armand.ourhome.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@EnableJpaRepositories(basePackageClasses = OurHomeCommunityApplication.class)
@EntityScan(basePackageClasses = OurHomeCommunityApplication.class)
@SpringBootApplication(
        scanBasePackages = "com.armand.ourhome",
        excludeName = "com.armand.ourhome.community"
)
public class OurHomeCommunityApplication {
    public static void main(String[] args) {
        SpringApplication.run(OurHomeCommunityApplication.class, args);
    }
}
