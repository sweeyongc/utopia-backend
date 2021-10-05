package com.project.utopia;

import com.project.utopia.entity.Authorities;
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
                .disable(); // 先不处理跨站点请求伪造的问题（Cross-site request forgery）

        http
                .formLogin()
                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    System.out.println(authentication.getName() + "logged in.");
                    System.out.println(authentication.getAuthorities().toString());

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

        // antMatchers是帮我们设置不同页面的权限的。
        http
                .authorizeRequests() // 指明权限，*是任意字符；**是可以匹配/a/b这种多个level的。
                .antMatchers("/setRequestStatus/**").hasAnyAuthority( "ROLE_ADMIN")
                .antMatchers("/allRequests/**").hasAnyAuthority( "ROLE_ADMIN")
                .antMatchers("/announcements/new-announcement/**").hasAnyAuthority( "ROLE_ADMIN")
                .antMatchers("/announcements/deleteAnnouncement/**").hasAnyAuthority( "ROLE_ADMIN")
                .antMatchers("/announcements/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/profile/**").hasAnyAuthority("ROLE_USER")
                .anyRequest().permitAll();
        // 而且权限不够的时候，会自动redirect to login

        http   // logout不用写，用默认的就好了。
                .logout()
                .logoutUrl("/logout");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth. // 这是一个测试用的账号。
                inMemoryAuthentication().withUser("admin")
                .password("123").authorities("ROLE_ADMIN");

        auth
                .jdbcAuthentication()
                .dataSource(dataSource) //上面autowired进来
                .usersByUsernameQuery("SELECT emailId, password, enabled FROM users WHERE emailId=?")
                .authoritiesByUsernameQuery("SELECT emailId, authorities FROM authorities WHERE emailId=?");
// 这两行只能是raw SQL，留下问号”?”可以让框架帮我们输入用户的输入值。
    }

    @SuppressWarnings("deprecation") // spring5必须要加密，我们没有encode。所以直接返回就行了。
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance(); // 所以直接返回就行了。
    }

    // 响应OPTION request
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 注意如果允许的是*，那么credentials会出问题。
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setMaxAge(Duration.ofHours(1));
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}