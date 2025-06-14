// This package contains repository interfaces that handle data access (persistence) logic
package org.todo.todorails.repository;

// Importing necessary Spring Data JPA and Spring framework annotations
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.todo.todorails.model.User;

// Marks this interface as a Spring Data repository bean, making it discoverable for dependency injection
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /*
     * This interface extends JpaRepository, which gives us many useful methods like:
     * save(), findById(), findAll(), delete(), etc., without needing to write SQL.
     * Here, we use it to manage User entities where the primary key is of type Long.
     */

    // Custom query method to retrieve a user based on their username
    // Spring will automatically implement this based on method naming conventions
    User findByUsername(String username);

    // Another derived query method to find a user using their email
    User findByEmail(String email);

    /** 
     * Define a query to check if a user exists by username 
     * This method tells Spring to generate a query like:
     * SELECT COUNT(*) > 0 FROM users WHERE username = ?
     */
    boolean existsByUsername(String username);

    // Similar to the above, but checks whether a user with a given email exists
    boolean existsByEmail(String email);

    /*
     * These methods are automatically implemented by Spring Data JPA
     * as long as the method names follow the correct pattern.
     * No need to write SQL manually — Spring handles it under the hood.
     */
}
