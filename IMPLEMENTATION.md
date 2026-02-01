# 🌳 Stammbaum Webseite - Implementierungs-Zusammenfassung

## ✅ Vollständig Implementiert

### Backend-Komponenten

#### 1. **Person Entity** (`src/main/java/de/demirer/stammbaum/model/Person.java`)
- Selbstreferenzielle JPA-Entity mit Mutter/Vater-Beziehungen
- Kinder-Listen für beide Elternteile
- Felder: ID, Vorname, Nachname, Geburtsdatum, Todesdatum, Bildpfad
- Automatische Kaskadierungs-Behandlung bei Löschung

#### 2. **PersonRepository** (`src/main/java/de/demirer/stammbaum/repository/PersonRepository.java`)
- Spring Data JPA Repository
- Custom Queries:
  - `findAllRoots()` - alle Stammbäume ohne Eltern
  - `findByFullName()` - Suche nach Namen
  - `findByVorname()`, `findByNachname()`

#### 3. **PersonService** (`src/main/java/de/demirer/stammbaum/service/PersonService.java`)
- CRUD-Operationen
- Hierarchie-Navigation
- **Bildupload-Handling:**
  - Validierung (Format, Größe max. 5MB)
  - Speicherung im Dateisystem
  - Eindeutige Dateinamen
- **Tree-Data-Serialisierung** für D3.js Visualisierung

#### 4. **PersonController** (`src/main/java/de/demirer/stammbaum/controller/PersonController.java`)
REST API Endpoints:
```
GET    /api/persons              - Alle Personen
GET    /api/persons/roots        - Wurzel-Personen
GET    /api/persons/tree         - Kompletter Stammbaum
GET    /api/persons/{id}         - Person Details
POST   /api/persons              - Person erstellen
PUT    /api/persons/{id}         - Person aktualisieren
DELETE /api/persons/{id}         - Person löschen
POST   /api/persons/{id}/upload-image - Bildupload
```

#### 5. **WebController** (`src/main/java/de/demirer/stammbaum/controller/WebController.java`)
- Route `/` → `index.html` (Stammbaum-Ansicht)
- Route `/edit` → `edit.html` (Admin-Panel)

### Frontend-Komponenten

#### 1. **Stammbaum-Visualisierung** (`src/main/resources/templates/index.html`)

**Features:**
- ✅ D3.js Tree-Layout mit Parent-Child Verbindungen
- ✅ Zoom-Funktionalität (Buttons + Maus-Rad)
- ✅ Pan-Funktionalität (Drag & Drop)
- ✅ Touch-Swipe Support (vorbereitet)
- ✅ Querformat-Layout
- ✅ Responsive Design
- ✅ Legende mit Symbolen
- ✅ Person-Karten mit:
  - Profilbild (100x100px, rund)
  - Vollname
  - Geburtsdatum
  - Todesdatum oder "lebend" Status
  - Hover-Effekte

**Funktionen:**
```javascript
- Automatisches Laden von /api/persons/tree
- D3.js Hierarchisches Tree-Layout
- Zoom automatisch angepasst
- Multiple Stammbäume parallel möglich
- Fehlerbehandlung & Empty-State
```

#### 2. **Admin-Panel** (`src/main/resources/templates/edit.html`)

**Features:**
- ✅ Formulär zum Hinzufügen/Bearbeiten
- ✅ Bildupload mit Drag-Drop
- ✅ Bildvorschau vor Upload
- ✅ Eltern-Dropdown-Listen
- ✅ Personen-Liste mit Sucherkennung
- ✅ Edit/Delete-Buttons
- ✅ Success/Error Notifications
- ✅ Responsive Grid-Layout

**Funktionalität:**
```javascript
- Laden aller Personen via /api/persons
- CRUD über REST API
- Bildupload zu /api/persons/{id}/upload-image
- Live-Filterung der Personenliste
- Form-Validierung (Vor- & Nachname erforderlich)
- Eltern-Optionen dynamisch geladen
```

