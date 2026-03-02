package Shopping.BackendCart.controller;

import Shopping.BackendCart.dto.PersonDTO;
import Shopping.BackendCart.service.IPersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Person Controller - REST API for Person operations
 */
@RestController
@RequestMapping("/api/v1/persons")
@Tag(name = "Persons", description = "Person management API")
public class PersonController {

    private final IPersonService personService;

    public PersonController(IPersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    @Operation(summary = "Get all persons", description = "Retrieve a list of all persons")
    public ResponseEntity<List<PersonDTO>> findAll() {
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get person by ID", description = "Retrieve a person by their ID")
    public ResponseEntity<PersonDTO> findById(@PathVariable Integer id) {
        return personService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get person by email", description = "Retrieve a person by their email")
    public ResponseEntity<PersonDTO> findByEmail(@PathVariable String email) {
        return personService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new person", description = "Create a new person record")
    public ResponseEntity<PersonDTO> create(@Valid @RequestBody PersonDTO dto) {
        PersonDTO savedPerson = personService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPerson);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a person", description = "Update an existing person record")
    public ResponseEntity<PersonDTO> update(@PathVariable Integer id, @Valid @RequestBody PersonDTO dto) {
        try {
            PersonDTO updatedPerson = personService.update(id, dto);
            return ResponseEntity.ok(updatedPerson);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a person", description = "Delete a person by their ID")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        personService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/email/{email}")
    @Operation(summary = "Check if email exists", description = "Check if an email is already registered")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(personService.existsByEmail(email));
    }
}
