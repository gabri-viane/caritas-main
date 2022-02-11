/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package theopenhand.window.resources.ui.settings.plugins;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import theopenhand.commons.events.programm.OpExecutor;
import theopenhand.runtime.loader.Loader;
import theopenhand.runtime.loader.SettingsLoader;
import theopenhand.runtime.loader.folder.PluginFolderHandler;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class PluginSettingPage extends AnchorPane {

    @FXML
    private Button activateBTN;

    @FXML
    private Label activeLBL;

    @FXML
    private Button deactivateBTN;

    @FXML
    private Label nameLBL;

    @FXML
    private VBox optionsContainerVB;

    @FXML
    private Label ref_countLBL;

    @FXML
    private Button uninstallBTN;

    @FXML
    private Label versionLBL;

    private final SettingsLoader sl;

    /**
     *
     * @param sl
     */
    public PluginSettingPage(SettingsLoader sl) {
        this.sl = sl;
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/resources/ui/settings/plugins/PluginSettingPage.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(PluginSettingPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadOptions();
        Loader ld = Loader.getInstance();
        UUID td = sl.getUuid();
        int cnt = 0;
        for (var rr : ld.getRuntimeRefrences(td)) {
            cnt += rr.getPluginReferenceControllers().size();
        }
        this.ref_countLBL.setText("" + cnt);
        this.nameLBL.setText(ld.findPluginName(td));
        this.activeLBL.setText("Sì");
        uninstallBTN.setOnAction(a -> {
            uninstall();
        });
        deactivateBTN.setOnAction(a -> {
            sl.getLinkableClass().unload();
            this.activeLBL.setText("No");
        });
        activateBTN.setOnAction(a -> {
            sl.getLinkableClass().load();
            this.activeLBL.setText("Sì");
        });
    }

    private void loadOptions() {
        optionsContainerVB.getChildren().addAll(sl.getNodes());
    }

    public void setVersion(String version) {
        this.versionLBL.setText(version);
    }

    private void uninstall() {
        var pfh = PluginFolderHandler.getInstance();
        OpExecutor<Void> op = Loader.getInstance().remove(sl.getUuid());
        OpExecutor<Void> next = () -> {
            DialogCreator.showAlert(Alert.AlertType.WARNING, "Disistallazione in attesa", "Il plugin verrà rimosso all'uscita del programma, al prossimo avvio non sarà più disponibile.", null);
            return null;
        };
        List<UUID> ll = pfh.getRequiredBy(sl.getUuid());
        Optional<ButtonType> showAndWait;
        if (ll != null && !ll.isEmpty()) {
            showAndWait = DialogCreator.showAlert(Alert.AlertType.CONFIRMATION, "Disistallazione con dipendenze plugin", "Esistono " + ll.size() + " plugin che dipendono da questo. Procedendo verranno rimosse anche le dipendenze.", null);
        } else {
            showAndWait = DialogCreator.showAlert(Alert.AlertType.CONFIRMATION, "Disinstallazione plugin", "Vuoi disinstallare veramente questo plugin?\nVerranno rimosse " + ref_countLBL.getText() + " funzionalità aggiunte.", null);
        }
        showAndWait.ifPresent(bt -> {
            if (bt.equals(ButtonType.YES)) {
                op.onCall();
                next.onCall();
            }
        });
    }

}
