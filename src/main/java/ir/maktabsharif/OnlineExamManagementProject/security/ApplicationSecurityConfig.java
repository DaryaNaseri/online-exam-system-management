package ir.maktabsharif.OnlineExamManagementProject.security;

import ir.maktabsharif.OnlineExamManagementProject.service.Impl.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.concurrent.TimeUnit;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ApplicationSecurityConfig {

    private CustomUserDetailService userDetailsService;

    @Autowired
    public ApplicationSecurityConfig(CustomUserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/static/style.css", "users/auth/**").permitAll()
                        .requestMatchers("exams").hasAnyRole("ADMIN","TEACHER")
                        .requestMatchers("/users/filter/**").hasAuthority("FILTER_AND_SEARCH_USERS")
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
//                .formLogin(form -> form
//                        .loginPage("/login").permitAll()
//                        .defaultSuccessUrl("/courses", true)
//                )
                .rememberMe(remember -> remember.tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(7))
                        .key("somethingverysecured"))
                .logout(logout -> logout.logoutUrl("/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .logoutSuccessUrl("/login"));

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    WebSecurityCustomizer ignoringCustomizer() {
        return (web) ->
                web
                        .ignoring()
                        .requestMatchers("/h2-console/**");
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}