### Konfiguration

#### 1. **Datenbank** (`application.properties`)
```properties
# Standard: H2 (In-Memory für Entwicklung)
spring.datasource.url=jdbc:h2:mem:stammbaum
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# H2-Konsole: http://localhost:8080/h2-console
# Optional MySQL konfigurierbar
```

#### 2. **Dependencies** (`build.gradle`)
- Spring Boot 4.0.2
- Spring Web, Spring Data JPA
- Thymeleaf (Template Engine)
- H2 Database & MySQL Driver
- Lombok (Boilerplate)
- Commons FileUpload
- Jackson (JSON)
- D3.js 7 (CDN)

#### 3. **Dateisystem**
- Upload-Verzeichnis: `src/main/resources/static/uploads/`
- Placeholder-Bild: `src/main/resources/static/placeholder.svg`

---

## 🚀 So startest du die Anwendung

### 1. Entwicklung mit H2 (Empfohlen für Anfang)
```bash
cd Stammbaum
./gradlew bootRun
```

Dann öffne: **http://localhost:8080**

### 2. Mit MySQL (für Produktion)

**Konfiguriere MySQL:**
```sql
CREATE DATABASE stammbaum CHARACTER SET utf8mb4;
```

**Bearbeite `application.properties`:**
```properties
# H2 Zeilen auskommentieren:
# spring.datasource.url=jdbc:h2:...

# MySQL aktivieren:
spring.datasource.url=jdbc:mysql://localhost:3306/stammbaum
spring.datasource.username=root
spring.datasource.password=dein_passwort
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
```

**Neustart:**
```bash
./gradlew bootRun
```

---

## 📱 Benutzer-Workflows

### Stammbaum ansehen
1. Öffne http://localhost:8080
2. Sehe den visuellen Stammbaum
3. Zoom mit Buttons oder Maus-Scroll
4. Pan mit Drag & Drop
5. Reset-Button um Originalansicht wiederherzustellen

### Person hinzufügen
1. Gehe zu http://localhost:8080/edit
2. Fülle Formular aus (Vorname, Nachname erforderlich)
3. Optional: Geburtsdatum, Todesdatum
4. Lade Foto hoch (JPG, PNG, max 5MB)
5. Wähle Mutter/Vater falls vorhanden
6. Klicke "Speichern"
7. Neue Person ist sofort im Stammbaum sichtbar

### Person bearbeiten
1. Gehe zu http://localhost:8080/edit
2. Suche Person in der Liste
3. Klick "Bearbeiten"
4. Ändere Felder
5. Klick "Aktualisieren"

### Person löschen
1. Gehe zu http://localhost:8080/edit
2. Suche Person in der Liste
3. Klick "Löschen"
4. Bestätige Löschung
5. Person und ihre Kinder-Beziehungen werden gelöscht

---

## 🛠️ Technische Details

### Datenbank-Schema
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

### Bildupload-Ablauf
```
1. Admin lädt Bild hoch
2. PersonService validiert:
   - Content-Type muss image/* sein
   - Dateigewöß max 5MB
3. Eindeutiger Name: person_{id}_{timestamp}.{ext}
4. Speicherung: src/main/resources/static/uploads/{filename}
5. DB-Update: Person.bildpfad = /uploads/{filename}
6. Frontend lädt: <img src="/uploads/...">
```

### D3.js Tree-Rendering
```
GET /api/persons/tree
  ↓
Rückgabe: [{id, vorname, nachname, geburtsdatum, todesdatum, bildpfad, children: [...]}, ...]
  ↓
D3.hierarchy() und tree.layout()
  ↓
Links gezeichnet (Parent → Child)
  ↓
Nodes als Foreign Objects mit HTML-Karten
  ↓
Zoom & Pan eingebunden
```

---

