package com.questtable.main;

import com.questtable.view.cli.controller.SchermataHomeCliController;

import java.util.Scanner;

public class AvvioCli {

    public static void avvia(Scanner scanner) {
        SchermataHomeCliController schermataHomeCliController = new SchermataHomeCliController(scanner);
        schermataHomeCliController.avvia();
    }
}
