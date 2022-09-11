package com.jcoding.zenithanalysis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;


@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        String path = System.getProperty("user.dir")+"/upload";
        String location = Paths.get(path).toUri().toString();
        registry
                .addResourceHandler("/upload/**")
                .addResourceLocations(location);
    }
}
