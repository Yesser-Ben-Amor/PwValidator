
package org.example; // Paket-Deklaration: Definiert zu welchem Paket diese Klasse gehört

/**
 * TDD Tests für den Passwort-Validator
 * Diese Klasse enthält alle Tests für die Test-Driven Development Methodik
 */
public class PasswordValidatorTest { // Öffentliche Klasse für die Tests

    // Hauptmethode - Einstiegspunkt des Programms
    public static void main(String[] args) {
        // Ausgabe einer Überschrift für die aktuelle Testphase
        System.out.println("🔴 RED Phase: Testing hasMinLength...");
        // Aufruf der Test-Methode für die Mindestlängen-Prüfung
        testHasMinLength();
    }
    
    // Methode zum Testen der hasMinLength-Funktionalität
    public static void testHasMinLength() {
        // Kommentar: In der RED Phase sollen Tests zunächst fehlschlagen
        
        // TEST 1: Grenzfall mit 7 Zeichen (zu kurz für Mindestlänge 8)
        // Erwartet: false (daher Negation mit !)
        test("7 chars should be false", !PasswordValidator.hasMinLength("1234567", 8));
        
        // TEST 2: Grenzfall mit exakt 8 Zeichen (genau die Mindestlänge)
        // Erwartet: true
        test("8 chars should be true", PasswordValidator.hasMinLength("12345678", 8));
        
        // TEST 3: Grenzfall mit 9 Zeichen (länger als Mindestlänge)
        // Erwartet: true
        test("9 chars should be true", PasswordValidator.hasMinLength("123456789", 8));
        
        // TEST 4: Leerer String (0 Zeichen) bei Mindestlänge 8
        // Erwartet: false (daher Negation mit !)
        test("Empty string should be false", !PasswordValidator.hasMinLength("", 8));
        
        // TEST 5: Null-Wert als Eingabe
        // Erwartet: false (daher Negation mit !)
        test("Null should be false", !PasswordValidator.hasMinLength(null, 8));
        
        // TEST 6: Mindestlänge 0 mit 4-Zeichen-String "test"
        // Erwartet: true (jeder String ist länger als 0)
        test("Min length 0 should be true", PasswordValidator.hasMinLength("test", 0));
    }
    
    // Hilfsmethode zum Ausführen und Anzeigen von Testergebnissen
    private static void test(String description, boolean condition) {
        // Ternärer Operator: wenn condition true ist, zeige ✅ PASS, sonst ❌ FAIL
        // Dann füge die Beschreibung hinzu und gib alles aus
        System.out.println((condition ? "✅ PASS" : "❌ FAIL") + ": " + description);
    }
}
