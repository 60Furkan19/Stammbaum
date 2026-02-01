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
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Map<String, Object> personData) {
        Person existingPerson = personService.getPersonById(id);

        // Grunddaten aktualisieren
        if (personData.containsKey("vorname")) {
            existingPerson.setVorname((String) personData.get("vorname"));
        }
        if (personData.containsKey("nachname")) {
            existingPerson.setNachname((String) personData.get("nachname"));
        }
        if (personData.containsKey("geburtsdatum")) {
            String geburtsdatum = (String) personData.get("geburtsdatum");
            existingPerson.setGeburtsdatum(geburtsdatum != null && !geburtsdatum.isEmpty() 
                ? java.time.LocalDate.parse(geburtsdatum) : null);
        }
        if (personData.containsKey("todesdatum")) {
            String todesdatum = (String) personData.get("todesdatum");
            existingPerson.setTodesdatum(todesdatum != null && !todesdatum.isEmpty() 
                ? java.time.LocalDate.parse(todesdatum) : null);
        }

        // Mutter-Beziehung aktualisieren (auch Entfernen möglich)
        if (personData.containsKey("mutter")) {
            Object mutterData = personData.get("mutter");
            if (mutterData == null) {
                existingPerson.setMutter(null);
            } else if (mutterData instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> mutterMap = (Map<String, Object>) mutterData;
                Object mutterId = mutterMap.get("id");
                if (mutterId != null) {
                    Person mutter = personService.getPersonById(Long.valueOf(mutterId.toString()));
                    existingPerson.setMutter(mutter);
                }
            }
        }

        // Vater-Beziehung aktualisieren (auch Entfernen möglich)
        if (personData.containsKey("vater")) {
            Object vaterData = personData.get("vater");
            if (vaterData == null) {
                existingPerson.setVater(null);
            } else if (vaterData instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> vaterMap = (Map<String, Object>) vaterData;
                Object vaterId = vaterMap.get("id");
                if (vaterId != null) {
                    Person vater = personService.getPersonById(Long.valueOf(vaterId.toString()));
                    existingPerson.setVater(vater);
                }
            }
        }

        // Ehegatte-Beziehung aktualisieren (auch Entfernen möglich)
        if (personData.containsKey("ehegatte")) {
            Object ehegatteData = personData.get("ehegatte");
            if (ehegatteData == null) {
                // Entferne bidirektionale Beziehung
                if (existingPerson.getEhegatte() != null) {
                    Person alterEhegatte = existingPerson.getEhegatte();
                    alterEhegatte.setEhegatte(null);
                    personService.savePerson(alterEhegatte);
                }
                existingPerson.setEhegatte(null);
            } else if (ehegatteData instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> ehegatteMap = (Map<String, Object>) ehegatteData;
                Object ehegatteId = ehegatteMap.get("id");
                if (ehegatteId != null) {
                    // Entferne alte Beziehung
                    if (existingPerson.getEhegatte() != null) {
                        Person alterEhegatte = existingPerson.getEhegatte();
                        alterEhegatte.setEhegatte(null);
                        personService.savePerson(alterEhegatte);
                    }
                    // Setze neue Beziehung bidirektional
                    Person neuerEhegatte = personService.getPersonById(Long.valueOf(ehegatteId.toString()));
                    existingPerson.setEhegatte(neuerEhegatte);
                    neuerEhegatte.setEhegatte(existingPerson);
                    personService.savePerson(neuerEhegatte);
                }
            }
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
