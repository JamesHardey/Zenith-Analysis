package com.jcoding.zenithanalysis.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class ZenithSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserDetailsService userDetailService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(encoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.authorizeRequests()
               .antMatchers("/login", "/register",
                       "/", "/contact-us", "/about","/events","/events/**",
                       "/courses", "/verify", "/verify/**","/error",
                       "/resend/**", "/h2-console/**","/sendMessage",
                       "/change-pass/**","/forget-pass","/upload/**"
               ).permitAll()
               .antMatchers("/home/**")
               .hasAuthority("USER")
               .antMatchers("/admin/**")
               .hasAuthority("ADMIN")
               .anyRequest().authenticated()
               .and()
               .formLogin()
               .loginPage("/login")
               .loginProcessingUrl("/login")
               .failureUrl("/login?error")
               .defaultSuccessUrl("/process_login")
               .usernameParameter("email")
               .permitAll()
               .and()
               .rememberMe().key("AbcdEfghIjklmNopQrsTuvXyz_0123456789")
               .and()
               .logout().logoutUrl("/logout")
               .logoutSuccessUrl("/login?logout")
               .permitAll();
       http.csrf().disable();
       http.headers().frameOptions().sameOrigin();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**", "/css/**",
                "/js/**", "/webjars/**",
                "/h2-console/**","/upload/**");
    }




}
