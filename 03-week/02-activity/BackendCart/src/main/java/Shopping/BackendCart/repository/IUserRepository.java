package Shopping.BackendCart.repository;

import Shopping.BackendCart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * User Repository - data access for User entity
 */
@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u JOIN FETCH u.person WHERE u.email = :email")
    Optional<User> findByEmailWithPerson(String email);
}
