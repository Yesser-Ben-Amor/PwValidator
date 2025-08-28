package org.example; // Paket-Deklaration für die Klasse

// Import-Anweisungen für benötigte Java-Klassen
import java.net.InetAddress; // Für IP-Adress-Operationen
import java.net.UnknownHostException; // Für Netzwerk-Fehlerbehandlung
import java.util.*; // Für Collections (Set, HashSet, Arrays)

/**
 * Sicherheitsmonitor für SQL-Injection-Erkennung
 * Überwacht verdächtige Eingaben und trackt IP-Adressen
 */
public class SecurityMonitor { // Hauptklasse für Sicherheitsüberwachung
    
    // Konstante: Unveränderliche Liste der häufigsten SQL-Injection-Ausdrücke
    private static final Set<String> SQL_INJECTION_PATTERNS = new HashSet<>(Arrays.asList(
        // Klassische SQL-Injection-Muster (häufigste Angriffe)
        "' OR '1'='1", // Immer-wahr-Bedingung zum Umgehen der Authentifizierung
        "' OR 1=1--", // Mathematische Immer-wahr-Bedingung mit Kommentar
        "' OR 1=1#", // Gleiche Bedingung mit Hash-Kommentar (MySQL)
        "' OR 1=1/*", // Gleiche Bedingung mit Block-Kommentar
        "admin'--", // Admin-Login mit Kommentar (überspringt Passwort-Prüfung)
        "admin'#", // Admin-Login mit Hash-Kommentar
        "admin'/*", // Admin-Login mit Block-Kommentar
        "' OR 'x'='x", // Buchstaben-basierte Immer-wahr-Bedingung
        "' OR 'a'='a", // Weitere Buchstaben-basierte Immer-wahr-Bedingung
        "') OR ('1'='1", // Klammer-geschlossene Immer-wahr-Bedingung
        
        // Union-basierte Injections (Daten aus anderen Tabellen extrahieren)
        "' UNION SELECT", // Vereinigung mit SELECT-Anweisung
        "' UNION ALL SELECT", // Vereinigung aller Datensätze mit SELECT
        "UNION SELECT NULL", // Vereinigung mit NULL-Werten
        "UNION SELECT 1,2,3", // Vereinigung mit Zahlen-Platzhaltern
        
        // Blind SQL Injection (Information ohne direkte Ausgabe extrahieren)
        "' AND (SELECT SUBSTRING", // Teilstring-Extraktion für Blind-Angriffe
        "' AND ASCII(SUBSTRING", // ASCII-Wert-Extraktion für Blind-Angriffe
        "' AND LENGTH(", // Längen-Bestimmung für Blind-Angriffe
        "' WAITFOR DELAY", // Zeit-Verzögerung für Blind-Angriffe (SQL Server)
        "' AND SLEEP(5)", // Schlaf-Funktion für Blind-Angriffe (MySQL)
        
        // Error-basierte Injection (Fehler-Nachrichten für Informationsgewinnung)
        "' AND EXTRACTVALUE", // XML-Wert-Extraktion (MySQL)
        "' AND UPDATEXML", // XML-Update für Fehler-basierte Angriffe (MySQL)
        "' AND EXP(~(SELECT", // Exponential-Funktion für Fehler-Erzeugung
        
        // Stacked Queries (mehrere SQL-Befehle in einer Anfrage)
        "'; DROP TABLE", // Tabelle löschen nach Haupt-Query
        "'; DELETE FROM", // Daten löschen nach Haupt-Query
        "'; INSERT INTO", // Daten einfügen nach Haupt-Query
        "'; UPDATE", // Daten aktualisieren nach Haupt-Query
        
        // Häufige Payloads (oft verwendete Angriffs-Kombinationen)
        "1' OR '1'='1", // Zahlen-String-Kombination mit Immer-wahr-Bedingung
        "admin' OR '1'='1'--", // Admin-Login mit Immer-wahr-Bedingung
        "x' OR 1=1 OR 'x'='y", // Komplexe OR-Bedingung
        "' OR ''='", // Leerer String-Vergleich
        "1' OR 1=1#", // Zahlen-basierte Bedingung mit Hash-Kommentar
        "' OR 1=1 LIMIT 1--", // Begrenzte Ergebnis-Menge mit Kommentar
        
        // NoSQL Injection (für NoSQL-Datenbanken wie MongoDB)
        "' || '1'=='1", // JavaScript-ähnliche Immer-wahr-Bedingung
        "' && '1'=='1", // JavaScript-ähnliche UND-Bedingung
        "$ne", // MongoDB "not equal" Operator
        "$gt", // MongoDB "greater than" Operator
        "$where", // MongoDB JavaScript-Ausführung
        
        // Weitere verdächtige Muster (gefährliche SQL-Befehle)
        "SELECT * FROM", // Alle Daten aus Tabelle auswählen
        "DROP DATABASE", // Komplette Datenbank löschen
        "EXEC xp_cmdshell", // System-Befehle ausführen (SQL Server)
        "LOAD_FILE(", // Dateien vom Server laden (MySQL)
        "INTO OUTFILE", // Daten in Datei schreiben (MySQL)
        "BENCHMARK(", // Performance-Tests für Zeit-basierte Angriffe
        "pg_sleep(" // Schlaf-Funktion für PostgreSQL
    )); // Ende der SQL-Injection-Muster-Liste
    
