package training.todo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

// at the application startup, spring security will try to look for a bean of type security filter chain...
// this is the bean that is responsible for configuring all the http security of our application...
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    // spring framework automatically calls this method for us and to do it we'll annotate it with
    // bean annotation...
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // by default, it(csrf) is enabled...
        // in stateless api, we don't need this therefore we'll disable this feature...
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
//                .requestMatchers("/api/auth/**")
//                .permitAll()
//                .requestMatchers("/api/**")
//                .authenticated()
                .requestMatchers("/**")
                .permitAll()
                .and()
                // this disables session creation on Spring Security...
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                .and()
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
