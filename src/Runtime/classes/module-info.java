/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2SEModule/module-info.java to edit this template
 */

module Runtime {
    requires Installer;
    requires Commons;
    requires java.sql;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires Utils;

    exports theopenhand.window.resources.handler to Program;
    exports theopenhand.window.resources.ui to Program;
    exports theopenhand.window.resources.ui.settings.plugins to Program;

    exports theopenhand.runtime.loader to Program;
    exports theopenhand.runtime.loader.folder to Program;
    opens theopenhand.runtime.loader.folder.xml to Utils;
    opens theopenhand.runtime.ambient.xml to Utils;
    opens theopenhand.window.resources.ui to javafx.graphics, javafx.fxml;
    opens theopenhand.window.resources.ui.settings.plugins to javafx.graphics, javafx.fxml;
}
