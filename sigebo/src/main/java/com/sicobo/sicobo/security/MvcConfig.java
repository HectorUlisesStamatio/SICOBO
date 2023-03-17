package com.sicobo.sicobo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/sitios").setViewName("adminViews/listSites");
        registry.addViewController("/registro").setViewName("adminViews/registerSite");
        registry.addViewController("/modificacion").setViewName("adminViews/updateSite");
    }
}
