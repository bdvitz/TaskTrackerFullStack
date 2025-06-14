// This package declaration indicates that this class belongs to the 'config' folder of our application.
// It's generally used to organize our classes logically by responsibility.
package org.todo.todorails.config;

// These are import statements — they bring in the classes we need from Spring and our own project.
// Think of them as "toolkits" we're making available for use in this file.
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.todo.todorails.service.UserService;
// import org.springframework.security.core.userdetails.UserDetailsService;

// This annotation marks this class as a configuration class for Spring.
// It’s basically saying “this class will define some beans (objects managed by Spring)”.
// @SuppressWarnings("unused")
@Configuration
@EnableWebSecurity // Enables Spring Security for the application. Very important!
public class SecurityConfig {

    // Here we declare a final field to hold a reference to our custom UserService.
    // This service will be responsible for loading user-specific data (like passwords, roles, etc.)
    private final UserService userService;

    // This is a constructor — it's how we "inject" the UserService dependency into this class.
    // This technique is part of what's called Dependency Injection — very common in Spring.
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    // This method defines a bean for the security filter chain.
    // The SecurityFilterChain defines how HTTP security should behave.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disabling CSRF protection here. CSRF is important, but sometimes disabled for APIs or local testing.
            .csrf(csrf -> csrf.disable()) // ✅ Replaces older .csrf().disable() with a lambda for clarity

            // Now we define which HTTP requests are allowed without authentication.
            .authorizeHttpRequests(authorize -> authorize
                    /** 
                     * Allow users to access CSS files and the registration page
                     * even if they haven't logged in yet.
                     */
                    .requestMatchers("/css/**", "/register").permitAll()

                    // Same for JavaScript files and images — these are static assets
                    .requestMatchers("/js/**", "/images/**").permitAll()

                    // Let anyone access the homepage, login, terms, and a custom error page
                    .requestMatchers("/", "/login", "/terms", "/custom-error").permitAll()

                    // Any other request not explicitly permitted above must be authenticated
                    .anyRequest().authenticated()
            )

            // Configures the login page
            .formLogin(form -> form
                    .loginPage("/login") // Set a custom login page URL
                    .defaultSuccessUrl("/dashboard") // Where to go after successful login
                    .failureUrl("/login?error") // Where to go after a failed login
                    .permitAll() // Allow everyone to access the login page
            )

            // Enables logout functionality for all users
            .logout(logout -> logout.permitAll());

        // Finally, we return the configured security chain
        return http.build();
    }

    // Here, we define a bean for password encoding.
    // BCrypt is a strong hashing algorithm that safely stores passwords.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // This bean sets up an AuthenticationManager.
    // It tells Spring Security how to authenticate users: use our UserService and the password encoder.
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // Get the AuthenticationManagerBuilder from the current HttpSecurity context
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);

        // Set up the builder to use our user details service and password encoder
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());

        // Build and return the authentication manager
        return auth.build();
    }
}
