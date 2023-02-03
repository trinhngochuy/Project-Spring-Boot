package com.example.project_sem_4.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final String ADMIN = "ADMIN";
    private final String RECEPTIONISTS = "RECEPTIONISTS";
    private final String CUSTOMER_CARE = "CUSTOMER_CARE";
    private final String STAFF = "STAFF";
    private final String CUSTOMER = "CUSTOMER";
    @Bean
    public ApiAuthorizationFilter apiAuthorizationFilter(){
        return new ApiAuthorizationFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //override default login path
        ApiAuthenticationFilter apiAuthenticationFilter = new ApiAuthenticationFilter(authenticationManagerBean(),getApplicationContext());
        apiAuthenticationFilter.setFilterProcessesUrl("/login");
        http.cors().and().csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/registerCustomer",
                "/login**", "/token/refresh**",
                "/uploadfile**","/test**",
                "/cloud/**",
                "/mail**","/account/active/**",
                "/order/create",
                "/order/createOrderDetail",
                "/service/search",
                "/service/findAllTypeService",
                "/service/{id}",
                "/type_service/search",
                "/type_service/{id}",
                "/account/{id}",
                "/branch/{id}", "/branch/search",
                "/booking/search",
                "/booking/findAllByEmployee_idAndDate_booking",
                "/booking/{id}",
                "/blog/search", "/blog/{id}",
                        "/feedback/create**")
                .permitAll();
        //ADMIN
        http.authorizeRequests().antMatchers("/account**").hasAnyAuthority(ADMIN);
        http.authorizeRequests().antMatchers("/register").hasAnyAuthority(ADMIN);
        http.authorizeRequests().antMatchers("/blog**").hasAnyAuthority(ADMIN);
        http.authorizeRequests().antMatchers("/booking**").hasAnyAuthority(ADMIN);
        http.authorizeRequests().antMatchers("/branch**").hasAnyAuthority(ADMIN);
        http.authorizeRequests().antMatchers("/feedback**").hasAnyAuthority(ADMIN);
        http.authorizeRequests().antMatchers("/order**").hasAnyAuthority(ADMIN);
        http.authorizeRequests().antMatchers("/service**").hasAnyAuthority(ADMIN);
        http.authorizeRequests().antMatchers("/type_service**").hasAnyAuthority(ADMIN);
        http.authorizeRequests().antMatchers("/voucher**").hasAnyAuthority(ADMIN);
        // ACCOUNT
//        http.authorizeRequests().antMatchers("/account/{id}").hasAnyAuthority(ADMIN,RECEPTIONISTS, CUSTOMER_CARE, STAFF, CUSTOMER);
        http.authorizeRequests().antMatchers("account/update/{id}").hasAnyAuthority(ADMIN,RECEPTIONISTS, CUSTOMER_CARE, STAFF, CUSTOMER);
        http.authorizeRequests().antMatchers("/account/search").hasAnyAuthority(ADMIN,RECEPTIONISTS, CUSTOMER_CARE);
        // BLOG
        http.authorizeRequests().antMatchers("blog/create").hasAnyAuthority(ADMIN,CUSTOMER_CARE);
        http.authorizeRequests().antMatchers("blog/update/{id}").hasAnyAuthority(ADMIN,CUSTOMER_CARE);
        // BOOKING
        // BRANCH
        // FEEDBACK
        http.authorizeRequests().antMatchers("/feedback/{id}").hasAnyAuthority(ADMIN,CUSTOMER_CARE, CUSTOMER);
        http.authorizeRequests().antMatchers("/feedback/search").hasAnyAuthority(ADMIN,CUSTOMER_CARE, CUSTOMER);
        http.authorizeRequests().antMatchers("/feedback/changeStatus/{id}").hasAnyAuthority(ADMIN,CUSTOMER_CARE);
        // ORDER
//        http.authorizeRequests().antMatchers("/order/create").hasAnyAuthority(ADMIN,CUSTOMER);
//        http.authorizeRequests().antMatchers("/order/createOrderDetail").hasAnyAuthority(ADMIN,CUSTOMER);
        http.authorizeRequests().antMatchers("/order/{id}").hasAnyAuthority(ADMIN,RECEPTIONISTS, CUSTOMER);
        http.authorizeRequests().antMatchers("/order/search").hasAnyAuthority(ADMIN,RECEPTIONISTS, CUSTOMER);
        http.authorizeRequests().antMatchers("/order/update/status/{id}").hasAnyAuthority(ADMIN,RECEPTIONISTS);
        // SERVICE
        // TYPE_SERVICE
        // VOUCHER
        http.authorizeRequests().antMatchers("/voucher/{voucherCode}").hasAnyAuthority(ADMIN,CUSTOMER);
        http.authorizeRequests().antMatchers("/voucher/search").hasAnyAuthority(ADMIN,CUSTOMER);

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(apiAuthenticationFilter);
        http.addFilterBefore(apiAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}