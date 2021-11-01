package com.armand.ourhome;

import com.armand.ourhome.domain.OurHomeDomainConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@EnableAutoConfiguration
@SpringBootConfiguration
public class OurHomeApplicationTests extends OurHomeDomainConfig {

    @Test
    public void contextLoads() throws Exception {
    }

}