    // Statische Variable: Set für verdächtige IP-Adressen (erste Warnung)
    private static final Set<String> SUSPICIOUS_IPS = new HashSet<>();
    
    // Statische Variable: Set für gesperrte IP-Adressen (zweite Warnung)
    private static final Set<String> BLOCKED_IPS = new HashSet<>();
    
    /**
     * Überprüft eine Eingabe auf SQL-Injection-Muster
     * 
     * @param input Die zu überprüfende Eingabe
     * @return true wenn verdächtige Muster gefunden wurden
     */
    public static boolean checkForSQLInjection(String input) { // Öffentliche statische Methode für SQL-Injection-Prüfung
        // Null-Check und Leerstring-Prüfung
        if (input == null || input.trim().isEmpty()) { // Wenn Eingabe null oder leer ist
            return false; // Keine Bedrohung - gib false zurück
        }
        
        // Eingabe in Großbuchstaben für case-insensitive Vergleich
        String upperInput = input.toUpperCase().trim(); // Konvertiere zu Großbuchstaben für einheitlichen Vergleich
        
        // Prüfe gegen alle bekannten SQL-Injection-Muster
        for (String pattern : SQL_INJECTION_PATTERNS) { // Durchlaufe jedes SQL-Injection-Muster
            if (upperInput.contains(pattern.toUpperCase())) { // Wenn Eingabe das Muster enthält
                handleSuspiciousActivity(input, pattern); // Behandle verdächtige Aktivität
                return true; // Bedrohung erkannt - gib true zurück
            }
        } // Ende der Muster-Schleife
        
        return false; // Keine Bedrohung gefunden - gib false zurück
    }
    
    /**
     * Behandelt verdächtige Aktivitäten
     * 
     * @param input Die verdächtige Eingabe
     * @param pattern Das erkannte Muster
     */
    private static void handleSuspiciousActivity(String input, String pattern) { // Private Methode für Behandlung verdächtiger Aktivitäten
        String clientIP = getClientIP(); // Hole die IP-Adresse des Clients
        
        // Ausgabe der Sicherheitswarnung auf Fehler-Stream (stderr)
        System.err.println("\n" + "=".repeat(60)); // Trennlinie mit 60 Gleichheitszeichen
        System.err.println("🚨 SICHERHEITSWARNUNG: SQL-INJECTION ERKANNT! 🚨"); // Hauptwarnung
        System.err.println("=".repeat(60)); // Weitere Trennlinie
        System.err.println("Verdächtige Eingabe: " + input); // Zeige die verdächtige Eingabe
        System.err.println("Erkanntes Muster: " + pattern); // Zeige das erkannte SQL-Injection-Muster
        System.err.println("Ihre IP-Adresse: " + clientIP); // Zeige die IP-Adresse des Angreifers
        System.err.println("Zeitpunkt: " + new Date()); // Zeige aktuelles Datum und Uhrzeit
        
        // Prüfe ob IP bereits verdächtig ist (zweiter Versuch)
        if (SUSPICIOUS_IPS.contains(clientIP)) { // Wenn IP bereits in der Verdächtigen-Liste steht
            // Zweite Warnung - IP wird gesperrt
            BLOCKED_IPS.add(clientIP); // Füge IP zur Sperrliste hinzu
            System.err.println("\n❌ IHRE IP-ADRESSE WURDE GESPERRT!"); // Sperrung-Nachricht
            System.err.println("Grund: Wiederholte SQL-Injection-Versuche"); // Grund für Sperrung
            System.err.println("Kontaktieren Sie den Administrator für Entsperrung."); // Anweisung für Entsperrung
        } else { // Wenn IP noch nicht verdächtig war (erster Versuch)
            // Erste Warnung
            SUSPICIOUS_IPS.add(clientIP); // Füge IP zur Verdächtigen-Liste hinzu
            System.err.println("\n⚠️  WARNUNG: Beim nächsten Hack-Versuch wird Ihre IP gesperrt!"); // Erste Warnung
            System.err.println("Diese Aktivität wurde protokolliert und gemeldet."); // Protokollierung-Hinweis
        } // Ende der IP-Status-Prüfung
        
        System.err.println("=".repeat(60)); // Abschließende Trennlinie
        System.err.println(); // Leerzeile für bessere Lesbarkeit
    }
    
