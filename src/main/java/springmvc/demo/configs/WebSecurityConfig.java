package springmvc.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import springmvc.demo.hooks.JWTAuthenticationFilter;
import springmvc.demo.hooks.JWTLoginFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springmvc.demo.services.authentication.CurrentUserDetailService;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
@EnableWebMvc
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CurrentUserDetailService currentUserDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("the second 1");
        http
                .cors().and()
                .csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .and()
                .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/staffs").hasRole("MANAGER")
                .antMatchers(HttpMethod.GET, "/api/users").hasRole("MANAGER")
                .antMatchers(HttpMethod.POST, "/api/staffs/role/{id: ^[a-zA-Z0-9]*$}").hasRole("MANAGER")
                .antMatchers(HttpMethod.GET, "/api/staffs/{[a-zA-Z0-9]}").hasAnyRole("MANAGER", "STAFF")
                .antMatchers(HttpMethod.GET,"/api/reservations/{[a-zA-Z0-9]}").hasAnyRole("MANAGER","STAFF","CLIENT")
                .antMatchers(HttpMethod.POST,"/api/reservations").hasAnyRole("MANAGER","STAFF","CLIENT")
                .antMatchers(HttpMethod.PUT,"/api/reservations/cancel/{code: ^[a-zA-Z0-9]}").hasAnyRole("MANAGER","STAFF","CLIENT")
                .antMatchers(HttpMethod.PUT,"/api/reservations/check-in/{code: ^[a-zA-Z0-9]}").hasAnyRole("MANAGER","STAFF")
                .antMatchers(HttpMethod.PUT,"/api/reservations/check-out/{code: ^[a-zA-Z0-9]}").hasAnyRole("MANAGER","STAFF")
                .antMatchers(HttpMethod.GET,"/api/reservations/admin/{\\d{4}-\\d{2}-\\d{2}}/{\\d{4}-\\d{2}-\\d{2}}").hasRole("MANAGER")
                .and()
                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity webSecurity) {

        webSecurity.ignoring().antMatchers(HttpMethod.POST, "/api/users")
                .and().ignoring().antMatchers(HttpMethod.GET, "/api/rooms/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(currentUserDetailService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }
}

