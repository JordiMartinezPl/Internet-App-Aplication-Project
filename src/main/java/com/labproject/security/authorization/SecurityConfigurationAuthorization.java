package com.labproject.security.authorization;

import com.labproject.security.authentication.AuthenticationController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfigurationAuthorization {
    private static final String[] WHITE_LIST_URL = {
            "/login",
            "/h2-console/**",
            "/webjars/**",
            "/v3/api-docs/**", //this is for swagger
            "/swagger-ui/**",
            "/swagger-ui.html"};
    private final JwtDecoder jwtDecoder;

    public SecurityConfigurationAuthorization(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationController authenticationController) throws Exception {

        return http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .authorizeHttpRequests(auth -> {

                    // ALBUM
                    auth.requestMatchers("api/albums/create/**").hasAuthority("ADMIN");
                    auth.requestMatchers("api/albums/delete").hasAuthority("ADMIN");
                    auth.requestMatchers("api/albums/*/publicAvailability").hasAuthority("ADMIN");
                    auth.requestMatchers("/api/albums/*/users").hasAuthority("ADMIN");
                    auth.requestMatchers("/api/albums/sticker/*/sale/**").hasAnyAuthority("PREMIUM", "ADMIN");
                    auth.requestMatchers("api/albums/saleSticker/*/buy").hasAnyAuthority("PREMIUM", "ADMIN");
                    auth.requestMatchers("/api/albums/*/gift").hasAnyAuthority("PREMIUM", "ADMIN");

                    // USER
                    auth.requestMatchers("api/user/all").hasAuthority("ADMIN");
                    auth.requestMatchers("api/user/lastUsersRegistered/**").hasAuthority("ADMIN");
                    auth.requestMatchers("api/user/allOrderedByActivity").hasAuthority("ADMIN");

                    // FORUM
                    auth.requestMatchers("api/forum/*/sendDirectMessage/**").hasAnyAuthority("ADMIN", "PREMIUM");
                    auth.requestMatchers("api/forum/message/*/replyDirectMessage/**").hasAnyAuthority("ADMIN", "PREMIUM");
                    auth.requestMatchers("api/forum/*/singleTimeMessage/**").hasAnyAuthority("ADMIN", "PREMIUM");
                    auth.requestMatchers("api/forum/publishMessage/**").hasAuthority("ADMIN");
                    auth.requestMatchers("/api/forum/allOrderedByMessages").hasAnyAuthority("ADMIN");


                    //the rest of the functions can be used by everyone
                    auth.anyRequest().permitAll();

                })
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer((oauth2) -> oauth2.jwt((jwt) -> jwt.decoder(jwtDecoder)))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

}
