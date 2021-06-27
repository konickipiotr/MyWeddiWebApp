package com.myweddi.config;

import com.myweddi.users.authentication.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import static org.springframework.security.crypto.password.NoOpPasswordEncoder.*;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final WeddingDetailsService weddingDetailsService;

    @Autowired
    public SecurityConfiguration(WeddingDetailsService weddingDetailsService) {
        this.weddingDetailsService = weddingDetailsService;
    }

    @Bean
    public PasswordEncoder encoder(){return new BCryptPasswordEncoder();}

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(weddingDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(encoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public DaoAuthenticationProvider apiAuthenticationProvider() {
        DaoAuthenticationProvider apiAuthenticationProvider = new DaoAuthenticationProvider();
        apiAuthenticationProvider.setUserDetailsService(weddingDetailsService);
        apiAuthenticationProvider.setPasswordEncoder(getInstance());
        return apiAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth
            .authenticationProvider(authenticationProvider())
            .authenticationProvider(apiAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .authorizeRequests()
                .antMatchers("/init/**").hasAnyRole(AccountType.ADMIN.name(),
                                                                        AccountType.PRIVATE.name(),
                                                                        AccountType.SERVICE.name())
                .antMatchers("/user/**").hasRole(AccountType.PRIVATE.name())
                .antMatchers("/service/**").hasRole(AccountType.SERVICE.name())
                .antMatchers("/admin/**").hasRole(AccountType.ADMIN.name())
                .antMatchers("/login", "/logout", "/css/**", "/img/**", "/registration/**", "/remindpassword").permitAll()
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login");

        http.csrf().disable()
                .authenticationProvider(apiAuthenticationProvider())

                .authorizeRequests()
                .antMatchers("/api/registration/**", "/api/forgotpassword/**").permitAll()
                .antMatchers("/api/user/**").hasRole(AccountType.PRIVATE.name())
                .antMatchers("/api/service/**").hasRole(AccountType.SERVICE.name())
                .and()
                .httpBasic();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui/",
                "/webjars/**",
                "/h2-console/**",
                "/css/**",
                "/img/**");
    }

}
