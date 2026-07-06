package com.questtable.dao.file_system;

import java.nio.file.Path;
import java.nio.file.Paths;

final class FileSystemPaths {
    static final Path CARTELLA_DATI = Paths.get("data", "file_system");
    static final Path FILE_UTENTI = CARTELLA_DATI.resolve("utenti.csv");
    static final Path FILE_TAVOLI = CARTELLA_DATI.resolve("tavoli.csv");
    static final Path FILE_PRENOTAZIONI = CARTELLA_DATI.resolve("prenotazioni.csv");

    private FileSystemPaths() {
    }
}
