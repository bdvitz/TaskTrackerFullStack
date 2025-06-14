// The service class responsible for handling business logic related to users
package org.todo.todorails.service;

// Spring annotations and security components
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

// Domain and repository layer classes
import org.todo.todorails.model.User;
import org.todo.todorails.repository.UserRepository;

// Mark this class as a Spring service, making it available for component scanning
@Service
public class UserServiceAnnotated implements UserDetailsService {

    // Declare dependencies: UserRepository for DB access, and encoder for securing passwords
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructor-based dependency injection for UserRepository
    @Autowired
    public UserServiceAnnotated(UserRepository userRepository) {
        this.userRepository = userRepository;

        // Instantiating password encoder for hashing passwords securely
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // This method handles the registration of a new user
    public User registerUser(User user) throws Exception {

        // ✅ Step 1: Check if a user with the same username already exists
        // This prevents duplicate accounts with the same username
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new Exception("Username already exists");
        }

        // ✅ Step 2: Secure the user’s password using BCrypt hashing before storing
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // ✅ Step 3: Mark that the user has accepted the terms (assumed required for registration)
        user.setTermsAccepted(true);

        // ✅ Step 4: Save the user in the database and return the saved user
        return userRepository.save(user);
    }

    // Utility method to find a user by username (used elsewhere in app)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Another helper method to retrieve a user based on email (useful for login/lookup)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Allows saving or updating a user instance in the database
    public void save(User user) {
        userRepository.save(user);
    }

    // Main method required by Spring Security to load user-specific data during login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try finding the user by username first
        User user = findByUsername(username);

        // If no user is found with that username, try checking if it's an email instead
        if (user == null) {
            user = findByEmail(username);

            // If no user found by either username or email, throw an error for Spring Security
            if(user == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
        }

        // Return the User instance, which must implement UserDetails
        return user;
    }
}
