package Shopping.BackendCart.service;

import Shopping.BackendCart.dto.PersonDTO;
import java.util.List;
import java.util.Optional;

/**
 * Person Service Interface - defines business operations for Person
 */
public interface IPersonService {
    
    List<PersonDTO> findAll();
    
    Optional<PersonDTO> findById(Integer id);
    
    Optional<PersonDTO> findByEmail(String email);
    
    PersonDTO save(PersonDTO dto);
    
    PersonDTO update(Integer id, PersonDTO dto);
    
    void deleteById(Integer id);
    
    boolean existsByEmail(String email);
}
