package com.project.utopia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors() // all CORS request
                .and()
                .csrf()
                .disable();

        http
                .formLogin()
                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    System.out.println(authentication.getName() + "logged in.");

                    httpServletResponse.setContentType("application/json");
                    httpServletResponse.setCharacterEncoding("UTF-8");
                    httpServletResponse.setStatus(HttpStatus.OK.value());

                    // Return a JSON like {authority: "ROLE_ADMIN"} or {authority: "ROLE_USER"}
                    GrantedAuthority authority = new ArrayList<>(authentication.getAuthorities()).get(0);
                    httpServletResponse.getWriter().write(String.format("{\"authority\": \"%s\"}", authority));
                })
                .failureHandler((httpServletRequest, httpServletResponse, e) -> {
                    httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value()); // if fail to login, return the bad request status code
                });

        http
                .authorizeRequests()
                .antMatchers("/new-announcement/**").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/deleteAnnouncement/**").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/updateAnnouncement/**").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/allRequests/**").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/setRequestStatus/**").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/profile/**").hasAnyAuthority("ROLE_USER")
                .antMatchers("/deleteRequest/**").hasAnyAuthority("ROLE_USER")
                .antMatchers("/newRequest/**").hasAnyAuthority("ROLE_USER")
                .antMatchers("/currentRequests/**").hasAnyAuthority("ROLE_USER")
//                .antMatchers("/announcements/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .anyRequest().permitAll();

        http
                .logout()
                .logoutUrl("/logout");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT emailId, password, enabled FROM users WHERE emailId=?")
                .authoritiesByUsernameQuery("SELECT emailId, authorities FROM authorities WHERE emailId=?");
    }

    @SuppressWarnings("deprecation")
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setMaxAge(Duration.ofHours(1));
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}