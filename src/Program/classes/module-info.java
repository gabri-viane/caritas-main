/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2SEModule/module-info.java to edit this template
 */

module Program {
    requires Installer;
    requires Commons;
    requires Runtime;
    requires Utils;

    requires java.logging;
    requires java.naming;
    requires java.sql;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens theopenhand.programm to javafx.graphics, javafx.fxml;
    opens theopenhand.programm.window.plugins to javafx.graphics, javafx.fxml;
    opens theopenhand.programm.window.store to javafx.graphics, javafx.fxml;
    opens theopenhand.programm.window.homepage to javafx.graphics, javafx.fxml;
}
