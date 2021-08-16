package com.doter.server.config;

import com.doter.common.security.filters.ImageAuthenticationFilter;
import com.doter.common.security.filters.PhoneAuthenticationFilter;
import com.doter.common.security.filters.TokenAuthenticationFilter;
import com.doter.common.security.filters.UsernameAuthenticationFilter;
import com.doter.common.security.handler.*;
import com.doter.common.security.manager.CodeManager;
import com.doter.server.security.providers.ImageAuthenticationProvider;
import com.doter.server.security.providers.PhoneAuthenticationProvider;
import com.doter.server.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @CLassName SecurityConfig
 * @Description TDD
 * @Author HTP-韩天鹏
 * @Date 2021/8/16 9:41
 * @Version 1.0
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private CodeManager codeManager;

    @Resource
    private UserDetailService userDetailService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private RestLogoutSuccessHandler restLogoutSuccessHandler;

    @Resource
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Resource
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Resource
    private RestAuthenticationFailureHandler restAuthenticationFailureHandler;

    @Resource
    private RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
        auth.authenticationProvider(phoneAuthenticationProvider());
        auth.authenticationProvider(imageAuthenticationProvider());
    }


    /**
     * 允许匿名访问所有接口 主要是 auth 接口
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .formLogin().loginProcessingUrl("login")
                .and().logout().logoutUrl("/oauth/logout").logoutSuccessHandler(restLogoutSuccessHandler);
        http.addFilterAfter(usernameAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(imageAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(phoneAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(HttpMethod.POST, "/oauth/login", "/oauth/login/**", "/oauth/refreshToken").permitAll()
                .antMatchers(HttpMethod.GET, "/oauth/code/**").permitAll()
                .anyRequest().authenticated();

        http.cors().and().csrf().disable().headers().frameOptions().disable().cacheControl();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler);

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public UsernameAuthenticationFilter usernameAuthenticationFilter() {
        UsernameAuthenticationFilter usernameAuthenticationFilter = new UsernameAuthenticationFilter("/oauth/login");
        try {
            usernameAuthenticationFilter.setAuthenticationManager(super.authenticationManager());
            usernameAuthenticationFilter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
            usernameAuthenticationFilter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usernameAuthenticationFilter;
    }

    @Bean
    public ImageAuthenticationFilter imageAuthenticationFilter() {
        ImageAuthenticationFilter imageAuthenticationFilter = new ImageAuthenticationFilter("/oauth/login/image");
        try {
            imageAuthenticationFilter.setAuthenticationManager(super.authenticationManager());
            imageAuthenticationFilter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
            imageAuthenticationFilter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageAuthenticationFilter;
    }

    @Bean
    public PhoneAuthenticationFilter phoneAuthenticationFilter() {
        PhoneAuthenticationFilter phoneAuthenticationFilter = new PhoneAuthenticationFilter("/oauth/login/phone");
        try {
            phoneAuthenticationFilter.setAuthenticationManager(super.authenticationManager());
            phoneAuthenticationFilter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
            phoneAuthenticationFilter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phoneAuthenticationFilter;
    }

    @Bean
    public ImageAuthenticationProvider imageAuthenticationProvider() {
        ImageAuthenticationProvider imageAuthenticationProvider = new ImageAuthenticationProvider();
        imageAuthenticationProvider.setCodeManager(codeManager);
        imageAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        imageAuthenticationProvider.setUserDetailService(userDetailService);
        return imageAuthenticationProvider;
    }

    @Bean
    public PhoneAuthenticationProvider phoneAuthenticationProvider() {
        PhoneAuthenticationProvider phoneAuthenticationProvider = new PhoneAuthenticationProvider();
        phoneAuthenticationProvider.setUserDetailService(userDetailService);
        phoneAuthenticationProvider.setCodeManager(codeManager);
        return phoneAuthenticationProvider;
    }

}
