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

import java.util.ArrayList;
import java.util.UUID;
import javafx.scene.control.Alert;
import theopenhand.installer.online.store.PluginDownloadData;
import theopenhand.installer.online.store.PluginStore;
import theopenhand.installer.utils.Installer;
import theopenhand.installer.utils.WebConnection;
import theopenhand.runtime.loader.Loader;
import theopenhand.runtime.loader.folder.PluginFolderHandler;
import theopenhand.window.graphics.creators.DialogCreator;

/**
 *
 * @author gabri
 */
public class PluginController {

    private static final PluginController instance = new PluginController();

    private PluginController() {

    }

    public static PluginController getInstance() {
        return instance;
    }

    public void load() {
        Thread t = new Thread() {
            @Override
            public void run() {
                StaticExchange.LOADER = Loader.getInstance();
                StaticExchange.LOADER.activate();
                executeUpdate();
                GUIControl.getInstance().refreshData();
            }
        };
        t.start();
    }

    private void update(ArrayList<UUID> toUpdate, int index) {
        if (index < toUpdate.size()) {
            UUID uuid = toUpdate.get(index);
            PluginStore ps = PluginStore.getInstance();
            PluginDownloadData plugin = ps.getPlugin(uuid);
            WebConnection.DownloadTask download = ps.download(plugin);
            GUIControl.getInstance().showDownloadTask(download, () -> {
                Installer i = Installer.generate(download.getOutput(), PluginFolderHandler.getInstance());
                i.installFrom(plugin);
                update(toUpdate, index + 1);
            });
            new Thread(download).start();
        } else if (!toUpdate.isEmpty()) {
            DialogCreator.showAlert(Alert.AlertType.WARNING, "Plugin aggiornati", "Riavviare il programma per applicare gli aggiornamenti dei plugin.", null);
        }
    }

    public void executeUpdate() {
        PluginFolderHandler pfh = PluginFolderHandler.getInstance();
        ArrayList<UUID> toUpdate = pfh.getToUpdate();
        update(toUpdate, 0);
    }
}
