package org.example;

/**
 * TDD-basierter Passwort-Validator
 */
public class PasswordValidator {
    
    /**
     * Überprüft die Mindestlänge eines Passworts
     * 
     * @param password Das zu prüfende Passwort (darf null sein)
     * @param minLength Die erforderliche Mindestlänge (muss >= 0 sein)
     * @return true wenn das Passwort die Mindestlänge erfüllt, false bei null oder zu kurz
     */
    public static boolean hasMinLength(String password, int minLength) {
        // Null-Check: null-Passwort ist immer ungültig
        if (password == null) {
            return false;
        }
        
        // Längenprüfung: Passwort muss mindestens minLength Zeichen haben
        return password.length() >= minLength;
    }
}
