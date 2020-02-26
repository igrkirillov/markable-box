package ru.x5.bo.markable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import ru.x5.bo.markable.config.MarkableSpringConfiguration;

@SpringBootConfiguration
@Import({MarkableSpringConfiguration.class, ReferenceConfiguration.class, SubdocsAndAddonsConfiguration.class,
    TestSpringConfiguration.class})
public class MarkableStandaloneApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(MarkableStandaloneApplication.class, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ServletWebServerFactory servletWebServerFactory(){
        return new JettyServletWebServerFactory();
    }
}

