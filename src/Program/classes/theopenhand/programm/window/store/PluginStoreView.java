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
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import theopenhand.installer.SetupInit;
import theopenhand.installer.online.store.PluginDownloadData;
import theopenhand.installer.online.store.PluginStore;
import theopenhand.installer.utils.WebConnection;
import theopenhand.runtime.loader.folder.Installer;
import theopenhand.runtime.loader.folder.PluginFolderHandler;
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
    private TableColumn<PluginDownloadData, String> nameColumn;

    @FXML
    private Label nameLB;

    @FXML
    private TableView<PluginDownloadData> pluginsTable;

    @FXML
    private TableColumn<PluginDownloadData, String> versionColumn;

    @FXML
    private TableColumn<PluginDownloadData, String> installedColumn;

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

    @FXML
    private ListView<PluginDownloadData> reqLV;

    @FXML
    private VBox reqVB;

    private final PluginStore ps;
    HashMap<UUID, PluginDownloadData> to_install = new HashMap<>();

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
        nameColumn.setCellValueFactory(new CustomPVF("name"));
        versionColumn.setCellValueFactory(new CustomPVF("version"));
        installedColumn.setCellValueFactory(new CustomPVF("installed"));
        pluginsTable.getSelectionModel().selectedItemProperty().addListener((var ov, var t, var t1) -> {
            if (t1 != null) {
                setData(t1);
            } else {
                noSelection();
            }
        });
        autoInstallHL.setOnAction(a -> {
            PluginDownloadData pd = (PluginDownloadData) autoInstallHL.getUserData();
            WebConnection.DownloadTask dt = ps.download(pd);
            dt.setOnSucceeded((t) -> {
                onDownloadEnd(dt, pd);
                installRequired(pd);
                installSuccess();
            });
            autoInstallHL.setVisited(false);
            startDownload(dt);
        });
        manualInstallHL.setOnAction(a -> {
            WebConnection.DownloadTask dt = ps.download((PluginDownloadData) autoInstallHL.getUserData());
            dt.setOnSucceeded((t) -> {
                downloadSuccess();
            });
            autoInstallHL.setVisited(false);
            startDownload(dt);
        });
        zipInstallHL.setOnAction(a -> {
            zipInstall();
            zipInstallHL.setVisited(false);
        });
        jarInstallHL.setOnAction(a -> {
            jarInstallHL.setVisited(false);
        });
        linkInstallHL.setOnAction(a -> {
            linkInstallHL.setVisited(false);
        });
        refresh();
        noSelection();
    }

    private void installRequired(PluginDownloadData pd) {
        to_install.clear();
        findAllRequired(pd);
        to_install.forEach((t, u) -> {
            WebConnection.DownloadTask dt = ps.download(u);
            dt.setOnSucceeded(e -> {
                onDownloadEnd(dt, u);
            });
            startDownload(dt);
        });
    }

    private void findAllRequired(PluginDownloadData pd) {
        pd.getRequires().forEach(uid -> {
            PluginDownloadData plugin_to_install = ps.getPlugin(uid);
            if (!PluginFolderHandler.getInstance().installed(uid)) {
                to_install.put(uid, plugin_to_install);
                findAllRequired(plugin_to_install);
            }
        });
    }

    private void refresh() {
        ps.refresh();
        ObservableList<PluginDownloadData> items = pluginsTable.getItems();
        pluginsTable.getSelectionModel().clearSelection();
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
        reqVB.setVisible(false);
        reqLV.getItems().clear();
    }

    private void setData(PluginDownloadData pd) {
        autoInstallHL.setVisible(true);
        manualInstallHL.setVisible(true);
        autoInstallHL.setUserData(pd);
        manualInstallHL.setUserData(pd);
        nameLB.setText(pd.getName());
        versionLB.setText(pd.getVersion().toString());
        boolean already_installed = PluginFolderHandler.getInstance().installed(pd.getUuid());
        autoInstallHL.setDisable(already_installed);
        manualInstallHL.setDisable(already_installed);
        installedLB.setText(already_installed ? "Sì" : "No");
        if (pd.getDescription() == null) {
            ps.retriveDescription(pd);
        }
        reqLV.getItems().clear();
        if (!pd.getRequires().isEmpty()) {
            pd.getRequires().forEach(uid -> {
                reqLV.getItems().add(ps.getPlugin(uid));
            });
            reqVB.setVisible(true);
        } else {
            reqVB.setVisible(false);
        }
        descTA.setText(pd.getDescription());
    }

    private void zipInstall() {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(SetupInit.getInstance().getDOWNLOAD_FOLDER());
        File selected_file = fc.showOpenDialog(StaticReferences.getMainWindowScene().getWindow());
        if (selected_file != null) {
            if (Installer.generate(selected_file).installZipComplete()) {
                DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Installazione completata", "Il plugin è stato installato.\n Riavviare il programma per applicare le modifiche.", null);
            } else {
                DialogCreator.showAlert(Alert.AlertType.ERROR, "Installazione fallita", "Non è stato possibile installare il plugin selezionato.", null);
            }
        }
    }

    private void downloadSuccess() {
        taskHB.setVisible(false);
        taskPB.progressProperty().unbind();
        taskLB.textProperty().unbind();
        autoInstallHL.setDisable(false);
        DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Scaricamento completato", "Il plugin è stato scaricato.", null);
        taskHB.setVisible(false);
    }

    private void installSuccess() {
        DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Installazione completata", "Il plugin è stato scaricato e installato.\n Riavviare il programma per applicare le modifiche.", null);
        taskHB.setVisible(false);
    }

    private void startDownload(WebConnection.DownloadTask dt) {
        taskHB.setVisible(true);
        taskPB.progressProperty().bind(dt.progressProperty());
        taskLB.textProperty().bind(dt.messageProperty());
        autoInstallHL.setDisable(true);
        Thread td = new Thread(dt);
        td.start();
    }

    private void onDownloadEnd(WebConnection.DownloadTask dt, PluginDownloadData pd) {
        taskHB.setVisible(false);
        taskPB.progressProperty().unbind();
        taskLB.textProperty().unbind();
        autoInstallHL.setDisable(false);
        Installer.generate(dt.getOutput()).installFrom(pd);
        refresh();
    }

    private class CustomPVF extends PropertyValueFactory<PluginDownloadData, String> {

        public CustomPVF(String s) {
            super(s);
        }

        @Override
        public ObservableValue<String> call(TableColumn.CellDataFeatures<PluginDownloadData, String> cdf) {
            if ("installed".equals(this.getProperty())) {
                return new SimpleStringProperty(PluginFolderHandler.getInstance().installed(cdf.getValue().getUuid()) ? "Sì" : "No");
            } else {
                return super.call(cdf);
            }
        }

    }

}