    /**
     * Überprüft ob eine IP-Adresse gesperrt ist
     * 
     * @return true wenn die aktuelle IP gesperrt ist
     */
    public static boolean isCurrentIPBlocked() { // Öffentliche statische Methode zur IP-Sperr-Prüfung
        String clientIP = getClientIP(); // Hole die aktuelle Client-IP-Adresse
        return BLOCKED_IPS.contains(clientIP); // Prüfe ob IP in der Sperrliste enthalten ist
    }
    
    /**
     * Ermittelt die Client-IP-Adresse
     * 
     * @return Die IP-Adresse als String
     */
    private static String getClientIP() { // Private Methode zur IP-Adress-Ermittlung
        try { // Versuche IP-Adresse zu ermitteln
            // In einer echten Anwendung würde man die echte Client-IP ermitteln
            // Hier simulieren wir mit der lokalen IP
            InetAddress localHost = InetAddress.getLocalHost(); // Hole lokale Host-Adresse
            return localHost.getHostAddress(); // Gib IP-Adresse als String zurück
        } catch (UnknownHostException e) { // Falls Netzwerk-Fehler auftritt
            return "127.0.0.1"; // Fallback auf localhost (127.0.0.1)
        } // Ende des try-catch-Blocks
    }
    
    /**
     * Gibt Statistiken über verdächtige Aktivitäten aus
     */
    public static void printSecurityStats() { // Öffentliche statische Methode für Sicherheitsstatistiken
        System.out.println("\n📊 SICHERHEITSSTATISTIKEN:"); // Überschrift für Statistiken
        System.out.println("Verdächtige IPs (1. Warnung): " + SUSPICIOUS_IPS.size()); // Anzahl verdächtiger IPs
        System.out.println("Gesperrte IPs: " + BLOCKED_IPS.size()); // Anzahl gesperrter IPs
        
        // Zeige verdächtige IPs nur wenn vorhanden
        if (!SUSPICIOUS_IPS.isEmpty()) { // Wenn verdächtige IPs existieren
            System.out.println("Verdächtige IPs: " + SUSPICIOUS_IPS); // Liste der verdächtigen IPs
        }
        
        // Zeige gesperrte IPs nur wenn vorhanden
        if (!BLOCKED_IPS.isEmpty()) { // Wenn gesperrte IPs existieren
            System.out.println("Gesperrte IPs: " + BLOCKED_IPS); // Liste der gesperrten IPs
        }
    } // Ende der printSecurityStats Methode
    
    /**
     * Entsperrt eine IP-Adresse (nur für Administratoren)
     * 
     * @param ipAddress Die zu entsperrende IP-Adresse
     */
    public static void unblockIP(String ipAddress) { // Öffentliche statische Methode für IP-Entsperrung
        BLOCKED_IPS.remove(ipAddress); // Entferne IP aus der Sperrliste
        SUSPICIOUS_IPS.remove(ipAddress); // Entferne IP aus der Verdächtigen-Liste
        System.out.println("✅ IP-Adresse " + ipAddress + " wurde entsperrt."); // Bestätigungsnachricht
    } // Ende der unblockIP Methode
} // Ende der SecurityMonitor Klasse
