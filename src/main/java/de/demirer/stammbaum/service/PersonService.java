package de.demirer.stammbaum.service;

import de.demirer.stammbaum.model.Person;
import de.demirer.stammbaum.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // CRUD Operations
    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person nicht gefunden mit ID: " + id));
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public List<Person> getAllRoots() {
        return personRepository.findAllRoots();
    }

    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }

    // Hierarchie-Navigation
    public Map<String, Object> getPersonWithHierarchy(Long id) {
        Person person = getPersonById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("person", person);
        result.put("mutter", person.getMutter());
        result.put("vater", person.getVater());
        result.put("kinderAlsMutter", person.getKinderAlsMutter());
        result.put("kinderAlsVater", person.getKinderAlsVater());
        return result;
    }

    // Bildupload
    public String uploadImage(Long personId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Datei ist leer");
        }

        // Validierung
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Nur Bilddateien sind erlaubt");
        }

        // Maximale Größe: 5MB
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Datei ist zu groß (max. 5MB)");
        }

        // Verzeichnis erstellen falls nicht vorhanden
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Eindeutigen Dateinamen generieren
        String filename = "person_" + personId + "_" + System.currentTimeMillis() +
                         getFileExtension(file.getOriginalFilename());
        Path filePath = uploadPath.resolve(filename);

        // Datei speichern
        Files.write(filePath, file.getBytes());

        // Person aktualisieren mit Bildpfad
        Person person = getPersonById(personId);
        person.setBildpfad("/uploads/" + filename);
        savePerson(person);

        return "/uploads/" + filename;
    }

    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return ".jpg";
    }

    // Alle Stammbaum-Daten (für D3.js Visualisierung)
    public List<Map<String, Object>> getTreeData() {
        List<Person> allPersons = getAllPersons();
        Set<Long> processedIds = new HashSet<>();
        List<Map<String, Object>> trees = new ArrayList<>();

        // Finde alle Wurzeln (Personen ohne Mutter und Vater)
        List<Person> roots = allPersons.stream()
                .filter(p -> p.getMutter() == null && p.getVater() == null)
                .toList();

        // Wenn keine Wurzeln gefunden, zeige alle Top-Level Personen
        if (roots.isEmpty()) {
            roots = allPersons;
        }

        for (Person root : roots) {
            if (!processedIds.contains(root.getId())) {
                trees.add(buildTreeNode(root, processedIds));
            }
        }

        return trees;
    }

    private Map<String, Object> buildTreeNode(Person person, Set<Long> processedIds) {
        if (person == null || processedIds.contains(person.getId())) {
            return null;
        }

        processedIds.add(person.getId());

        Map<String, Object> node = new HashMap<>();
        node.put("id", person.getId());
        node.put("vorname", person.getVorname());
        node.put("nachname", person.getNachname());
        node.put("fullName", person.getFullName());
        node.put("geburtsdatum", person.getGeburtsdatum());
        node.put("todesdatum", person.getTodesdatum());
        node.put("bildpfad", person.getBildpfad() != null ? person.getBildpfad() : "/placeholder.png");

        List<Map<String, Object>> kinder = new ArrayList<>();

        // Sammle alle eindeutigen Kinder
        Set<Long> kinderIds = new HashSet<>();

        if (person.getKinderAlsMutter() != null) {
            for (Person kind : person.getKinderAlsMutter()) {
                if (!kinderIds.contains(kind.getId())) {
                    kinderIds.add(kind.getId());
                    Map<String, Object> childNode = buildTreeNode(kind, processedIds);
                    if (childNode != null) {
                        kinder.add(childNode);
                    }
                }
            }
        }

        if (person.getKinderAlsVater() != null) {
            for (Person kind : person.getKinderAlsVater()) {
                if (!kinderIds.contains(kind.getId())) {
                    kinderIds.add(kind.getId());
                    Map<String, Object> childNode = buildTreeNode(kind, processedIds);
                    if (childNode != null) {
                        kinder.add(childNode);
                    }
                }
            }
        }

        if (!kinder.isEmpty()) {
            node.put("children", kinder);
        }

        return node;
    }
}
