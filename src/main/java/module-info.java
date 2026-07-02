module com.questtable {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.questtable to javafx.fxml;
    exports com.questtable;
    exports com.questtable.bean;
    exports com.questtable.config;
    exports com.questtable.controller;
    exports com.questtable.dao;
    exports com.questtable.model;
    exports com.questtable.session;

    opens com.questtable.view.controller to javafx.fxml;
    exports com.questtable.view.controller;
}
