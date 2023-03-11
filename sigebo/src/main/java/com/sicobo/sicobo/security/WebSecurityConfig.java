package com.sicobo.sicobo.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{

        http.authorizeHttpRequests((request) ->{
            request.requestMatchers("/", "/home","/index").permitAll();
            request.requestMatchers("/css/**", "/js/**", "/img/**", "/scss/**", "/vendor/**").permitAll();

            //request.requestMatchers("/usuario/**").hasAnyRole("USUARIO");
            //request.requestMatchers("/gestor/**").hasAnyRole("GESTOR");
            //request.requestMatchers("/admin/**").hasAnyRole("ADMIN");
            //request.anyRequest().authenticated();

            request.anyRequest().permitAll();
        });


        http.formLogin((login) ->{
            login.loginPage("/login").permitAll();
        });

        http.logout((logout) ->{
                    logout.permitAll();
                }
        );


        return http.build();
    }

}
