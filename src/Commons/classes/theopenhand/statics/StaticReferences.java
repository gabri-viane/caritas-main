/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.statics;

import java.util.ArrayList;
import javafx.scene.Scene;
import theopenhand.commons.connection.DatabaseConnection;
import theopenhand.commons.events.programm.FutureCallable;
import theopenhand.commons.handlers.MainWindowEventsHandler;

/**
 *
 * @author gabri
 */
public class StaticReferences {

    /**
     *
     */
    public static DatabaseConnection dbconn;

    private static Scene MAIN_WINDOW_SCENE;
    private static MainWindowEventsHandler MAIN_WINDOW_EVENTS_HANDLER;
    private static ArrayList<FutureCallable<Void>> on_runtime_exit = new ArrayList<>();

    static {
        Thread hook = new Thread() {
            @Override
            public void run() {
                on_runtime_exit.forEach(a -> {
                    a.execute();
                });
            }
        };
        Runtime.getRuntime().addShutdownHook(hook);
    }

    public static void subscribeOnExit(FutureCallable<Void> clb) {
        on_runtime_exit.add(clb);
    }

    /**
     *
     * @return
     */
    public static Scene getMainWindowScene() {
        return MAIN_WINDOW_SCENE;
    }

    /**
     *
     * @return
     */
    public static MainWindowEventsHandler getMainWindowReference() {
        return MAIN_WINDOW_EVENTS_HANDLER;
    }

    /**
     *
     * @return
     */
    public static DatabaseConnection getConnection() {
        return dbconn;
    }

    /**
     *
     * @param MAIN_WINDOW_EVENTS_HANDLER
     */
    public static void setMAIN_WINDOW_EVENTS_HANDLER(MainWindowEventsHandler MAIN_WINDOW_EVENTS_HANDLER) {
        StaticReferences.MAIN_WINDOW_EVENTS_HANDLER = MAIN_WINDOW_EVENTS_HANDLER;
    }

    /**
     *
     * @param MAIN_WINDOW_SCENE
     */
    public static void setMAIN_WINDOW_SCENE(Scene MAIN_WINDOW_SCENE) {
        StaticReferences.MAIN_WINDOW_SCENE = MAIN_WINDOW_SCENE;
    }

}
