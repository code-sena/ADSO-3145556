package Shopping.BackendCart.service;

import Shopping.BackendCart.dto.PersonDTO;
import Shopping.BackendCart.entity.Person;
import Shopping.BackendCart.repository.IPersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Person Service Implementation - implements IPersonService
 */
@Service
@Transactional
public class PersonService implements IPersonService {

    private final IPersonRepository personRepository;

    public PersonService(IPersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> findAll() {
        return personRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonDTO> findById(Integer id) {
        return personRepository.findById(id)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonDTO> findByEmail(String email) {
        return personRepository.findByEmail(email)
                .map(this::mapToDTO);
    }

    @Override
    public PersonDTO save(PersonDTO dto) {
        Person person = mapToEntity(dto);
        Person savedPerson = personRepository.save(person);
        return mapToDTO(savedPerson);
    }

    @Override
    public PersonDTO update(Integer id, PersonDTO dto) {
        return personRepository.findById(id)
                .map(existingPerson -> {
                    existingPerson.setFirstName(dto.getFirstName());
                    existingPerson.setLastName(dto.getLastName());
                    existingPerson.setEmail(dto.getEmail());
                    existingPerson.setPhone(dto.getPhone());
                    existingPerson.setAddress(dto.getAddress());
                    Person updatedPerson = personRepository.save(existingPerson);
                    return mapToDTO(updatedPerson);
                })
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
    }

    @Override
    public void deleteById(Integer id) {
        personRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return personRepository.existsByEmail(email);
    }

    // Mappers
    private Person mapToEntity(PersonDTO dto) {
        Person person = new Person();
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setEmail(dto.getEmail());
        person.setPhone(dto.getPhone());
        person.setAddress(dto.getAddress());
        return person;
    }

    private PersonDTO mapToDTO(Person person) {
        return new PersonDTO(
                person.getFirstName(),
                person.getLastName(),
                person.getEmail(),
                person.getPhone(),
                person.getAddress()
        );
    }
}
