
package org.example; // Paket-Deklaration: Definiert zu welchem Paket diese Klasse gehört

/**
 * TDD Tests für den Passwort-Validator
 * Diese Klasse enthält alle Tests für die Test-Driven Development Methodik
 */
public class PasswordValidatorTest { // Öffentliche Klasse für die Tests

    // Hauptmethode - Einstiegspunkt des Programms
    public static void main(String[] args) {
        // Test 1: Mindestlänge
        System.out.println("🔴 RED Phase: Testing hasMinLength...");
        testHasMinLength();
        
        System.out.println("\n" + "=".repeat(50));
        
        // Test 2: Ziffer enthalten
        System.out.println("🔴 RED Phase: Testing containsDigit...");
        testContainsDigit();
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
    
    // Methode zum Testen der containsDigit-Funktionalität
    public static void testContainsDigit() {
        // Kommentar: In der RED Phase sollen Tests zunächst fehlschlagen
        
        // TEST 1: Keine Ziffer im Passwort
        // Erwartet: false (daher Negation mit !)
        test("No digit should be false", !PasswordValidator.containsDigit("abcdef"));
        
        // TEST 2: Genau eine Ziffer am Anfang
        // Erwartet: true
        test("One digit at start should be true", PasswordValidator.containsDigit("1abcdef"));
        
        // TEST 3: Genau eine Ziffer in der Mitte
        // Erwartet: true
        test("One digit in middle should be true", PasswordValidator.containsDigit("abc5def"));
        
        // TEST 4: Genau eine Ziffer am Ende
        // Erwartet: true
        test("One digit at end should be true", PasswordValidator.containsDigit("abcdef9"));
        
        // TEST 5: Mehrere Ziffern
        // Erwartet: true
        test("Multiple digits should be true", PasswordValidator.containsDigit("a1b2c3"));
        
        // TEST 6: Nur Ziffern
        // Erwartet: true
        test("Only digits should be true", PasswordValidator.containsDigit("123456"));
        
        // TEST 7: Leerer String
        // Erwartet: false (daher Negation mit !)
        test("Empty string should be false", !PasswordValidator.containsDigit(""));
        
        // TEST 8: Null-Wert
        // Erwartet: false (daher Negation mit !)
        test("Null should be false", !PasswordValidator.containsDigit(null));
        
        // TEST 9: Unicode-Zeichen (keine ASCII-Ziffern)
        // Erwartet: false - wir ignorieren Unicode-Ziffern wie ①②③
        test("Unicode chars should be false", !PasswordValidator.containsDigit("abc①def"));
    }
    
    // Hilfsmethode zum Ausführen und Anzeigen von Testergebnissen
    private static void test(String description, boolean condition) {
        // Ternärer Operator: wenn condition true ist, zeige ✅ PASS, sonst ❌ FAIL
        // Dann füge die Beschreibung hinzu und gib alles aus
        System.out.println((condition ? "✅ PASS" : "❌ FAIL") + ": " + description);
    }
}
