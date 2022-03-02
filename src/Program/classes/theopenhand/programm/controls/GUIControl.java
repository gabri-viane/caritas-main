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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import theopenhand.installer.data.ConnectionData;
import theopenhand.installer.utils.WebConnection;
import theopenhand.programm.window.plugins.PluginForm;
import theopenhand.programm.window.store.PluginStoreView;
import theopenhand.runtime.loader.Loader;
import theopenhand.statics.StaticReferences;
import theopenhand.window.resources.handler.PluginBinder;

/**
 *
 * @author gabri
 */
public class GUIControl {

    private static final GUIControl instance = new GUIControl();

    private GUIControl() {
    }

    public static GUIControl getInstance() {
        return instance;
    }

    public void initKeybinds() {
        StaticReferences.getMainWindowScene().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.F1) {
                BorderPane mainContainerBP = StaticExchange.MAC.getMainContainerBP();
                if (mainContainerBP.getTop() != null) {
                    mainContainerBP.setTop(null);
                } else {
                    mainContainerBP.setTop(StaticExchange.MAC.getMainMenuBar());
                }
                event.consume();
            }
        });
        StaticExchange.MAC.getPluginStoreBtn().setOnAction(a -> StaticExchange.MAC.getMainContainerBP().setCenter(new PluginStoreView()));
        StaticExchange.MAC.getConnSetupBTN().setOnAction(a->{
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

    public void showDownloadTask(WebConnection.DownloadTask dt,Runnable on_succeeded) {
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
