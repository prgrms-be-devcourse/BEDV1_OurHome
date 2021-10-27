package com.armand.ourhome.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackageClasses = {OurHomeMarketApplication.class})
@EntityScan(basePackageClasses = {OurHomeMarketApplication.class})
@SpringBootApplication(scanBasePackages = "com.armand.ourhome",
        excludeName = "com.armand.ourhome.community")
public class OurHomeMarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(OurHomeMarketApplication.class, args);
    }
}
