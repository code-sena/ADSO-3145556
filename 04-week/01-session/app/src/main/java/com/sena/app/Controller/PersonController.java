package com.sena.app.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.app.Entity.Person;
import com.sena.app.Service.PersonService;

@RestController
@RequestMapping("/api/person")
public class PersonController {
    
    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Person> create(@RequestBody Person person) {
        Person savedPerson = service.save(person);
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Person>> All() {       
        List<Person> people = service.All();
        return new ResponseEntity<>(people, HttpStatus.OK);
    }


}
