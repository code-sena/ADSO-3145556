package Shopping.BackendCart.repository;

import Shopping.BackendCart.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Role Repository - data access for Role entity
 */
@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {
    
    Optional<Role> findByName(String name);
    
    boolean existsByName(String name);
}
