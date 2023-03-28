package com.sicobo.sicobo.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    @Autowired
    private DataSource dataSource;
    @Bean
    public PasswordEncoder passWordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.jdbcAuthentication().dataSource(dataSource);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{

        http.authorizeHttpRequests(request ->{
            request.requestMatchers("/", "/home","/index","/register","/registrarUsuario").permitAll();
            request.requestMatchers("/css/**", "/js/**", "/img/**", "/scss/**", "/vendor/**").permitAll();
            request.requestMatchers("/usuario/**").hasRole("USUARIO");
            request.requestMatchers("/gestor/**").hasRole("GESTOR");
            request.requestMatchers("/admin/**").hasRole("ADMIN");
            request.anyRequest().authenticated();
        });


        http.formLogin(login ->{
            login.loginPage("/login").permitAll();
            login.defaultSuccessUrl("/");
        });

        http.logout(logout ->{
                    logout.permitAll();
                }
        );



        return http.build();
    }

}
