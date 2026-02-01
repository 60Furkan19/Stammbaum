package de.demirer.stammbaum.controller;

import de.demirer.stammbaum.model.Person;
import de.demirer.stammbaum.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PersonController {

    private final PersonService personService;

    // GET alle Personen
    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    // GET alle Wurzeln
    @GetMapping("/roots")
    public ResponseEntity<List<Person>> getAllRoots() {
        return ResponseEntity.ok(personService.getAllRoots());
    }

    // GET Stammbaum-Daten (für D3.js)
    @GetMapping("/tree")
    public ResponseEntity<List<Map<String, Object>>> getTreeData() {
        return ResponseEntity.ok(personService.getTreeData());
    }

    // GET Person mit Hierarchie
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        return ResponseEntity.ok(personService.getPersonById(id));
    }

    @GetMapping("/{id}/hierarchy")
    public ResponseEntity<Map<String, Object>> getPersonWithHierarchy(@PathVariable Long id) {
        return ResponseEntity.ok(personService.getPersonWithHierarchy(id));
    }

    // POST neue Person
    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person savedPerson = personService.savePerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPerson);
    }

    // PUT Person aktualisieren
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person person) {
        Person existingPerson = personService.getPersonById(id);

        existingPerson.setVorname(person.getVorname());
        existingPerson.setNachname(person.getNachname());
        existingPerson.setGeburtsdatum(person.getGeburtsdatum());
        existingPerson.setTodesdatum(person.getTodesdatum());

        // Eltern-Beziehungen nur wenn explizit gesetzt
        if (person.getMutter() != null) {
            existingPerson.setMutter(person.getMutter());
        }
        if (person.getVater() != null) {
            existingPerson.setVater(person.getVater());
        }

        // Ehegatte-Beziehung nur wenn explizit gesetzt
        if (person.getEhegatte() != null) {
            existingPerson.setEhegatte(person.getEhegatte());
        }

        Person updatedPerson = personService.savePerson(existingPerson);
        return ResponseEntity.ok(updatedPerson);
    }

    // DELETE Person
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    // POST Bildupload
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<Map<String, String>> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            String imagePath = personService.uploadImage(id, file);
            return ResponseEntity.ok(Map.of("imagePath", imagePath));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Fehler beim Hochladen: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
