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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import theopenhand.statics.StaticReferences;
import theopenhand.commons.connection.DatabaseConnection;
import theopenhand.commons.handlers.MainWindowEventsHandler;
import theopenhand.installer.SetupInit;
import theopenhand.programm.window.plugins.PluginForm;
import theopenhand.programm.window.store.PluginStoreView;
import theopenhand.runtime.loader.Loader;
import theopenhand.statics.privates.StaticReferencesPvt;
import theopenhand.window.graphics.dialogs.DialogCreator;
import theopenhand.window.hand.MainActivityCTRL;
import theopenhand.window.hand.MainReference;
import theopenhand.window.resources.handler.PluginBinder;

/**
 *
 * @author gabri
 */
public class TheOpenHand extends Application {
    
    private void initSetup() {
        //System.out.println(System.getProperty("user.dir"));
        /*try {
            Runtime.getRuntime().exec(new String[]{"cmd","/c",System.getProperty("user.dir")+File.separatorChar+"app"+File.separatorChar+"NCDB OuterHands.exe"});
        } catch (IOException ex) {
            Logger.getLogger(TheOpenHand.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        SetupInit.getInstance();
        //Installer.install(new File(ConsoleInput.getInstance().readString("Inserisci percorso file:"))).install();
    }
    
    @Override
    public void start(Stage stage) {
        
        initSetup();
        
        MainReference mr = MainReference.getInstance();
        MainActivityCTRL activityCTRL = (MainActivityCTRL) mr.getRc();
        
        StaticReferencesPvt.primaryStage = stage;
        
        Scene s = mr.getScene();
        StaticReferences.setMAIN_WINDOW_SCENE(s);
        StaticReferences.setMAIN_WINDOW_EVENTS_HANDLER(new MainWindowEventsHandler(activityCTRL));
        /*DatabaseConnection.IP = "127.0.0.1:3307/ncdb?serverTimezone=UTC";
        DatabaseConnection.USER = "root";
        DatabaseConnection.PASSWORD = "root";*/
        StaticReferences.dbconn = new DatabaseConnection();
        stage.setScene(s);
        stage.setTitle("TheOpenHand : Database Caritas");
        stage.getIcons().add(new Image(TheOpenHand.class.getResourceAsStream("/theopenhand/programm/resources/TheOpenHand-Icona Round.png")));
        stage.show();
        
        s.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.F1) {
                BorderPane mainContainerBP = activityCTRL.getMainContainerBP();
                if (mainContainerBP.getTop() != null) {
                    mainContainerBP.setTop(null);
                } else {
                    mainContainerBP.setTop(activityCTRL.getMainMenuBar());
                }
                event.consume();
            }
        });
        
        if (StaticReferences.dbconn.isConnected()) {
            Loader l = Loader.getInstance();
            l.activate();
            PluginBinder.loadAll();
            PluginForm pfcntrl = new PluginForm(l);
            activityCTRL.getPluginSettingsBtn().setOnAction((t) -> StaticReferences.getMainWindowReference().setCenterNode(pfcntrl));
        } else {
            DialogCreator.showAlert(Alert.AlertType.WARNING, "Connessione fallita", "Non è stato possibile stabilire una connessione al database.\nI Plugins e le funzionalità aggiuntive non saranno caricate.", null);
        }
        activityCTRL.getPluginStoreBtn().setOnAction(a -> activityCTRL.getMainContainerBP().setCenter(new PluginStoreView()));
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
