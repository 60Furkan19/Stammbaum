ht# Familien Stammbaum Webseite

Eine interaktive Spring Boot Webseite zum Verwalten und Visualisieren von Familienstammbäumen mit MySQL-Datenbank, Zoom/Pan-Funktionalität und Admin-Panel zum Hinzufügen/Bearbeiten/Löschen von Personen.

## Features

✅ **Interaktive Stammbaum-Visualisierung**
- D3.js basierte Baumdarstellung mit hierarchischen Beziehungen
- Zoom & Pan Funktionalität (Maus + Zoom-Buttons)
- Touch-Swipe Support
- Querformat-Layout
- Person-Karten mit Foto/Name/Geburtsdatum/Todesdatum

✅ **Admin-Panel (/edit)**
- Personen hinzufügen/bearbeiten/löschen
- Bildupload (direkt ins Dateisystem: `src/main/resources/static/uploads/`)
- Eltern-Kind-Beziehungen definieren
- Live-Suchfunktion
- Responsive Design

✅ **Backend (Spring Boot + REST API)**
- Spring Data JPA für Datenbankoperationen
- RESTful API Endpoints:
  - `GET /api/persons` – Alle Personen
  - `GET /api/persons/tree` – Stammbaum-Daten für D3.js
  - `POST /api/persons` – Person erstellen
  - `PUT /api/persons/{id}` – Person aktualisieren
  - `DELETE /api/persons/{id}` – Person löschen
  - `POST /api/persons/{id}/upload-image` – Bild hochladen

✅ **Datenbank**
- **Entwicklung:** H2 (in-memory, einfach zu testen)
- **Produktion:** MySQL (konfigurierbar in `application.properties`)
- Selbstreferenzielle Beziehungen: Mutter/Vater ↔ Kinder

## Installation & Setup

### 1. Voraussetzungen
- Java 25+
- Gradle 9.3.0+ (ist im Projekt enthalten)
- Optional: MySQL 8.0+

### 2. Projekt starten (Entwicklung mit H2)

```bash
cd Stammbaum
./gradlew bootRun
```

Die Anwendung läuft dann auf **http://localhost:8080**

- **Stammbaum-Ansicht:** http://localhost:8080/
- **Admin-Panel:** http://localhost:8080/edit
- **H2-Konsole:** http://localhost:8080/h2-console (für DB-Debugging)

### 3. Für MySQL konfigurieren (Produktion)

Bearbeite `src/main/resources/application.properties`:

```properties
# Uncomment MySQL-Sektion und kommentiere H2 aus
spring.datasource.url=jdbc:mysql://localhost:3306/stammbaum?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=DEIN_PASSWORT
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

Und erstelle die Datenbank:
```sql
CREATE DATABASE stammbaum;
```

## Dateistruktur

```
src/main/
├── java/de/demirer/stammbaum/
│   ├── StammbaumApplication.java           # Spring Boot Entry Point
│   ├── model/
│   │   └── Person.java                     # JPA Entity mit Parent-Child Beziehungen
│   ├── repository/
│   │   └── PersonRepository.java           # Spring Data JPA Interface
│   ├── service/
│   │   └── PersonService.java              # Business Logic & Bildupload
│   └── controller/
│       ├── PersonController.java           # REST API Endpoints
│       └── WebController.java              # HTML Views (index, edit)
└── resources/
    ├── application.properties               # Database & File Upload Config
    ├── templates/
    │   ├── index.html                      # Stammbaum-Visualisierung (D3.js)
    │   └── edit.html                       # Admin-Panel
    └── static/
        ├── placeholder.svg                 # Default-Bild wenn kein Upload
        └── uploads/                        # Hochgeladene Bilder landen hier
```

## API Dokumentation

### Stammbaum abrufen (für D3.js Visualisierung)
```bash
GET /api/persons/tree
```
Rückgabe: Array von Baumstrukturen (JSON)

### Alle Personen abrufen
```bash
GET /api/persons
```

### Person erstellen
```bash
POST /api/persons
Content-Type: application/json

