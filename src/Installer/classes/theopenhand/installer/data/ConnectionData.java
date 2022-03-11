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
package theopenhand.installer.data;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import theopenhand.commons.connection.DatabaseConnection;
import theopenhand.installer.SetupInit;
import theopenhand.installer.data.xml.connection.AddressElement;
import theopenhand.installer.data.xml.connection.CredentialsElement;
import theopenhand.installer.graphics.ConnectionSetup;
import theopenhand.statics.StaticReferences;
import theopenhand.window.graphics.creators.DialogCreator;
import ttt.utils.xml.document.XMLDocument;
import ttt.utils.xml.engine.XMLEngine;
import ttt.utils.xml.engine.interfaces.IXMLElement;
import ttt.utils.xml.io.XMLWriter;
import ttt.utils.xml.io.base.RootElement;

/**
 *
 * @author gabri
 */
public class ConnectionData {

    private static ConnectionData instance;
    private XMLDocument doc;
    private XMLWriter writer;

    private ConnectionData() {
        init();
    }

    private void init() {
        File f = SetupInit.getInstance().getCONNECTIONS_SETTINGS();
        try {
            writer = new XMLWriter(f);
            doc = new XMLDocument(f);
            XMLEngine eng = new XMLEngine(f, RootElement.class, AddressElement.class, CredentialsElement.class);
            eng.morph(doc);
            initData();
            StaticReferences.subscribeOnExit(() -> {
                flush();
            });
        } catch (IOException ex) {
            Logger.getLogger(ProgrammData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void flush() {
        writer.writeDocument(doc, true);
    }

    public static ConnectionData getInstance() {
        if (instance == null) {
            instance = new ConnectionData();
        }
        return instance;
    }

    private void initData() {
        IXMLElement el = doc.getRoot().getFirstElement("address");
        IXMLElement cr = doc.getRoot().getFirstElement("credentials");
        if (el != null && cr != null) {
            AddressElement pe = (AddressElement) el;
            CredentialsElement ce = (CredentialsElement) cr;
            //Ho l'indirizzo del database
            DatabaseConnection.IP = pe.getAddress() + ":" + pe.getPort() + "/" + pe.getDatabaseName() + (pe.getValue() != null && !pe.getValue().isBlank()
                    ? "?" + pe.getValue()
                    : "");
            DatabaseConnection.PASSWORD = ce.getPassword();
            DatabaseConnection.USER = ce.getUsername();
        } else {
            //Installazione nuova
            AddressElement pe;
            CredentialsElement ce;
            if (el == null) {
                pe = new AddressElement();
                pe.setPort("3306");
                pe.setValue("serverTimezone=UTC&enabledTLSProtocols=TLSv1.2&noAccessToProcedureBodies=true");
                doc.getRoot().addSubElement(pe);
            } else {
                pe = (AddressElement) el;
            }
            if (cr == null) {
                ce = new CredentialsElement();
                doc.getRoot().addSubElement(ce);
            } else {
                ce = (CredentialsElement) cr;
            }
            showDialog(pe, ce);
        }
    }

    public void changeData() {
        AddressElement pe = (AddressElement) doc.getRoot().getFirstElement("address");
        CredentialsElement ce = (CredentialsElement) doc.getRoot().getFirstElement("credentials");
        showDialog(pe, ce);
        DialogCreator.showAlert(Alert.AlertType.WARNING, "Per procedere", "Per applicare le modifiche riavviare il programma.", null);
    }

    private void showDialog(AddressElement pe, CredentialsElement ce) {
        ConnectionSetup cs = new ConnectionSetup();
        cs.setup(pe.getAddress(), pe.getDatabaseName(), pe.getPort(), ce.getUsername(), ce.getPassword(), pe.getValue());
        DialogCreator.createDialog(cs, () -> {
            pe.setAddress(cs.getAddress());
            pe.setDatabaseName(cs.getDatabaseName());
            pe.setPort(cs.getPort());
            pe.setValue(cs.getParameters());
            ce.setPassword(cs.getPassword());
            ce.setUsername(cs.getUsername());
        }, () -> {
            DialogCreator.showAlert(Alert.AlertType.WARNING, "Parametri inalterati", "Se non vengono cambati i parametri lo stato di connessione rimane uguale.", null);
        }).showAndWait();
    }

}
