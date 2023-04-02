package be.hogent.springbook.security.config;

import be.hogent.springbook.security.service.SecurityService;
import be.hogent.springbook.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf-> csrf.ignoringRequestMatchers("/h2-console/**"))
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().authenticated())
                .formLogin(withDefaults())
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(PasswordEncoder passwordEncoder, SecurityService securityService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(securityService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}