## 📦 Projektstruktur
```
Stammbaum/
├── build.gradle                    # Dependencies & Build-Config
├── src/
│   ├── main/
│   │   ├── java/de/demirer/stammbaum/
│   │   │   ├── StammbaumApplication.java
│   │   │   ├── model/
│   │   │   │   └── Person.java
│   │   │   ├── repository/
│   │   │   │   └── PersonRepository.java
│   │   │   ├── service/
│   │   │   │   └── PersonService.java
│   │   │   └── controller/
│   │   │       ├── PersonController.java
│   │   │       └── WebController.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── templates/
│   │       │   ├── index.html         ⭐ Stammbaum-Ansicht
│   │       │   └── edit.html          ⭐ Admin-Panel
│   │       └── static/
│   │           ├── placeholder.svg
│   │           └── uploads/           📸 Bilder landen hier
│   └── test/
│       └── java/...
├── README.md                       # Dokumentation
├── SETUP.md                        # Setup-Anleitung
└── gradle/
    └── wrapper/                    # Gradle Wrapper
```

---

## ✨ Features in dieser Version

### Stammbaum-Ansicht ✅
- [x] Hierarchisches Tree-Layout (D3.js)
- [x] Parent-Child Verbindungen visuell
- [x] Zoom In/Out Buttons
- [x] Maus-Scroll Zoom
- [x] Pan (Drag & Drop)
- [x] Querformat
- [x] Multiple Root-Nodes (mehrere Familien)
- [x] Person-Karten mit Bild + Name + Daten
- [x] Responsive auf Desktop
- [x] Zoom-Reset Button
- [x] Legende

### Admin-Panel (/edit) ✅
- [x] Personen-Formular (Add/Edit)
- [x] Bildupload mit Validierung
- [x] Bildvorschau vor Upload
- [x] Eltern-Zuordnung (Dropdown)
- [x] Personen-Liste
- [x] Live-Suche
- [x] Edit-Button
- [x] Delete-Button mit Bestätigung
- [x] Success/Error Notifications
- [x] Responsive Design

### Backend ✅
- [x] Spring Boot REST API
- [x] JPA Entity Relationships
- [x] Dateisystem-Upload
- [x] Validierungen
- [x] Error Handling
- [x] H2 Datenbank (Entwicklung)
- [x] MySQL Support (Produktion)

---

## 🎯 Nächste Schritte (Optional)

Falls du weitere Features möchtest:

1. **Mehrsprachigkeit** - EN/DE Toggle
2. **Dark Mode** - Dunkles Theme
3. **PDF Export** - Stammbaum ausdrucken
4. **Erweiterte Suche** - Filter nach Daten
5. **Authentication** - Benutzer-Login
6. **Mobile App** - React Native Version
7. **Import/Export** - CSV/GEDCOM Support
8. **Statistiken** - Generationen-Übersicht
9. **Email Sharing** - Stammbaum teilen
10. **Archive Mode** - Alte Versionen speichern

---

## 🆘 Häufige Probleme & Lösungen

| Problem | Lösung |
|---------|--------|
| Port 8080 bereits genutzt | `taskkill /PID <PID> /F` |
| Bilder nicht sichtbar | Überprüfe `src/main/resources/static/uploads/` |
| MySQL Verbindung fehlgeschlagen | Überprüfe Datenbank-Konfiguration in `application.properties` |
| H2-Daten weg nach Neustart | Das ist normal! H2 in-memory wird beim Neustart gelöscht. Wechsel zu MySQL für Persistierung. |
| Build fehlgeschlagen | Nutze `./gradlew clean build` |

---

## 📞 Support

Falls Probleme auftreten:

1. Überprüfe die **README.md** und **SETUP.md**
2. Schau die **Logs** an (Terminal-Output während `bootRun`)
3. Überprüfe **application.properties** Konfiguration
4. Stelle sicher **Java 25+** installiert ist
5. Nutze `./gradlew clean build -x test` für Clean-Build

---

**🎉 Dein Familien Stammbaum ist bereit zum Starten! Viel Erfolg! 🌳👨‍👩‍👧‍👦**
