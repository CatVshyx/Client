module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires json;
    requires javafx.swing;
    requires layout;
    requires kernel;
    requires io;
    requires java.net.http;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires javafx.web;

    opens com.example.client to javafx.fxml;
    opens com.example.client.model to javafx.fxml;

    exports com.example.client;
    exports com.example.client.controllers;
    exports com.example.client.model;
    opens com.example.client.controllers to javafx.fxml;
    exports com.example.client.util;
    opens com.example.client.util to javafx.fxml;
    exports com.example.client.ui.cells;
    opens com.example.client.ui.cells to javafx.fxml;
    exports com.example.client.additional;
    opens com.example.client.additional to javafx.fxml;

}