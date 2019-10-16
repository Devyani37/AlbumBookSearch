package com.devyani.kramphub.BookAlbumSearch;

import com.devyani.kramphub.BookAlbumSearch.configurations.ApiConfig;
import com.devyani.kramphub.BookAlbumSearch.exceptions.RestTemplateResponseErrorHandler;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootApplication
public class BookAlbumSearchApplication {

    @Autowired
    private ApiConfig apiConfig;

    public static void main(String[] args) {
        SpringApplication.run(BookAlbumSearchApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(apiConfig.getConnectTimeout()))
                .setReadTimeout(Duration.ofMillis(apiConfig.getReadTimeout()))
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

}
