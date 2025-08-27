package org.example;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Der beste Passwort Validator on the City");
        System.out.println("#################");
        System.out.println("Bitte geben Sie das Passwort ein!");
        System.out.println("(Geben Sie 'exit' ein, um das Programm zu beenden)\n");
        
        while (true) {
            System.out.println("Passwort eingeben");
            String password = scanner.nextLine();
            
            // Abbruch Bedingung
            if(password.equalsIgnoreCase("exit")){
                System.out.println("Tchao cacao");
                break;
            }
            
            // Alle Validierungen aufrufen
            boolean laengeOk = hatMindestLaenge(password, 8);
            boolean hatZiffer = enthaeltZiffer(password);
            boolean hatGrossKlein = enthaeltGrossUndKlein(password);
            boolean istSchwach = istSchwachesPasswort(password);
            boolean hatSonderzeichen = enthaeltSonderzeichen(password);
            int zeichenGruppen = zaehleZeichenGruppen(password);
            
            // Ergebnisse ausgeben
            System.out.println("\n--- Passwort Analyse ---");
            System.out.println("Passwort: " + password);
            System.out.println("Mindestlänge (8 Zeichen): " + (laengeOk ? "✓" : "✗"));
            System.out.println("Enthält Ziffer: " + (hatZiffer ? "✓" : "✗"));
            System.out.println("Groß- und Kleinbuchstaben: " + (hatGrossKlein ? "✓" : "✗"));
            System.out.println("Sonderzeichen: " + (hatSonderzeichen ? "✓" : "✗"));
            System.out.println("Ist schwaches Passwort: " + (istSchwach ? "✗ JA" : "✓ NEIN"));
            System.out.println("Anzahl Zeichengruppen: " + zeichenGruppen + "/4");
            
            // Gesamtbewertung
            if (!istSchwach && laengeOk && hatZiffer && hatGrossKlein && hatSonderzeichen) {
                System.out.println("\n🔒 STARKES PASSWORT! Alle Kriterien erfüllt.");
            } else if (!istSchwach && zeichenGruppen >= 3 && laengeOk) {
                System.out.println("\n🔓 MITTLERES PASSWORT. Könnte stärker sein.");
            } else {
                System.out.println("\n⚠️ SCHWACHES PASSWORT! Bitte verbessern.");
            }
            
            // Trennlinie erstellen (Java 8 kompatibel)
            StringBuilder separator = new StringBuilder();
            for (int i = 0; i < 40; i++) {
                separator.append("=");
            }
            System.out.println("\n" + separator.toString() + "\n");
        }
        scanner.close();
    }

    // Methode 1: Mindestlänge prüfen
    public static boolean hatMindestLaenge(String password, int min) {
        if (password == null) {
            return false;
        }
        return password.length() >= min;
    }

    // Methode 2: Ziffer prüfen
    public static boolean enthaeltZiffer(String password) {
        if (password == null) {
            return false;
        }
        
        char[] chars = password.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c >= '0' && c <= '9') {
                return true;
            }
        }
        return false;
    }

    // Methode 3: Groß- und Kleinbuchstaben prüfen
    public static boolean enthaeltGrossUndKlein(String password) {
        if (password == null) {
            return false;
        }
        
        boolean hasCapital = false;
        boolean hasLowerCase = false;
        char[] chars = password.toCharArray();
        
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c >= 'A' && c <= 'Z') {
                hasCapital = true;
            } else if (c >= 'a' && c <= 'z') {
                hasLowerCase = true;
            }
            
            // Früh beenden wenn beide gefunden
            if (hasCapital && hasLowerCase) {
                return true;
            }
        }
        return hasCapital && hasLowerCase;
    }

    // Methode 4: Schwache Passwörter prüfen
    public static boolean istSchwachesPasswort(String password) {
        if (password == null) {
            return true;
        }

        // Liste schwacher Passwörter
        String[] schwachePasswoerter = {
                "password", "123456", "123456789", "12345678", "12345",
                "1234567", "password123", "admin", "qwerty", "abc123",
                "letmein", "monkey", "1234567890", "dragon", "111111",
                "baseball", "iloveyou", "trustno1", "sunshine", "master",
                "welcome", "shadow", "ashley", "football", "jesus",
                "michael", "ninja", "mustang", "password1", "root", "sudo"
        };

        String kleinPassword = password.toLowerCase();

        for (int i = 0; i < schwachePasswoerter.length; i++) {
            if (kleinPassword.equals(schwachePasswoerter[i])) {
                return true;
            }
        }
        return false;
    }

    // Methode 5: Sonderzeichen prüfen
    public static boolean enthaeltSonderzeichen(String password) {
        if (password == null) {
            return false;
        }

        String erlaubteSonderzeichen = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        char[] passwordChars = password.toCharArray();
        char[] sonderChars = erlaubteSonderzeichen.toCharArray();
        
        for (int i = 0; i < passwordChars.length; i++) {
            char passwordChar = passwordChars[i];
            for (int j = 0; j < sonderChars.length; j++) {
                if (passwordChar == sonderChars[j]) {
                    return true;
                }
            }
        }
        return false;
    }

    // Methode 6: Zeichengruppen zählen
    public static int zaehleZeichenGruppen(String password) {
        if (password == null) {
            return 0;
        }

        int gruppen = 0;

        // Ziffer prüfen
        if (enthaeltZiffer(password)) {
            gruppen++;
        }

        // Großbuchstaben und Kleinbuchstaben separat prüfen
        boolean hasCapital = false;
        boolean hasLowerCase = false;

        char[] chars = password.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c >= 'A' && c <= 'Z') {
                hasCapital = true;
            } else if (c >= 'a' && c <= 'z') {
                hasLowerCase = true;
            }
        }

        if (hasCapital) gruppen++;
        if (hasLowerCase) gruppen++;

        // Sonderzeichen prüfen
        if (enthaeltSonderzeichen(password)) {
            gruppen++;
        }

        return gruppen;
    }
}
