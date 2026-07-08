package com.questtable.main;

import com.questtable.config.PersistenceConfig;
import com.questtable.dao.DAOFactory;

import java.util.Scanner;

public class AvvioQuestTable {
    private static final int SCELTA_JAVAFX = 1;
    private static final int SCELTA_CLI = 2;
    private static final int SCELTA_DEMO = 1;
    private static final int SCELTA_FILE_SYSTEM = 2;
    private static final int SCELTA_MYSQL = 3;

    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        AvvioQuestTable avvioQuestTable = new AvvioQuestTable();
        avvioQuestTable.avvia();
    }

    private void avvia() {
        configuraPersistenza();
        avviaInterfaccia();
    }

    private void configuraPersistenza() {
        boolean sceltaValida = false;

        while (!sceltaValida) {
            mostraMenuPersistenza();
            int scelta = leggiScelta();

            switch (scelta) {
                case SCELTA_DEMO -> {
                    PersistenceConfig.configuraTipoPersistenza(DAOFactory.DEMO);
                    sceltaValida = true;
                }
                case SCELTA_FILE_SYSTEM -> {
                    PersistenceConfig.configuraTipoPersistenza(DAOFactory.FILE_SYSTEM);
                    sceltaValida = true;
                }
                case SCELTA_MYSQL -> {
                    PersistenceConfig.configuraTipoPersistenza(DAOFactory.MYSQL);
                    sceltaValida = true;
                }
                default -> mostraSceltaNonValida();
            }
        }
    }

    private void avviaInterfaccia() {
        boolean sceltaValida = false;

        while (!sceltaValida) {
            mostraMenuInterfaccia();
            int scelta = leggiScelta();

            switch (scelta) {
                case SCELTA_JAVAFX -> {
                    sceltaValida = true;
                    AvvioJavaFX.avvia();
                }
                case SCELTA_CLI -> {
                    sceltaValida = true;
                    AvvioCli.avvia(scanner);
                }
                default -> mostraSceltaNonValida();
            }
        }
    }

    private void mostraMenuPersistenza() {
        stampaSeparatore();
        System.out.println("Seleziona il livello di persistenza");
        System.out.println("1. Demo");
        System.out.println("2. File system");
        System.out.println("3. MySQL");
    }

    private void mostraMenuInterfaccia() {
        stampaSeparatore();
        System.out.println("Seleziona l'interfaccia utente");
        System.out.println("1. JavaFX");
        System.out.println("2. CLI");
    }

    private int leggiScelta() {
        System.out.print("Scelta: ");
        String valoreInserito = scanner.nextLine();

        try {
            return Integer.parseInt(valoreInserito.trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private void mostraSceltaNonValida() {
        System.out.println("Scelta non valida.");
    }

    private void stampaSeparatore() {
        System.out.println();
        System.out.println("========================================");
    }
}
