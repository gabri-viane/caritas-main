/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.programm;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
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
import theopenhand.installer.online.store.PluginStore;
import theopenhand.programm.controls.GUIControl;
import theopenhand.programm.controls.PluginController;
import theopenhand.programm.controls.StaticExchange;
import theopenhand.programm.controls.UpdateControl;
import theopenhand.programm.window.Ribbon;
import theopenhand.statics.privates.StaticReferencesPvt;
import theopenhand.window.graphics.creators.DialogCreator;
import theopenhand.window.hand.MainActivityCTRL;
import theopenhand.window.hand.MainReference;

/**
 *
 * @author gabri
 */
public class TheOpenHand extends Application {

    private void initSetup() {
        PluginStore.getInstance();
        SetupInit.getInstance();
        ProgrammData instance = ProgrammData.getInstance();
        ConnectionData.getInstance();
        MainReference.css_selected = instance.getTheme() != null ? getClass().getResource(instance.getTheme()).toExternalForm() : "/theopenhand/programm/resources/sheets/DefaultTheme.css";
        if (instance.getGCTask()) {
            Timer t = new Timer(true);
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.gc();
                }
            }, new Date(), 30000);
        }
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
        new Ribbon().inject();

        if (StaticReferences.dbconn.isConnected()) {
            PluginController.getInstance().load();
        } else {
            DialogCreator.showAlert(Alert.AlertType.WARNING, "Connessione fallita", "Non è stato possibile stabilire una connessione al database.\nI Plugins e le funzionalità aggiuntive non saranno caricate.", null);
        }
        UpdateControl up = new UpdateControl();
        up.checkProgramUpdate();
        up.checkOuterHands();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
