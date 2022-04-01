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
package theopenhand.programm.controls;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import theopenhand.installer.online.update.OHAutoupdate;
import theopenhand.installer.online.update.ProgrammAutoupdate;
import theopenhand.installer.utils.WebConnection;
import theopenhand.programm.TheOpenHand;
import theopenhand.window.graphics.creators.DialogCreator;

/**
 *
 * @author gabri
 */
public class UpdateControl {

    public UpdateControl() {

    }

    public void checkProgramUpdate() {
        ProgrammAutoupdate pa = new ProgrammAutoupdate();
        if (pa.toUpdate()) {
            WebConnection.DownloadTask du = pa.downloadUpdate();
            du.setOnSucceeded((t) -> {
                pa.extract(du);
            });
            Thread t = new Thread(du);
            GUIControl.getInstance().showDownloadTask(du, () -> {
                File update = pa.extract(du);
                Optional<ButtonType> showAlert = DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Aggiornamento", "Aggiornamento scaricato e pronto per l'installazione.", null);
                showAlert.ifPresent((y) -> {
                    if (y.equals(ButtonType.OK)) {
                        //Fai partire aggiornamento
                        try {
                            System.out.println(du.getOutput().getAbsolutePath());
                            Runtime.getRuntime().exec(new String[]{"cmd", "/c", update.getAbsolutePath()});
                        } catch (IOException ex) {
                            Logger.getLogger(TheOpenHand.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        Platform.exit();
                    }
                });
            });
            t.start();
        }
    }

    public void checkOuterHands() {
        OHAutoupdate oha = new OHAutoupdate();
        if (oha.toUpdate()) {
            WebConnection.DownloadTask du = oha.downloadUpdate();
            du.setOnSucceeded((t) -> {
                oha.extract(du);
            });
            Thread t = new Thread(du);
            GUIControl.getInstance().showDownloadTask(du, () -> {
                oha.extract(du);
            });
            t.start();
        }
    }

}
