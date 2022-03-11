/*
 * Copyright 2022 gabri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package theopenhand.installer.graphics;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.window.graphics.creators.DialogCreator;

/**
 *
 * @author gabri
 */
public class ConnectionSetup extends AnchorPane implements DialogComponent {

    @FXML
    private TextField addressTF;

    @FXML
    private Button connectBTN;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField paramsTF;

    @FXML
    private PasswordField passwordPF;

    @FXML
    private TextField portTF;

    @FXML
    private Button quitBTN;

    @FXML
    private TextField usernameTF;

    private ClickListener on_acc;

    public ConnectionSetup() {
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/installer/graphics/ConnectionSetup.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionSetup.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectBTN.setOnAction(a -> {
            tryConnection();
        });
    }

    public String getAddress() {
        return addressTF.getText();
    }

    public String getDatabaseName() {
        return nameTF.getText();
    }

    public String getPort() {
        return portTF.getText();
    }

    public String getUsername() {
        return usernameTF.getText();
    }

    public String getPassword() {
        return passwordPF.getText();
    }

    public String getParameters() {
        return paramsTF.getText();
    }

    public void setup(String addr, String db_name, String port, String user, String psw, String params) {
        addressTF.setText(addr);
        nameTF.setText(db_name);
        portTF.setText(port);
        usernameTF.setText(user);
        passwordPF.setText(psw);
        paramsTF.setText(params);
    }

    public void tryConnection() {
        String error = null;
        String addr = getAddress();
        String dbn = getDatabaseName();
        String port = getPort();
        String params = getParameters();
        String username = getUsername();
        String password = getPassword();
        if (addr == null || addr.isBlank()) {
            error = "L'indirizzo non è valido.";
        } else if (dbn == null || dbn.isBlank()) {
            error = "Il nome del database non è valido.";
        } else if (port == null || port.isBlank()) {
            error = "La porta di connessione non è valida.";
        } else if (username == null || username.isBlank()) {
            error = "Accesso anonimo non consentito: inserisci nome utente.";
        } else if (password == null || password.isBlank()) {
            error = "Inserisci una password.";
        }

        if (error == null) {
            try {
                Connection DBConn = java.sql.DriverManager.getConnection("jdbc:mysql://" + addr + ":" + port + "/" + dbn + (params == null || params.isBlank() ? "" : "?" + params), username, password);
                if (DBConn != null) {
                    if (on_acc != null) {
                        on_acc.onClick();
                    }
                }
            } catch (java.sql.SQLException ex) {
                DialogCreator.showAlert(Alert.AlertType.ERROR, "Impossibile connettersi", "Non è stato possibile connettersi al database selezionato.\nRicontrollare i dati di acceso.", null);
            } catch (RuntimeException e) {

            }
        } else {
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Dati incompleti", error, null);
        }
    }

    @Override
    public double getDialogWidth() {
        return getPrefWidth();
    }

    @Override
    public double getDialogHeight() {
        return getPrefHeight();
    }

    @Override
    public String getTitle() {
        return "Proprietà connessione";
    }

    @Override
    public Parent getParentNode() {
        return this;
    }

    @Override
    public void onExitPressed(ClickListener cl) {
        quitBTN.setOnAction((a) -> {
            if (cl != null) {
                cl.onClick();
            }
        });
    }

    @Override
    public void onAcceptPressed(ClickListener cl) {
        on_acc = cl;
    }

}
