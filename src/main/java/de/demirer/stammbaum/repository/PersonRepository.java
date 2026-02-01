package de.demirer.stammbaum.repository;

import de.demirer.stammbaum.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    // Finde alle Wurzel-Personen (ohne Eltern)
    @Query("SELECT p FROM Person p WHERE p.mutter IS NULL AND p.vater IS NULL")
    List<Person> findAllRoots();

    // Finde Personen nach Vorname
    List<Person> findByVorname(String vorname);

    // Finde Personen nach Nachname
    List<Person> findByNachname(String nachname);

    // Finde Personen nach vollständigem Namen
    @Query("SELECT p FROM Person p WHERE p.vorname = :vorname AND p.nachname = :nachname")
    List<Person> findByFullName(String vorname, String nachname);
}
