package Shopping.BackendCart.service;

import Shopping.BackendCart.dto.RoleDTO;
import java.util.List;
import java.util.Optional;

/**
 * Role Service Interface - defines business operations for Role
 */
public interface IRoleService {
    
    List<RoleDTO> findAll();
    
    Optional<RoleDTO> findById(Integer id);
    
    Optional<RoleDTO> findByName(String name);
    
    RoleDTO save(RoleDTO dto);
    
    RoleDTO update(Integer id, RoleDTO dto);
    
    void deleteById(Integer id);
    
    boolean existsByName(String name);
}
