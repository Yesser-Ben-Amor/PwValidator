package org.example;

/**
 * Test-Demo für das SQL-Injection-Erkennungssystem
 */
public class SecurityTest {
    
    public static void main(String[] args) {
        System.out.println("🔒 SQL-INJECTION-ERKENNUNGSSYSTEM TEST");
        System.out.println("=" .repeat(50));
        
        // Test 1: Normales Passwort (sollte OK sein)
        testPassword("MeinSicheresPasswort123!");
        
        // Test 2: Erste SQL-Injection (erste Warnung)
        testPassword("' OR '1'='1");
        
        // Test 3: Zweite SQL-Injection (IP wird gesperrt)
        testPassword("admin'--");
        
        // Test 4: Nach Sperrung (sollte blockiert werden)
        if (SecurityMonitor.isCurrentIPBlocked()) {
            System.out.println("\n🚫 IP ist gesperrt - weitere Tests nicht möglich");
        }
        
        // Statistiken anzeigen
        SecurityMonitor.printSecurityStats();
    }
    
    private static void testPassword(String password) {
        System.out.println("\n🧪 Teste Passwort: " + password);
        
        if (SecurityMonitor.checkForSQLInjection(password)) {
            System.out.println("❌ SQL-Injection erkannt!");
        } else {
            System.out.println("✅ Passwort ist sicher");
        }
        
        if (SecurityMonitor.isCurrentIPBlocked()) {
            System.out.println("🚫 IP wurde gesperrt!");
        }
    }
}
