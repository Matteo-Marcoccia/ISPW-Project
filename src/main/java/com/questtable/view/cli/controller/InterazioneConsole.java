package com.questtable.view.cli.controller;

import java.util.Scanner;

@SuppressWarnings("java:S106")
public final class InterazioneConsole {

    private InterazioneConsole() {
    }

    public static String leggiTesto(Scanner scanner, String messaggio) {
        stampaRichiesta(messaggio);
        return scanner.nextLine();
    }

    public static int leggiIntero(Scanner scanner, String messaggio) {
        return convertiInIntero(leggiTesto(scanner, messaggio));
    }

    public static int convertiInIntero(String valoreInserito) {
        try {
            return Integer.parseInt(valoreInserito.trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    public static void stampaMessaggio(String messaggio) {
        System.out.println(messaggio);
    }

    public static void stampaSeparatore() {
        stampaRigaVuota();
        System.out.println("========================================");
    }

    public static void stampaSceltaNonValida() {
        stampaMessaggio("Scelta non valida.");
    }

    private static void stampaRichiesta(String messaggio) {
        System.out.print(messaggio);
    }

    private static void stampaRigaVuota() {
        System.out.println();
    }
}
