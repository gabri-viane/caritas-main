/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.window.hand;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import theopenhand.commons.connection.DatabaseConnection;
import theopenhand.runtime.templates.ReferenceController;

/**
 *
 * @author gabri
 */
public class MainReference extends ReferenceController {

    private static final MainReference mr = new MainReference();

    private Scene s;
    private MainActivityCTRL rc;

    private MainReference() {
        init();
    }

    /**
     *
     * @return
     */
    public static MainReference getInstance() {
        return mr;
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/hand/MainActivity.fxml");
            loader.setLocation(resource);
            AnchorPane root = loader.load();
            MainActivityCTRL controller = loader.getController();

            this.s = new Scene(root);
            s.getStylesheets().add(getClass().getResource("/theopenhand/window/resources/sheets/ProgrammStylesheet.css").toExternalForm());
            rc = controller;
        } catch (IOException ex) {
            Logger.getLogger(MainReference.class.getName()).log(Level.SEVERE, null, ex);
        }
        rc.getServerLB().setText(DatabaseConnection.IP.split("[?]")[0]);
        rc.getUserLB().setText(DatabaseConnection.USER);
    }

    /**
     *
     * @return
     */
    public MainActivityCTRL getRc() {
        return rc;
    }

    /**
     *
     * @return
     */
    @Override
    public String getID() {
        return "MAIN-WND";
    }

    /**
     *
     * @return
     */
    public Scene getScene() {
        return s;
    }
}
