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
package theopenhand.programm.window;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import theopenhand.installer.data.ConnectionData;
import theopenhand.installer.data.ProgrammData;
import theopenhand.programm.MainRR;
import theopenhand.programm.controls.GUIControl;
import theopenhand.programm.controls.StaticExchange;
import theopenhand.programm.window.homepage.HomepageScene;
import theopenhand.programm.window.plugins.PluginForm;
import theopenhand.runtime.loader.folder.PluginFolderHandler;
import theopenhand.statics.StaticReferences;
import theopenhand.window.graphics.dialogs.DialogCreator;
import theopenhand.window.graphics.ribbon.RibbonFactory;

/**
 *
 * @author gabri
 */
public class Ribbon {

    public Ribbon() {
    }

    public void inject() {
        Button v = new Button("Esci");
        v.setOnAction(a -> {
            DialogCreator.showAlert(Alert.AlertType.CONFIRMATION, "Conferma uscita", "Vuoi disconnettere e salvare il lavoro?", null).ifPresent((t) -> {
                if (t.equals(ButtonType.YES)) {
                    Platform.exit();
                }
            });
        });

        Button b = new Button("Benvenuto");
        b.setOnAction(a -> {
            StaticReferences.getMainWindowReference().setCenterNode(HomepageScene.getInstance());
        });
        MenuButton v1 = new MenuButton("Salva");
        MenuItem db_save = new MenuItem("Database");
        db_save.setOnAction((t) -> {
            saveDatabase();
        });
        Menu v2 = new Menu("Dati programma");
        MenuItem pg_save_sett = new MenuItem("Impostazioni");
        pg_save_sett.setOnAction((t) -> {
            ProgrammData.getInstance().flush();
            ConnectionData.getInstance().flush();
            DialogCreator.createAlert(Alert.AlertType.INFORMATION, "Salvataggio completato", "I dati sono stati salvati con successo.", null).show();
        });
        MenuItem pg_save_pl = new MenuItem("Plugin");
        pg_save_pl.setOnAction((t) -> {
            DialogCreator.showAlert(Alert.AlertType.WARNING, "Attenzione!", "Salvando questi dati i plugin verranno disattivate e ricaricati solamente al riavvio del programma.", null)
                    .ifPresent((x) -> {
                        if (x == ButtonType.OK) {

                            PluginFolderHandler.getInstance().flush();
                        }
                    });
        });
        v2.getItems().addAll(pg_save_sett, pg_save_pl);
        v1.getItems().addAll(db_save, v2);
        RibbonFactory.createGroup(MainRR.mrr, "Home", "Programma").addNode(v).addNode(v1);
        RibbonFactory.createGroup(MainRR.mrr, "Home", "Inizio").addNode(b);

        Button ms = new Button("Store");
        ms.setOnAction((t) -> {
            GUIControl.getInstance().showStore();
        });
        Button mps = new Button("Impostazioni");
        mps.setOnAction((t) -> {
            PluginForm pf = new PluginForm(StaticExchange.getLOADER());
            StaticReferences.getMainWindowReference().setCenterNode(pf);
        });
        VBox pls = new VBox(ms, mps);
        pls.setSpacing(5);
        pls.setPrefHeight(Region.USE_COMPUTED_SIZE);
        pls.setMinHeight(Region.USE_COMPUTED_SIZE);
        RibbonFactory.createGroup(MainRR.mrr, "Home", "Plugins").addNode(pls);
    }

    public static void saveDatabase() {
        StaticReferences.getConnection().commit();
        DialogCreator.createAlert(Alert.AlertType.INFORMATION, "Salvataggio completato", "I dati sono stati salvati con successo.", null).show();
    }

}
