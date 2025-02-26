package ir.maktabsharif.OnlineExamManagementProject.security;

import ir.maktabsharif.OnlineExamManagementProject.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private CustomUserDetailService userDetailsService;

    public SecurityConfig(CustomUserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/register").permitAll()
                        .requestMatchers("/users/all").hasAuthority("VIEW_USER_LIST")
                        .requestMatchers("users/approve/").hasAuthority("APPROVE_USER")
                        .requestMatchers("/courses/**").hasAnyRole("ADMIN", "TEACHER")
                        .anyRequest().authenticated())

                .httpBasic(withDefaults())
                .formLogin(withDefaults());


        //http.authorizeHttpRequests(auth -> auth.requestMatchers("/**")
        //.hasRole("ADMIN")
        //.hasAuthority("VIEW_PRIVILEGE")
        //                      .permitAll()
        //                    .anyRequest()
        //.authenticated()
        //  )
        //.formLogin(withDefaults());
        return http.build();
    }


    @Bean
    WebSecurityCustomizer ignoringCustomizer() {
        return (web) ->
                web
                        .ignoring()
                        .requestMatchers("/h2-console/**");
    }


}
