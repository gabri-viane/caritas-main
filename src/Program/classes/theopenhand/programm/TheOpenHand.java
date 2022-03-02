/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.programm;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import theopenhand.statics.StaticReferences;
import theopenhand.commons.connection.DatabaseConnection;
import theopenhand.commons.handlers.MainWindowEventsHandler;
import theopenhand.installer.SetupInit;
import theopenhand.installer.data.ConnectionData;
import theopenhand.installer.data.ProgrammData;
import theopenhand.programm.controls.GUIControl;
import theopenhand.programm.controls.PluginController;
import theopenhand.programm.controls.StaticExchange;
import theopenhand.programm.controls.UpdateControl;
import theopenhand.programm.window.store.PluginStoreView;
import theopenhand.statics.privates.StaticReferencesPvt;
import theopenhand.window.graphics.dialogs.DialogCreator;
import theopenhand.window.hand.MainActivityCTRL;
import theopenhand.window.hand.MainReference;

/**
 *
 * @author gabri
 */
public class TheOpenHand extends Application {

    private void initSetup() {
        /*try {
            Runtime.getRuntime().exec(new String[]{"cmd","/c",System.getProperty("user.dir")+File.separatorChar+"app"+File.separatorChar+"NCDB OuterHands.exe"});
        } catch (IOException ex) {
            Logger.getLogger(TheOpenHand.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        SetupInit.getInstance();
        ProgrammData.getInstance();
        ConnectionData.getInstance();
        UpdateControl up = new UpdateControl();
        up.checkProgramUpdate();
    }

    @Override
    public void start(Stage stage) {
        initSetup();

        MainReference mr = MainReference.getInstance();
        MainActivityCTRL activityCTRL = (MainActivityCTRL) mr.getRc();

        StaticExchange.setMR(mr);
        StaticExchange.setMAC(activityCTRL);
        StaticReferencesPvt.primaryStage = stage;

        Scene s = mr.getScene();

        StaticReferences.setMAIN_WINDOW_SCENE(s);
        StaticReferences.setMAIN_WINDOW_EVENTS_HANDLER(new MainWindowEventsHandler(activityCTRL));

        StaticReferences.dbconn = new DatabaseConnection();
        stage.setScene(s);
        stage.setTitle("TheOpenHand : Database Caritas");
        stage.getIcons().add(new Image(TheOpenHand.class.getResourceAsStream("/theopenhand/programm/resources/TheOpenHand-Icona Round.png")));
        stage.show();

        GUIControl.getInstance().initKeybinds();

        if (StaticReferences.dbconn.isConnected()) {
            PluginController.getInstance().load();
        } else {
            DialogCreator.showAlert(Alert.AlertType.WARNING, "Connessione fallita", "Non è stato possibile stabilire una connessione al database.\nI Plugins e le funzionalità aggiuntive non saranno caricate.", null);
        }

        PluginStoreView psv = new PluginStoreView();
        activityCTRL.getMainContainerBP().setCenter(psv);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
