package Shopping.BackendCart.repository;

import Shopping.BackendCart.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Person Repository - data access for Person entity
 */
@Repository
public interface IPersonRepository extends JpaRepository<Person, Integer> {
    
    Optional<Person> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
