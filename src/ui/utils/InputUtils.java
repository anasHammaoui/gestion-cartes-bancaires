package ui.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtils {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String lireChaine(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    public static int lireEntier(String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Erreur: Veuillez entrer un nombre valide.");
            }
        }
    }

    public static double lireDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Erreur: Veuillez entrer un nombre décimal valide.");
            }
        }
    }

    public static float lireFloat(String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = scanner.nextLine().trim();
                return Float.parseFloat(input);
            } catch (NumberFormatException e) {
                System.out.println("Erreur: Veuillez entrer un nombre décimal valide.");
            }
        }
    }

    public static LocalDateTime lireDate(String message) {
        while (true) {
            try {
                System.out.print(message + " (format: dd/MM/yyyy HH:mm): ");
                String input = scanner.nextLine().trim();
                return LocalDateTime.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Erreur: Format de date invalide. Utilisez dd/MM/yyyy HH:mm");
            }
        }
    }

    public static boolean confirmer(String message) {
        System.out.print(message + " (o/n): ");
        String reponse = scanner.nextLine().trim().toLowerCase();
        return reponse.equals("o") || reponse.equals("oui") || reponse.equals("y") || reponse.equals("yes");
    }

    public static void afficherMessage(String message) {
        System.out.println("\n" + message);
    }

    public static void afficherSucces(String message) {
        System.out.println("\n[SUCCESS] " + message);
    }

    public static void afficherErreur(String message) {
        System.out.println("\n[ERROR] " + message);
    }

    public static void afficherInfo(String message) {
        System.out.println("\n[INFO] " + message);
    }

    public static void pause() {
        System.out.println("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }
}