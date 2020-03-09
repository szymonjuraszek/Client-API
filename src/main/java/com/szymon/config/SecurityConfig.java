package com.szymon.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableOAuth2Sso
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
//                .httpBasic().and().authorizeRequests()
//                .antMatchers(HttpMethod.GET).permitAll()
//                .antMatchers(HttpMethod.POST).hasRole("ADMIN")
//                .and()
//                .formLogin().permitAll()
//                .and()
//                .logout().permitAll();

//        http
//                .httpBasic().and().authorizeRequests()
//                .antMatchers(HttpMethod.GET).permitAll()
//                .antMatchers(HttpMethod.POST).hasRole("ADMIN")
//                .and()
//                .formLogin().permitAll()
//                .and()
//                .logout().permitAll()
//                .and().csrf().disable();

        http.authorizeRequests().antMatchers(HttpMethod.GET).authenticated()
                .and()
                .formLogin();
    }
}
