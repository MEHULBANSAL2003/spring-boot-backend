    package com.springbootBackend.backend.config;


    import com.springbootBackend.backend.exceptions.CustomAuthEntryPoint;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;

    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.web.SecurityFilterChain;

    import static org.springframework.security.config.Customizer.withDefaults;

    @Configuration
    public class SecurityConfig {

      private final JwtAuthenticationFilter jwtAuthenticationFilter;
      private final CustomAuthEntryPoint customAuthEntryPoint;

      public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                            CustomAuthEntryPoint customAuthEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAuthEntryPoint = customAuthEntryPoint;
      }

        @Bean
        public PasswordEncoder  passwordEncoder(){
            return new BCryptPasswordEncoder(12);
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/auth/**").permitAll()
                            .anyRequest().authenticated()
                    )
              .exceptionHandling(ex -> ex
                .authenticationEntryPoint(customAuthEntryPoint) // 👈 force 401 here
              )
              .addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
              .httpBasic(httpBasic -> httpBasic.disable());     // disable HTTP Basic

          return http.build();
        }
    }