{
  "vorname": "Johann",
  "nachname": "Müller",
  "geburtsdatum": "1950-05-15",
  "todesdatum": null,
  "mutter": { "id": 1 },
  "vater": { "id": 2 }
}
```

### Bild hochladen
```bash
POST /api/persons/{id}/upload-image
Content-Type: multipart/form-data

file: <image_file>
```

### Person aktualisieren
```bash
PUT /api/persons/{id}
Content-Type: application/json

{
  "vorname": "Johann",
  "nachname": "Müller",
  ...
}
```

### Person löschen
```bash
DELETE /api/persons/{id}
```

## UI Funktionen

### Stammbaum-Ansicht (/)
- **Zoom-Buttons:** `+` / `−` oben rechts
- **Maus-Zoom:** Scroll-Rad zum Vergrößern/Verkleinern
- **Pan:** Klick + Drag zum Verschieben
- **Reset:** Button oben um zur Standardansicht zu gehen
- **Legende:** Unten links mit Erklärungen

### Admin-Panel (/edit)
- **Personen-Formular:** Links zum Hinzufügen/Bearbeiten
- **Bildupload:** Drag & Drop oder Klick
- **Eltern-Zuordnung:** Dropdown-Listen für Mutter & Vater
- **Personen-Liste:** Rechts mit Suchfunktion
- **Edit/Delete Buttons:** Pro Person in der Liste

## Datenbank-Schema

### Person Tabelle
```sql
CREATE TABLE person (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  vorname VARCHAR(255) NOT NULL,
  nachname VARCHAR(255) NOT NULL,
  geburtsdatum DATE,
  todesdatum DATE,
  bildpfad VARCHAR(255),
  mutter_id BIGINT,
  vater_id BIGINT,
  FOREIGN KEY (mutter_id) REFERENCES person(id),
  FOREIGN KEY (vater_id) REFERENCES person(id)
);
```

## Bildverwaltung

- **Upload-Verzeichnis:** `src/main/resources/static/uploads/`
- **Max. Dateigröße:** 5 MB
- **Erlaubte Formate:** JPG, PNG, GIF, WebP
- **Dateinamen:** `person_{id}_{timestamp}.{ext}`
- **Zugriff:** `/uploads/{filename}` in der Webseite

## Troubleshooting

### Port 8080 bereits in Verwendung
```bash
# Anderen Prozess auf Port 8080 finden und beenden
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### H2-Datenbank zurücksetzen
Lösche die Datenbank oder starte die App neu (H2 in-memory wird beim Neu-Start geleert)

### Bilder werden nicht angezeigt
1. Upload-Verzeichnis existiert? `src/main/resources/static/uploads/`
2. Dateirechte OK?
3. Browser-Cache leeren (F5 + Ctrl)

### MySQL Verbindungsfehler
Überprüfe in `application.properties`:
- Datenbank URL korrekt?
- MySQL läuft auf localhost:3306?
- Username & Password korrekt?
- Datenbank "stammbaum" existiert?

## Dependencies

- **Spring Boot 4.0.2** – Framework
- **Spring Data JPA** – ORM
- **Spring Web** – MVC/REST
- **Thymeleaf** – Template Engine
- **Hibernate 7.2.1** – JPA Provider
- **H2 Database** – In-Memory DB (Entwicklung)
- **MySQL Connector 8.4.0** – MySQL Driver
- **Commons FileUpload** – File Upload
- **Jackson** – JSON Processing
- **Lombok** – Boilerplate Reduktion
- **D3.js 7** – Frontend Visualisierung (CDN)

## Next Steps / Mögliche Erweiterungen

- [ ] Mehrsprachigkeit (EN/DE)
- [ ] Dark Mode
- [ ] PDF Export
- [ ] Erweiterte Filter/Suche
- [ ] Benutzer-Authentifizierung
- [ ] Family Tree Statistiken
- [ ] Import/Export (CSV, GEDCOM)
- [ ] Mobile App (React Native)

## Lizenz

Eigenes Projekt - Frei verwendbar

---

**Viel Erfolg beim Verwalten deines Familienstammbaums! 🌳👨‍👩‍👧‍👦**
