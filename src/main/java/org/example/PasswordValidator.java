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
    
    /**
     * Überprüft ob das Passwort mindestens eine ASCII-Ziffer (0-9) enthält
     * 
     * @param password Das zu prüfende Passwort (darf null sein)
     * @return true wenn das Passwort mindestens eine ASCII-Ziffer enthält, false bei null oder ohne Ziffern
     */
    public static boolean containsDigit(String password) {
        // Null-Check: null-Passwort enthält keine Ziffern
        if (password == null) {
            return false;
        }
        
        // Durchsuche jedes Zeichen nach ASCII-Ziffern (0-9)
        for (char character : password.toCharArray()) {
            if (isAsciiDigit(character)) {
                return true; // Erste Ziffer gefunden - sofort zurückgeben
            }
        }
        
        // Keine Ziffer gefunden
        return false;
    }
    
    /**
     * Hilfsmethode: Prüft ob ein Zeichen eine ASCII-Ziffer ist
     * 
     * @param c Das zu prüfende Zeichen
     * @return true wenn c eine ASCII-Ziffer (0-9) ist
     */
    private static boolean isAsciiDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
