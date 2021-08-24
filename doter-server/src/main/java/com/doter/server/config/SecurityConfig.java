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

    //认证处理器
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //创建认证管理器
        //UserDetailService 认证
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
        //phone 认证提供者处理器
        auth.authenticationProvider(phoneAuthenticationProvider());
        //image 认证提供者处理器
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
        //每一次过滤都要加上用户名密码过滤
//        1.过滤器之前 进行token认证和密码认证"
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        //2.添加过滤器之后 密码认证
        http.addFilterAfter(usernameAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                //图片认证
                .addFilterAfter(imageAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                //手机验证码认证
                .addFilterAfter(phoneAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.httpBasic().disable()
                .formLogin().disable()
                //退出
                .logout().logoutUrl("/oauth/logout").logoutSuccessHandler(restLogoutSuccessHandler);


        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(HttpMethod.POST, "/oauth/login", "/oauth/login/**", "/oauth/refreshToken").permitAll()
                .antMatchers(HttpMethod.GET, "/oauth/code/**").permitAll()
                .anyRequest().authenticated();

        http.cors().and().csrf().disable().headers().frameOptions().disable().cacheControl();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //例外的处理器
        http.exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler);

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
            //认证成功处理器
            usernameAuthenticationFilter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
            //认证失败处理器
            usernameAuthenticationFilter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usernameAuthenticationFilter;
    }

    @Bean
    public ImageAuthenticationFilter imageAuthenticationFilter() {
        //调用构造方法
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
        //构造函数
        ImageAuthenticationProvider imageAuthenticationProvider = new ImageAuthenticationProvider();
        //设置参数
        imageAuthenticationProvider.setCodeManager(codeManager);
        imageAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        imageAuthenticationProvider.setUserDetailService(userDetailService);
        return imageAuthenticationProvider;
    }

    @Bean
    public PhoneAuthenticationProvider phoneAuthenticationProvider() {
        //构造对象
        PhoneAuthenticationProvider phoneAuthenticationProvider = new PhoneAuthenticationProvider();
        //放入 数据库对象
        phoneAuthenticationProvider.setUserDetailService(userDetailService);
        //放出 验证码对象
        phoneAuthenticationProvider.setCodeManager(codeManager);
        return phoneAuthenticationProvider;
    }

}
