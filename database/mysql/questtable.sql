CREATE DATABASE IF NOT EXISTS questtable;

USE questtable;

CREATE TABLE IF NOT EXISTS utenti (
    username VARCHAR(50) PRIMARY KEY,
    parola_accesso VARCHAR(100) NOT NULL,
    ruolo VARCHAR(20) NOT NULL,
    punti_fedelta INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS sessioni_tavolo (
    id_tavolo INT PRIMARY KEY,
    titolo_gioco VARCHAR(100) NOT NULL,
    percorso_immagine VARCHAR(255) NOT NULL,
    posti_totali INT NOT NULL,
    posti_disponibili INT NOT NULL,
    giorno_settimana VARCHAR(20) NOT NULL,
    fascia_oraria VARCHAR(30) NOT NULL,
    quota DECIMAL(6,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS prenotazioni (
    id_prenotazione INT PRIMARY KEY,
    username_cliente VARCHAR(50) NOT NULL,
    id_tavolo INT NOT NULL,
    posti_prenotati INT NOT NULL,
    importo_totale DECIMAL(7,2) NOT NULL,
    data_prenotazione DATE NOT NULL,
    ora_prenotazione TIME NOT NULL,
    stato VARCHAR(20) NOT NULL,
    FOREIGN KEY (username_cliente) REFERENCES utenti(username),
    FOREIGN KEY (id_tavolo) REFERENCES sessioni_tavolo(id_tavolo)
);

INSERT INTO utenti (username, parola_accesso, ruolo, punti_fedelta)
VALUES
    ('matteo', '1234', 'CLIENTE', 0),
    ('admin', 'admin', 'GESTORE', 0)
ON DUPLICATE KEY UPDATE
    parola_accesso = VALUES(parola_accesso),
    ruolo = VALUES(ruolo),
    punti_fedelta = VALUES(punti_fedelta);

INSERT INTO sessioni_tavolo (
    id_tavolo,
    titolo_gioco,
    percorso_immagine,
    posti_totali,
    posti_disponibili,
    giorno_settimana,
    fascia_oraria,
    quota
)
VALUES
    (1, 'Catan', '/com/questtable/images/giochi/catan.png', 4, 3, 'GIOVEDI', '18:00 - 20:00', 12.00),
    (2, 'Catan', '/com/questtable/images/giochi/catan.png', 4, 2, 'SABATO', '21:00 - 23:00', 12.00),
    (3, 'Ticket to Ride', '/com/questtable/images/giochi/ticket-to-ride.png', 5, 1, 'VENERDI', '19:00 - 21:00', 10.00),
    (4, 'Pandemic', '/com/questtable/images/giochi/pandemic.png', 4, 0, 'DOMENICA', '17:00 - 19:00', 8.00)
ON DUPLICATE KEY UPDATE
    titolo_gioco = VALUES(titolo_gioco),
    percorso_immagine = VALUES(percorso_immagine),
    posti_totali = VALUES(posti_totali),
    posti_disponibili = VALUES(posti_disponibili),
    giorno_settimana = VALUES(giorno_settimana),
    fascia_oraria = VALUES(fascia_oraria),
    quota = VALUES(quota);

INSERT INTO prenotazioni (
    id_prenotazione,
    username_cliente,
    id_tavolo,
    posti_prenotati,
    importo_totale,
    data_prenotazione,
    ora_prenotazione,
    stato
)
VALUES
    (1, 'matteo', 1, 1, 12.00, CURRENT_DATE(), '10:00:00', 'IN_ATTESA'),
    (2, 'matteo', 3, 2, 20.00, CURRENT_DATE(), '10:05:00', 'IN_ATTESA')
ON DUPLICATE KEY UPDATE
    username_cliente = VALUES(username_cliente),
    id_tavolo = VALUES(id_tavolo),
    posti_prenotati = VALUES(posti_prenotati),
    importo_totale = VALUES(importo_totale),
    data_prenotazione = VALUES(data_prenotazione),
    ora_prenotazione = VALUES(ora_prenotazione),
    stato = VALUES(stato);
