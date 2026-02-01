package de.demirer.stammbaum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String vorname;

    @Column(nullable = false)
    private String nachname;

    @Column
    private LocalDate geburtsdatum;

    @Column
    private LocalDate todesdatum;

    @Column
    private String bildpfad;

    // Ehegatte-Beziehung (One-to-One bidirektional)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ehegatte_id")
    @JsonIgnore
    private Person ehegatte;

    // Eltern-Beziehungen
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mutter_id")
    @JsonIgnore
    private Person mutter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vater_id")
    @JsonIgnore
    private Person vater;

    // Kinder
    @OneToMany(mappedBy = "mutter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Person> kinderAlsMutter = new ArrayList<>();

    @OneToMany(mappedBy = "vater", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Person> kinderAlsVater = new ArrayList<>();

    @PreRemove
    private void preRemove() {
        // Entferne Referenzen zu dieser Person
        if (this.mutter != null) {
            this.mutter.getKinderAlsMutter().remove(this);
        }
        if (this.vater != null) {
            this.vater.getKinderAlsVater().remove(this);
        }
    }

    public String getFullName() {
        return vorname + " " + nachname;
    }
}
