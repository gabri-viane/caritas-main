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
import theopenhand.commons.events.programm.utils.ListEventListener;
import theopenhand.installer.data.ConnectionData;
import theopenhand.installer.data.Version;
import theopenhand.installer.online.update.ProgrammAutoupdate;
import theopenhand.installer.utils.WebConnection;
import theopenhand.programm.window.Ribbon;
import theopenhand.programm.window.homepage.HomepageScene;
import theopenhand.programm.window.plugins.PluginForm;
import theopenhand.window.graphics.ribbon.elements.RibbonBar;
import theopenhand.programm.window.store.PluginStoreView;
import theopenhand.runtime.SubscriptionHandler;
import theopenhand.runtime.loader.Loader;
import theopenhand.runtime.templates.RuntimeReference;
import theopenhand.statics.StaticReferences;
import theopenhand.window.graphics.creators.DialogCreator;
import theopenhand.window.graphics.ribbon.RibbonFactory;

/**
 *
 * @author gabri
 */
public class GUIControl {

    private static final GUIControl instance = new GUIControl();
    private PluginStoreView psv;
    private RibbonBar rb;

    private GUIControl() {
        init();
    }

    public void showStore() {
        if (psv == null) {
            psv = new PluginStoreView();
        }
        psv.refresh();
        StaticExchange.MAC.getMainContainerBP().setCenter(psv);
    }

    private void init() {
        StaticExchange.MAC.getMainContainerBP().setCenter(HomepageScene.getInstance());
        rb = new RibbonBar();
        StaticExchange.MAC.getMainContainerBP().setTop(rb);
        SubscriptionHandler.addListener(new ListEventListener<RuntimeReference>() {
            @Override
            public void onElementAdded(RuntimeReference element) {
                refreshData();
            }

            @Override
            public void onElementRemoved(RuntimeReference element) {
            }
        });
    }

    public static GUIControl getInstance() {
        return instance;
    }

    public void initKeybinds() {
        StaticReferences.getMainWindowScene().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (null != event.getCode()) {
                switch (event.getCode()) {
                    case S -> {
                        if (event.isControlDown()) {
                            Ribbon.saveDatabase();
                            event.consume();
                        }
                    }
                    case F2 -> {
                        BorderPane mainContainerBP = StaticExchange.MAC.getMainContainerBP();
                        if (mainContainerBP.getTop() == rb) {
                            mainContainerBP.setTop(StaticExchange.MAC.getMainMenuBar());
                        } else {
                            mainContainerBP.setTop(rb);
                        }
                        event.consume();
                    }
                    case F1 -> {
                        showStore();
                        event.consume();
                    }
                    case F3 -> {
                        StaticExchange.MAC.getConnSetupBTN().fire();
                        event.consume();
                    }
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

    public void refreshData() {
        Platform.runLater(() -> {
            Loader l = StaticExchange.LOADER;
            //PluginBinder.loadAll();
            RibbonFactory.load(rb);
            StaticExchange.MAC.getPluginSettingsBtn().setOnAction((a) -> StaticReferences.getMainWindowReference().setCenterNode(new PluginForm(l)));
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
