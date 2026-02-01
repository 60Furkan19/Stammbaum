# Stammbaum Anwendung - Konfigurationsanweisungen

## Datenbankauswahl

### Option 1: H2 (Standard für Entwicklung)
**Vorteil:** Keine Installation nötig, schnell zum Testen
**Datei:** src/main/resources/application.properties
```properties
# H2 aktiviert (Standard)
spring.datasource.url=jdbc:h2:mem:stammbaum
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true

# H2 Konsole: http://localhost:8080/h2-console
# Benutzername: sa
# Passwort: (leer)
# JDBC URL: jdbc:h2:mem:stammbaum
```

### Option 2: MySQL (für Produktion)

**Schritt 1:** MySQL installieren
- Download: https://dev.mysql.com/downloads/mysql/
- Oder: `choco install mysql-server` (mit Chocolatey)

**Schritt 2:** Datenbank erstellen
```bash
mysql -u root -p
CREATE DATABASE stammbaum CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;
```

**Schritt 3:** application.properties anpassen
```properties
# H2 auskommentieren:
# spring.datasource.url=jdbc:h2:mem:stammbaum
# spring.datasource.driverClassName=org.h2.Driver
# spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
# spring.h2.console.enabled=true

# MySQL aktivieren:
spring.datasource.url=jdbc:mysql://localhost:3306/stammbaum?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=DEIN_PASSWORT_HIER
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
```

**Schritt 4:** Anwendung neu starten
```bash
./gradlew bootRun
```

## Bildupload Konfiguration

Standard-Verzeichnis: `src/main/resources/static/uploads/`

Falls du ein anderes Verzeichnis möchtest, ändere in `application.properties`:
```properties
file.upload-dir=C:/meine_bilder/stammbaum/
```

Stelle sicher, dass das Verzeichnis existiert und Schreibberechtigung hat!

## Troubleshooting

### Fehler: "Communications link failure"
→ MySQL läuft nicht oder falsche Zugangsdaten. Überprüfe:
```bash
mysql -u root -p
```

### Fehler: "Dialect not recognized"
→ Datenbank-Konfiguration falsch. Vergleiche mit den obigen Beispielen.

### Port 8080 bereits in Verwendung
```bash
netstat -ano | findstr :8080
# Notiere die PID und töte den Prozess:
taskkill /PID <PID> /F
```

### Bilder werden nicht gespeichert
→ Upload-Verzeichnis existiert nicht oder keine Schreibberechtigung
```bash
mkdir src\main\resources\static\uploads
```

---

**Fragen? → Siehe README.md für vollständige Dokumentation**
