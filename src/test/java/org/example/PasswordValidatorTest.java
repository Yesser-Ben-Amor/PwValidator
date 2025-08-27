package org.example;

/**
 * TDD Tests für den Passwort-Validator
 */
public class PasswordValidatorTest {

    public static void main(String[] args) {
        System.out.println("🔴 RED Phase: Testing hasMinLength...");
        testHasMinLength();
    }
    
    public static void testHasMinLength() {
        // 🔴 RED: Diese Tests sollen FEHLSCHLAGEN
        
        // Grenzfall: zu kurz (7 Zeichen) - sollte false sein
        test("7 chars should be false", !PasswordValidator.hasMinLength("1234567", 8));
        
        // Grenzfall: genau richtig (8 Zeichen) - sollte true sein
        test("8 chars should be true", PasswordValidator.hasMinLength("12345678", 8));
        
        // Grenzfall: länger (9 Zeichen) - sollte true sein
        test("9 chars should be true", PasswordValidator.hasMinLength("123456789", 8));
        
        // Leerer String - sollte false sein
        test("Empty string should be false", !PasswordValidator.hasMinLength("", 8));
        
        // Null-Wert - sollte false sein
        test("Null should be false", !PasswordValidator.hasMinLength(null, 8));
        
        // Mindestlänge 0 - sollte true sein
        test("Min length 0 should be true", PasswordValidator.hasMinLength("test", 0));
    }
    
    private static void test(String description, boolean condition) {
        System.out.println((condition ? "✅ PASS" : "❌ FAIL") + ": " + description);
    }
}
