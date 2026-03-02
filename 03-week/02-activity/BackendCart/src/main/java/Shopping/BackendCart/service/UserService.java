package Shopping.BackendCart.service;

import Shopping.BackendCart.dto.UserDTO;
import Shopping.BackendCart.entity.User;
import Shopping.BackendCart.entity.Person;
import Shopping.BackendCart.repository.IUserRepository;
import Shopping.BackendCart.repository.IPersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User Service Implementation - implements IUserService
 */
@Service
@Transactional
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IPersonRepository personRepository;

    public UserService(IUserRepository userRepository, IPersonRepository personRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findById(Integer id) {
        return userRepository.findById(id)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToDTO);
    }

    @Override
    public UserDTO save(UserDTO dto) {
        User user = mapToEntity(dto);
        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    @Override
    public UserDTO update(Integer id, UserDTO dto) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setEmail(dto.getEmail());
                    existingUser.setPasswordHash(dto.getPassword());
                    existingUser.setIsActive(dto.getIsActive());
                    User updatedUser = userRepository.save(existingUser);
                    return mapToDTO(updatedUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }

    // Mappers
    private User mapToEntity(UserDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(dto.getPassword());
        user.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        
        // Set the Person relationship if personId is provided
        if (dto.getPersonId() != null) {
            Person person = personRepository.findById(dto.getPersonId())
                    .orElseThrow(() -> new RuntimeException("Person not found with id: " + dto.getPersonId()));
            user.setPerson(person);
        }
        
        return user;
    }

    private UserDTO mapToDTO(User user) {
        Integer personId = user.getPerson() != null ? user.getPerson().getPersonId() : null;
        
        return new UserDTO(
                personId,
                user.getEmail(),
                user.getPasswordHash(),
                user.getIsActive()
        );
    }
}
