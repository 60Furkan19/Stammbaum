# 🚀 STARTEN JETZT - Stammbaum Webseite

## ⚡ 3-Schritte Anleitung

### 1️⃣ Terminal öffnen
```
Drücke: Windows + R
Tippe: cmd
Drücke: Enter
```

### 2️⃣ Zum Projekt navigieren
```bash
cd C:\Users\Furka\Desktop\Java\Lernen\Stammbaum
```

### 3️⃣ Server starten
```bash
.\gradlew.bat bootRun
```

⏳ **Warten:** 30-60 Sekunden beim ersten Start (Dependencies werden heruntergeladen)

---

## ✅ Server läuft wenn du siehst:

```
Tomcat started on port(s): 8080
Started StammbaumApplication
```

---

## 🌐 Jetzt öffnen (im Browser)

### Stammbaum ansehen:
**http://localhost:8080/**

### Stammbaum bearbeiten:
**http://localhost:8080/edit**

---

## 📝 Erste Person hinzufügen

1. Gehe zu http://localhost:8080/edit
2. Fülle Felder aus:
   - **Vorname:** Johann
   - **Nachname:** Müller
   - **Geburtsdatum:** 1950-05-15
3. Klick **"Speichern"**
4. ✅ Gehe zu http://localhost:8080 und sehe Johann im Baum!

---

## 🖼️ Bild hinzufügen

1. Gehe zu http://localhost:8080/edit
2. Klick **"📸 Klick zum Bild hochladen"**
3. Wähle ein JPG oder PNG (max 5MB)
4. Klick **"Speichern"**
5. ✅ Person hat jetzt Profilbild!

---

## 🔗 Zweite Person als Kind

1. Gehe zu http://localhost:8080/edit
2. Neue Person:
   - **Vorname:** Michael
   - **Nachname:** Müller
3. Unter "Eltern zuordnen":
   - **Vater:** Johann Müller (aus Dropdown)
4. Klick **"Speichern"**
5. ✅ Gehe zu `/` und sehe Michael unter Johann!

---

## 🎮 Interaktive Karte

### Zoomen
- **+/-** Buttons oben rechts
- Oder: **Maus-Scroll**

### Verschieben
- **Drag & Drop** mit Maus

### Zurücksetzen
- **"Zurücksetzen"** Button

---

## ❌ Probleme?

### Port 8080 bereits genutzt
```bash
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Build-Fehler in IDE
- Das ist OK! Ist nur ein IDE-Index-Problem
- Der Build wird trotzdem funktionieren
- Versuche: **File → Invalidate Caches → Restart**

### H2-Daten nach Neustart weg
Das ist **normal**! H2 läuft im RAM.
- Neustart = Daten weg (gut zum Testen!)
- Für Persistierung: MySQL einrichten (siehe SETUP.md)

### Server startet nicht
```bash
.\gradlew.bat clean bootRun
```

---

## 📚 Weitere Infos

| Datei | Inhalt |
|-------|--------|
| README.md | Vollständige Dokumentation |
| SETUP.md | MySQL-Setup |
| QUICKSTART.md | Detaillierte Anleitung |
| FILES.md | Alle Dateien erklärt |

---

## 🎉 Fertig!

Deine Stammbaum-App läuft jetzt!

```bash
.\gradlew.bat bootRun
```

Öffne:
- **http://localhost:8080/** (Ansicht)
- **http://localhost:8080/edit** (Bearbeitung)

**Viel Spaß! 🌳👨‍👩‍👧‍👦**
