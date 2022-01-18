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
package theopenhand.programm.window.store;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import theopenhand.installer.SetupInit;
import theopenhand.installer.plugins.store.PluginData;
import theopenhand.installer.plugins.store.PluginStore;
import theopenhand.installer.utils.WebConnection;
import theopenhand.runtime.loader.Loader;
import theopenhand.runtime.loader.folder.Installer;
import theopenhand.statics.StaticReferences;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class PluginStoreView extends AnchorPane {

    @FXML
    private Hyperlink autoInstallHL;

    @FXML
    private TextArea descTA;

    @FXML
    private ImageView iconStore;

    @FXML
    private Label installedLB;

    @FXML
    private Hyperlink jarInstallHL;

    @FXML
    private Hyperlink linkInstallHL;

    @FXML
    private Hyperlink manualInstallHL;

    @FXML
    private TableColumn<PluginData, String> nameColumn;

    @FXML
    private Label nameLB;

    @FXML
    private TableView<PluginData> pluginsTable;

    @FXML
    private TableColumn<PluginData, String> versionColumn;

    @FXML
    private Label versionLB;

    @FXML
    private Hyperlink zipInstallHL;

    @FXML
    private HBox taskHB;

    @FXML
    private Label taskLB;

    @FXML
    private ProgressBar taskPB;

    private final PluginStore ps;
    private WebConnection.DownloadTask dt;

    public PluginStoreView() {
        ps = PluginStore.getInstance();
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/programm/window/store/PluginStoreView.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(PluginStoreView.class.getName()).log(Level.SEVERE, null, ex);
        }
        iconStore.setImage(new Image(this.getClass().getResourceAsStream("/theopenhand/programm/resources/plugin_store_symbol.png")));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        versionColumn.setCellValueFactory(new PropertyValueFactory<>("version"));
        pluginsTable.getSelectionModel().selectedItemProperty().addListener((var ov, var t, var t1) -> {
            if (t1 != null) {
                setData(t1);
            } else {
                noSelection();
            }
        });
        autoInstallHL.setOnAction(a -> {
            dt = ps.download((PluginData) autoInstallHL.getUserData());
            dt.setOnSucceeded((t) -> {
                installSuccess();
            });
            autoInstallHL.setVisited(false);
            startDownload();
        });
        manualInstallHL.setOnAction(a -> {
            dt = ps.download((PluginData) autoInstallHL.getUserData());
            dt.setOnSucceeded((t) -> {
                downloadSuccess();
            });
            autoInstallHL.setVisited(false);
            startDownload();
        });
        zipInstallHL.setOnAction(a -> {
            zipInstall();
            zipInstallHL.setVisited(false);
        });
        refresh();
        noSelection();
    }

    private void refresh() {
        ps.refresh();
        ObservableList<PluginData> items = pluginsTable.getItems();
        items.clear();
        items.addAll(ps.getPlugins().values());
    }

    private void noSelection() {
        nameLB.setText("selziona");
        versionLB.setText("selziona");
        installedLB.setText("selziona");
        autoInstallHL.setVisible(false);
        autoInstallHL.setUserData(null);
        manualInstallHL.setVisible(false);
        manualInstallHL.setUserData(null);
        taskHB.setVisible(false);
    }

    private void setData(PluginData pd) {
        autoInstallHL.setVisible(true);
        manualInstallHL.setVisible(true);
        autoInstallHL.setUserData(pd);
        manualInstallHL.setUserData(pd);
        nameLB.setText(pd.getName());
        versionLB.setText(pd.getVersion().toString());
        boolean already_installed = Loader.getInstance().getUUIDS().contains(pd.getUuid());
        autoInstallHL.setDisable(already_installed);
        manualInstallHL.setDisable(already_installed);
        installedLB.setText(already_installed ? "Sì" : "No");
        if (pd.getDescription() == null) {
            ps.retriveDescription(pd);
        }
        descTA.setText(pd.getDescription());
    }

    private void zipInstall() {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(SetupInit.getInstance().getDOWNLOAD_FOLDER());
        File selected_file = fc.showOpenDialog(StaticReferences.getMainWindowScene().getWindow());
        if (selected_file != null) {
            Alert showAlert;
            if (Installer.generate(selected_file).install()) {
                showAlert = DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Installazione completata", "Il plugin è stato installato.\n Riavviare il programma per applicare le modifiche.", null);
            } else {
                showAlert = DialogCreator.showAlert(Alert.AlertType.ERROR, "Installazione fallita", "Non è stato possibile installare il plugin selezionato.", null);
            }
            showAlert.showAndWait();
        }
    }

    private void downloadSuccess() {
        taskHB.setVisible(false);
        taskPB.progressProperty().unbind();
        taskLB.textProperty().unbind();
        autoInstallHL.setDisable(false);
        Alert showAlert = DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Scaricamento completato", "Il plugin è stato scaricato.", null);
        showAlert.showAndWait();
        dt = null;
        taskHB.setVisible(false);
    }

    private void installSuccess() {
        taskHB.setVisible(false);
        taskPB.progressProperty().unbind();
        taskLB.textProperty().unbind();
        autoInstallHL.setDisable(false);
        Installer.generate(dt.getOutput()).install();
        Alert showAlert = DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Installazione completata", "Il plugin è stato scaricato e installato.\n Riavviare il programma per applicare le modifiche.", null);
        showAlert.showAndWait();
        dt = null;
        taskHB.setVisible(false);
    }

    private void startDownload() {
        taskHB.setVisible(true);
        taskPB.progressProperty().bind(dt.progressProperty());
        taskLB.textProperty().bind(dt.messageProperty());
        autoInstallHL.setDisable(true);
        Thread td = new Thread(dt);
        td.start();
    }

}
