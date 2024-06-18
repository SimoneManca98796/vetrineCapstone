package simonemanca.vetrineCapstone.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(http -> http.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; script-src 'self' https://js.stripe.com; frame-src 'self' https://js.stripe.com; connect-src 'self' https://api.stripe.com https://errors.stripe.com https://r.stripe.com https://ppm.stripe.com https://merchant-ui-api.stripe.com https://vetrine-agricole-6d661b03a449.herokuapp.com; img-src 'self' data: https://*.stripe.com; style-src 'self' 'unsafe-inline'; font-src 'self' https://fonts.gstatic.com;")
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/test").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/static/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/prezzi/**").permitAll() // prezzi latte
                        .requestMatchers("/api/aziende/**").permitAll() // annunci
                        .requestMatchers("/api/prezziOvini/**").permitAll()
                        .requestMatchers("/api/prezziSuini/**").permitAll()
                        .requestMatchers("/api/prezziBovini/**").permitAll()
                        .requestMatchers("/api/products/**").permitAll() // prodotti
                        .requestMatchers("/api/categories/**").permitAll() // categorie dei prodotti
                        .requestMatchers("/api/payment/**").permitAll() // pagamenti
                        .requestMatchers("/api/**").permitAll() // notifiche
                        .requestMatchers("/api/verify-captcha/**").permitAll() // captcha
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated());

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:5173",
                "https://vetrine-agricole-6d661b03a449.herokuapp.com",
                "https://vetrine-capstone.vercel.app"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


