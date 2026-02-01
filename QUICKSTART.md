# 🚀 Familien Stammbaum - Schnellstart Anleitung

## ✅ Was wurde implementiert?

Deine **vollständige interaktive Stammbaum-Webseite** mit:

### 🎨 Frontend (HTML/CSS/JavaScript)
- ✅ **Stammbaum-Visualisierung** (`/`) - D3.js mit Zoom/Pan
- ✅ **Admin-Panel** (`/edit`) - Personen verwalten + Bildupload
- ✅ Responsive Design
- ✅ Person-Karten mit Foto, Name, Daten

### 🔧 Backend (Spring Boot Java)
- ✅ **REST API** für alle CRUD-Operationen
- ✅ **JPA-Datenbank** mit Parent-Child Beziehungen
- ✅ **Bildupload** ins Dateisystem
- ✅ **H2-Datenbank** für Entwicklung (keine Installation nötig)
- ✅ **MySQL** optional für Produktion

---

## 🎯 Anwendung starten

### Option 1: Über Terminal
```bash
cd C:\Users\Furka\Desktop\Java\Lernen\Stammbaum
.\gradlew.bat bootRun
```

### Option 2: In IntelliJ IDEA
1. Öffne das Projekt in IntelliJ
2. Klicke auf den grünen Play-Button neben `StammbaumApplication.java`
3. Oder: Run → Run 'StammbaumApplication'

### ⏳ Wartezeit
- Erstes Mal: ~60 Sekunden (Gradle & Dependencies laden)
- Danach: ~10-15 Sekunden

### ✔️ Server läuft wenn du siehst:
```
Tomcat started on port(s): 8080
Started StammbaumApplication in X seconds
```

---

## 🌐 URLs zum Öffnen

| URL | Funktion |
|-----|----------|
| **http://localhost:8080/** | 🎨 Stammbaum anschauen |
| **http://localhost:8080/edit** | ✏️ Stammbaum bearbeiten |
| **http://localhost:8080/h2-console** | 💾 H2-DB Console (Debug) |

---

## 📋 Schritt-für-Schritt: Erste Person hinzufügen

### 1. Gehe zur Bearbeitungsseite
Öffne: **http://localhost:8080/edit**

### 2. Formular ausfüllen
```
Vorname:      Johann
Nachname:     Müller
Geburtsdatum: 1950-05-15
```

### 3. (Optional) Bild hochladen
- Klick auf "📸 Klick zum Bild hochladen"
- Wähle JPG/PNG Datei (max 5MB)

### 4. Speichern
- Klick "Speichern" Button
- ✅ Person wird sofort hinzugefügt

### 5. Stammbaum ansehen
- Gehe zu **http://localhost:8080/**
- Siehe deine Person im interaktiven Baum!

---

## 🔗 Zweite Person als Kind hinzufügen

### 1. Gehe zur Bearbeitungsseite
Öffne: **http://localhost:8080/edit**

### 2. Neue Person erstellen
```
Vorname:      Michael
Nachname:     Müller
Geburtsdatum: 1975-03-20
```

### 3. Eltern zuordnen
- **Vater Dropdown** → "Johann Müller" wählen
- oder
- **Mutter Dropdown** → andere Person wählen

### 4. Speichern
- Klick "Speichern"
- ✅ Michael wird als Kind von Johann angezeigt!

### 5. Stammbaum anschauen
- Gehe zu **http://localhost:8080/**
- Sehe Johann mit Michael darunter verbunden

---

## 🎮 Stammbaum-Ansicht - Bedienung

### Zoomen
- **Buttons:** Klick `+` / `−` oben rechts
- **Maus:** Scroll-Rad nach oben/unten
- **Automatisch:** Reset-Button zur Standardansicht

### Verschieben (Pan)
- Klick + Drag mit Maus → gesamter Baum bewegt sich

### Person-Karten
- **Bild:** 100x100px Kreis
- **Name:** Vorname + Nachname
- **Daten:** Geburtsdatum & Todesdatum (oder "lebend")

### Verbindungen
- **Linien:** Zeigen Parent-Child Beziehungen
- **Hover:** Linien werden hervorgehoben

---

## 📝 Hauptfunktionen im Admin-Panel (/edit)

### ✏️ Person Bearbeiten
1. Suche in der Liste rechts
2. Klick "Bearbeiten"
3. Ändere Felder
4. Klick "Aktualisieren"

### 🗑️ Person Löschen
1. Klick "Löschen" Button
2. Bestätige im Dialog
3. Person + Beziehungen werden gelöscht

### 🔍 Suchfunktion
- Gib Namen in Suchbox ein
- Liste filtert live
- Funktioniert mit Vor- oder Nachnamen

### 🖼️ Bildverwaltung
- **Hochladen:** Datei auswählen → Klick "Speichern"
- **Speicherort:** `src/main/resources/static/uploads/`
- **Formate:** JPG, PNG, GIF, WebP
- **Max Größe:** 5 MB

