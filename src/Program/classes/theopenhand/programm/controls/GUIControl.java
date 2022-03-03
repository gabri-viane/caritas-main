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

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import theopenhand.commons.connection.DatabaseConnection;
import theopenhand.installer.data.ConnectionData;
import theopenhand.installer.data.Version;
import theopenhand.installer.online.update.ProgrammAutoupdate;
import theopenhand.installer.utils.WebConnection;
import theopenhand.programm.window.homepage.HomepageScene;
import theopenhand.programm.window.plugins.PluginForm;
import theopenhand.programm.window.store.PluginStoreView;
import theopenhand.runtime.loader.Loader;
import theopenhand.statics.StaticReferences;
import theopenhand.window.graphics.dialogs.DialogCreator;
import theopenhand.window.resources.handler.PluginBinder;

/**
 *
 * @author gabri
 */
public class GUIControl {

    private static final GUIControl instance = new GUIControl();
    private PluginStoreView psv;

    private GUIControl() {
        init();
    }

    private void init() {
        StaticExchange.MAC.getMainContainerBP().setCenter(HomepageScene.getInstance());
    }

    public static GUIControl getInstance() {
        return instance;
    }

    public void initKeybinds() {
        StaticReferences.getMainWindowScene().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (null != event.getCode()) {
                switch (event.getCode()) {
                    case F1 -> {
                        BorderPane mainContainerBP = StaticExchange.MAC.getMainContainerBP();
                        if (mainContainerBP.getTop() != null) {
                            mainContainerBP.setTop(null);
                        } else {
                            mainContainerBP.setTop(StaticExchange.MAC.getMainMenuBar());
                        }
                        event.consume();
                    }
                    case F2 -> {
                        if (psv == null) {
                            psv = new PluginStoreView();
                        }
                        psv.refresh();
                        StaticExchange.MAC.getMainContainerBP().setCenter(psv);
                    }
                    case F3 ->
                        StaticExchange.MAC.getConnSetupBTN().fire();
                    default -> {
                    }
                }
            }
        });
        StaticExchange.MAC.getPluginStoreBtn().setOnAction(a -> StaticExchange.MAC.getMainContainerBP().setCenter(new PluginStoreView()));
        StaticExchange.MAC.getAboutMenuBtn().setOnAction((t) -> {
            DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Informazioni a riguardo", "Versione: " + Version.serialVersionUID + "\nStatus connessione: " + (StaticReferences.getConnection().isConnected() ? "Connesso" : "Disconnseeo"), null);
        });
        StaticExchange.MAC.getUpdatePrgMenuBtn().setOnAction((t) -> {
            ProgrammAutoupdate pa = new ProgrammAutoupdate();
            if (pa.toUpdate()) {
                UpdateControl uc = new UpdateControl();
                uc.checkProgramUpdate();
            } else {
                DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Nessun aggiornamento", "L'ultima versione è già installata.", null);
            }
        });
        StaticExchange.MAC.getConnSetupBTN().setOnAction(a -> {
            ConnectionData.getInstance().changeData();
        });
        Hyperlink restartHL = StaticExchange.MAC.getRestartHL();
        restartHL.setOnAction(a -> {
            //Qui chiamo outerHands e chiudo questa instanza
            Platform.exit();
        });
        showRestart(false);
    }

    public void rereshData() {
        Platform.runLater(() -> {
            PluginBinder.loadAll();
            Loader l = StaticExchange.LOADER;
            PluginForm pfcntrl = new PluginForm(l);
            StaticExchange.MAC.getPluginSettingsBtn().setOnAction((a) -> StaticReferences.getMainWindowReference().setCenterNode(pfcntrl));
            StaticExchange.MAC.getPluginCounterLB().setText("" + l.getUUIDS().size());
        });
    }

    public void showRestart(boolean val) {
        StaticExchange.MAC.setRestartVisible(val);
    }

    public void showDownloadTask(WebConnection.DownloadTask dt, Runnable on_succeeded) {
        ProgressBar pb = new ProgressBar();
        pb.setPrefWidth(200);
        pb.progressProperty().bind(dt.progressProperty());
        Platform.runLater(() -> {
            StaticExchange.MAC.getBelowContainer().getChildren().add(pb);
        });
        dt.setOnSucceeded((t) -> {
            on_succeeded.run();
            Platform.runLater(() -> {
                StaticExchange.MAC.getBelowContainer().getChildren().remove(pb);
            });
        });
    }

}
