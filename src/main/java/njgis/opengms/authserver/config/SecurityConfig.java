package njgis.opengms.authserver.config;

import njgis.opengms.authserver.service.LoginIpFilter;
import njgis.opengms.authserver.service.MongoUserDetailsService;
import njgis.opengms.authserver.util.Md5PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MongoUserDetailsService mongoUserDetailsService;
    @Autowired
    LoginIpFilter loginIpFilter;
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new Md5PasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(mongoUserDetailsService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**","/css/**","/images/**","/element-ui/**","/img/**");
    }
    @Autowired
    CorsConfigurationSource corsConfigurationSource;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(loginIpFilter, UsernamePasswordAuthenticationFilter.class);
        http
                .authorizeRequests()
                .antMatchers("/OauthClient/**","/user/**","/login","/register","/resetPwd").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").loginProcessingUrl("/in").failureUrl("/login?error=true")
                .usernameParameter("account").passwordParameter("password")
                .and()
                .cors()
                .configurationSource(corsConfigurationSource)
                .and()
                .csrf().disable();
        http.httpBasic().disable();
    }
}