---

## 🗄️ Datenbank-Info

### Verwendung
- **Entwicklung (Default):** H2 In-Memory (keine Installation nötig)
- **Produktion:** MySQL (optional konfigurierbar)

### H2 besonderheiten
- ✅ Läuft im RAM
- ⚠️ Daten gehen beim Neustart verloren (zum Testen OK)
- 📊 Konsole: http://localhost:8080/h2-console
  - Benutzername: `sa`
  - Passwort: (leer)
  - JDBC URL: `jdbc:h2:mem:stammbaum`

### MySQL aktivieren (später möglich)
1. MySQL installieren
2. Datenbank erstellen: `CREATE DATABASE stammbaum;`
3. `application.properties` bearbeiten (siehe SETUP.md)
4. Neustart

---

## 🛠️ API Endpoints (für Entwickler)

```bash
# Alle Personen abrufen
curl http://localhost:8080/api/persons

# Stammbaum-Daten (für D3.js)
curl http://localhost:8080/api/persons/tree

# Person erstellen
curl -X POST http://localhost:8080/api/persons \
  -H "Content-Type: application/json" \
  -d '{"vorname":"Johann","nachname":"Müller","geburtsdatum":"1950-05-15"}'

# Bild hochladen
curl -X POST http://localhost:8080/api/persons/1/upload-image \
  -F "file=@mein_foto.jpg"

# Person löschen
curl -X DELETE http://localhost:8080/api/persons/1
```

---

## ❌ Häufige Probleme

### Problem: "Port 8080 bereits in Verwendung"
**Lösung:**
```bash
netstat -ano | findstr :8080
# Notiere die PID und töte sie:
taskkill /PID <PID> /F
```

### Problem: "Bilder werden nicht angezeigt"
**Lösung:**
```bash
# Überprüfe ob Verzeichnis existiert:
dir src\main\resources\static\uploads\

# Falls nicht, erstelle es:
mkdir src\main\resources\static\uploads
```

### Problem: "Fehler beim Build"
**Lösung:**
```bash
.\gradlew.bat clean build -x test
```

### Problem: "Server startet nicht"
**Lösung:**
1. Überprüfe Java-Version: `java -version` (min. Java 25)
2. Schau Logs im Terminal
3. Überprüfe `application.properties`
4. Versuche: `.\gradlew.bat clean bootRun`

### Problem: "H2-Daten sind weg nach Restart"
**Das ist normal!** H2 In-Memory wird beim Neustart gelöscht. 
- Nutze MySQL für persistente Daten
- Oder: In `application.properties` auf `jdbc:h2:file:./db/stammbaum` ändern

---

## 📂 Wichtige Dateien

| Datei | Funktion |
|-------|----------|
| `src/main/resources/application.properties` | Datenbank & Config |
| `src/main/resources/templates/index.html` | Stammbaum-Ansicht |
| `src/main/resources/templates/edit.html` | Admin-Panel |
| `src/main/java/.../controller/PersonController.java` | REST API |
| `src/main/java/.../model/Person.java` | DB-Entity |
| `src/main/resources/static/uploads/` | Hochgeladene Bilder |

---

## 🎓 Nächste Schritte

### Basis-Funktionalität testen
1. ✅ Server starten
2. ✅ Person hinzufügen
3. ✅ Bild hochladen
4. ✅ Stammbaum anschauen
5. ✅ Zoom/Pan testen

### Datenbank auf MySQL umstellen
1. MySQL installieren
2. SETUP.md lesen
3. `application.properties` anpassen

### Erweiterte Features (optional)
- Dark Mode hinzufügen
- PDF Export
- Mehrsprachigkeit
- Mobile App

---

## 📚 Dokumentation

| Datei | Inhalt |
|-------|--------|
| **README.md** | Vollständige Feature-Übersicht |
| **SETUP.md** | Datenbank-Konfiguration |
| **IMPLEMENTATION.md** | Technische Details & Architektur |
| **QUICKSTART.md** | Diese Datei - schnelle Anleitung |

---

## ✨ Das Projekt enthält

```
✅ 5 Java-Klassen (Entity, Repository, Service, 2x Controller)
✅ 2 HTML-Templates mit CSS & JavaScript
✅ REST API mit 7 Endpoints
✅ Bildupload-Funktionalität
✅ D3.js Visualisierung
✅ Responsive Design
✅ H2 & MySQL Support
✅ Vollständige Fehlerbehandlung
✅ Admin-Interface
```

---

## 🎉 Fertig!

Deine Stammbaum-Anwendung ist **vollständig funktionsfähig**! 

**Starten:**
```bash
.\gradlew.bat bootRun
```

**Öffne:**
- http://localhost:8080/ (Ansicht)
- http://localhost:8080/edit (Verwaltung)

**Viel Erfolg beim Verwenden! 🌳👨‍👩‍👧‍👦**
