package org.example;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Sicherheitsmonitor für SQL-Injection-Erkennung
 * Überwacht verdächtige Eingaben und trackt IP-Adressen
 */
public class SecurityMonitor {
    
    // Liste der häufigsten SQL-Injection-Ausdrücke
    private static final Set<String> SQL_INJECTION_PATTERNS = new HashSet<>(Arrays.asList(
        // Klassische SQL-Injection
        "' OR '1'='1",
        "' OR 1=1--",
        "' OR 1=1#",
        "' OR 1=1/*",
        "admin'--",
        "admin'#",
        "admin'/*",
        "' OR 'x'='x",
        "' OR 'a'='a",
        "') OR ('1'='1",
        
        // Union-basierte Injections
        "' UNION SELECT",
        "' UNION ALL SELECT",
        "UNION SELECT NULL",
        "UNION SELECT 1,2,3",
        
        // Blind SQL Injection
        "' AND (SELECT SUBSTRING",
        "' AND ASCII(SUBSTRING",
        "' AND LENGTH(",
        "' WAITFOR DELAY",
        "' AND SLEEP(5)",
        
        // Error-basierte Injection
        "' AND EXTRACTVALUE",
        "' AND UPDATEXML",
        "' AND EXP(~(SELECT",
        
        // Stacked Queries
        "'; DROP TABLE",
        "'; DELETE FROM",
        "'; INSERT INTO",
        "'; UPDATE",
        
        // Häufige Payloads
        "1' OR '1'='1",
        "admin' OR '1'='1'--",
        "x' OR 1=1 OR 'x'='y",
        "' OR ''='",
        "1' OR 1=1#",
        "' OR 1=1 LIMIT 1--",
        
        // NoSQL Injection
        "' || '1'=='1",
        "' && '1'=='1",
        "$ne",
        "$gt",
        "$where",
        
        // Weitere verdächtige Muster
        "SELECT * FROM",
        "DROP DATABASE",
        "EXEC xp_cmdshell",
        "LOAD_FILE(",
        "INTO OUTFILE",
        "BENCHMARK(",
        "pg_sleep("
    ));
    
    // Set für verdächtige IP-Adressen (erste Warnung)
    private static final Set<String> SUSPICIOUS_IPS = new HashSet<>();
    
    // Set für gesperrte IP-Adressen (zweite Warnung)
    private static final Set<String> BLOCKED_IPS = new HashSet<>();
    
    /**
     * Überprüft eine Eingabe auf SQL-Injection-Muster
     * 
     * @param input Die zu überprüfende Eingabe
     * @return true wenn verdächtige Muster gefunden wurden
     */
    public static boolean checkForSQLInjection(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        
        // Eingabe in Großbuchstaben für case-insensitive Vergleich
        String upperInput = input.toUpperCase().trim();
        
        // Prüfe gegen alle bekannten SQL-Injection-Muster
        for (String pattern : SQL_INJECTION_PATTERNS) {
            if (upperInput.contains(pattern.toUpperCase())) {
                handleSuspiciousActivity(input, pattern);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Behandelt verdächtige Aktivitäten
     * 
     * @param input Die verdächtige Eingabe
     * @param pattern Das erkannte Muster
     */
    private static void handleSuspiciousActivity(String input, String pattern) {
        String clientIP = getClientIP();
        
        System.err.println("\n" + "=".repeat(60));
        System.err.println("🚨 SICHERHEITSWARNUNG: SQL-INJECTION ERKANNT! 🚨");
        System.err.println("=".repeat(60));
        System.err.println("Verdächtige Eingabe: " + input);
        System.err.println("Erkanntes Muster: " + pattern);
        System.err.println("Ihre IP-Adresse: " + clientIP);
        System.err.println("Zeitpunkt: " + new Date());
        
        if (SUSPICIOUS_IPS.contains(clientIP)) {
            // Zweite Warnung - IP wird gesperrt
            BLOCKED_IPS.add(clientIP);
            System.err.println("\n❌ IHRE IP-ADRESSE WURDE GESPERRT!");
            System.err.println("Grund: Wiederholte SQL-Injection-Versuche");
            System.err.println("Kontaktieren Sie den Administrator für Entsperrung.");
        } else {
            // Erste Warnung
            SUSPICIOUS_IPS.add(clientIP);
            System.err.println("\n⚠️  WARNUNG: Beim nächsten Hack-Versuch wird Ihre IP gesperrt!");
            System.err.println("Diese Aktivität wurde protokolliert und gemeldet.");
        }
        
        System.err.println("=".repeat(60));
        System.err.println();
    }
    
    /**
     * Überprüft ob eine IP-Adresse gesperrt ist
     * 
     * @return true wenn die aktuelle IP gesperrt ist
     */
    public static boolean isCurrentIPBlocked() {
        String clientIP = getClientIP();
        return BLOCKED_IPS.contains(clientIP);
    }
    
    /**
     * Ermittelt die Client-IP-Adresse
     * 
     * @return Die IP-Adresse als String
     */
    private static String getClientIP() {
        try {
            // In einer echten Anwendung würde man die echte Client-IP ermitteln
            // Hier simulieren wir mit der lokalen IP
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1"; // Fallback auf localhost
        }
    }
    
    /**
     * Gibt Statistiken über verdächtige Aktivitäten aus
     */
    public static void printSecurityStats() {
        System.out.println("\n📊 SICHERHEITSSTATISTIKEN:");
        System.out.println("Verdächtige IPs (1. Warnung): " + SUSPICIOUS_IPS.size());
        System.out.println("Gesperrte IPs: " + BLOCKED_IPS.size());
        
        if (!SUSPICIOUS_IPS.isEmpty()) {
            System.out.println("Verdächtige IPs: " + SUSPICIOUS_IPS);
        }
        
        if (!BLOCKED_IPS.isEmpty()) {
            System.out.println("Gesperrte IPs: " + BLOCKED_IPS);
        }
    }
    
    /**
     * Entsperrt eine IP-Adresse (nur für Administratoren)
     * 
     * @param ipAddress Die zu entsperrende IP-Adresse
     */
    public static void unblockIP(String ipAddress) {
        BLOCKED_IPS.remove(ipAddress);
        SUSPICIOUS_IPS.remove(ipAddress);
        System.out.println("✅ IP-Adresse " + ipAddress + " wurde entsperrt.");
    }
}
