package Shopping.BackendCart.service;

import Shopping.BackendCart.dto.UserDTO;
import java.util.List;
import java.util.Optional;

/**
 * User Service Interface - defines business operations for User
 */
public interface IUserService {
    
    List<UserDTO> findAll();
    
    Optional<UserDTO> findById(Integer id);
    
    Optional<UserDTO> findByEmail(String email);
    
    UserDTO save(UserDTO dto);
    
    UserDTO update(Integer id, UserDTO dto);
    
    void deleteById(Integer id);
    
    boolean existsByEmail(String email);
    
    boolean existsById(Integer id);
}
