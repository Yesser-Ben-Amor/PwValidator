# 🔐 Passwort Validator

Ein umfassender Passwort-Validator in Java, der die Sicherheit von Passwörtern analysiert und bewertet.

## 🚀 Features

- **Mindestlänge-Prüfung**: Überprüft, ob das Passwort mindestens 8 Zeichen lang ist
- **Ziffer-Erkennung**: Stellt sicher, dass mindestens eine Ziffer enthalten ist
- **Groß-/Kleinschreibung**: Prüft auf Groß- und Kleinbuchstaben
- **Sonderzeichen-Validierung**: Erkennt spezielle Zeichen für erhöhte Sicherheit
- **Schwache Passwörter**: Überprüft gegen eine Liste häufig verwendeter schwacher Passwörter
- **Zeichengruppen-Analyse**: Zählt verschiedene Zeichentypen für eine Gesamtbewertung

## 🎯 Bewertungssystem

- 🔒 **STARKES PASSWORT**: Alle Kriterien erfüllt
- 🔓 **MITTLERES PASSWORT**: 3+ Zeichengruppen und Mindestlänge
- ⚠️ **SCHWACHES PASSWORT**: Verbesserung erforderlich

## 🛠️ Installation & Ausführung

### Voraussetzungen
- Java 8 oder höher
- Git

### Kompilieren und Ausführen
```bash
# Repository klonen
git clone https://github.com/Yesser-Ben-Amor/PwValidator.git
cd PwValidator

# Kompilieren
javac -d target/classes src/main/java/org/example/Main.java

# Ausführen
java -cp target/classes org.example.Main
```

## 📝 Verwendung

1. Starten Sie das Programm
2. Geben Sie ein Passwort ein
3. Erhalten Sie eine detaillierte Analyse mit Bewertung
4. Geben Sie `exit` ein, um das Programm zu beenden

## 🔍 Beispiel-Ausgabe

```
Der beste Passwort Validator on the City
#################
Bitte geben Sie das Passwort ein!
(Geben Sie 'exit' ein, um das Programm zu beenden)

Passwort eingeben
MySecure123!

--- Passwort Analyse ---
Passwort: MySecure123!
Mindestlänge (8 Zeichen): ✓
Enthält Ziffer: ✓
Groß- und Kleinbuchstaben: ✓
Sonderzeichen: ✓
Ist schwaches Passwort: ✓ NEIN
Anzahl Zeichengruppen: 4/4

🔒 STARKES PASSWORT! Alle Kriterien erfüllt.
```

## 🧪 Validierungskriterien

### Schwache Passwörter (Blacklist)
Das Programm erkennt häufig verwendete schwache Passwörter wie:
- `password`, `123456`, `admin`, `qwerty`
- Und viele weitere...

### Erlaubte Sonderzeichen
```
!@#$%^&*()_+-=[]{}|;:,.<>?
```

## 👨‍💻 Autor

**Yesser Ben Amor**
- GitHub: [@Yesser-Ben-Amor](https://github.com/Yesser-Ben-Amor)

## 📄 Lizenz

Dieses Projekt steht unter der MIT-Lizenz.
