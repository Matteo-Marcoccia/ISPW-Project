module com.questtable {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    exports com.questtable.main;
    exports com.questtable.bean;
    exports com.questtable.config;
    exports com.questtable.controller;
    exports com.questtable.dao;
    exports com.questtable.model;
    exports com.questtable.session;
    opens com.questtable.view.javafx.controller to javafx.fxml;
    exports com.questtable.view.javafx.controller;
}
