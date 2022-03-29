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
package theopenhand.installer.online.store;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import theopenhand.commons.events.programm.FutureCallable;
import theopenhand.installer.SetupInit;
import theopenhand.installer.interfaces.PluginHandler;
import theopenhand.installer.utils.Installer;
import theopenhand.installer.utils.WebConnection;
import theopenhand.statics.StaticReferences;
import theopenhand.window.graphics.creators.DialogCreator;

/**
 *
 * @author gabri
 */
public class DownloaderComplement {

    private final PluginStore ps;
    private final PluginHandler pl;
    HashMap<UUID, PluginDownloadData> to_install = new HashMap<>();
    HashMap<UUID, LibraryDownloadData> libraries = new HashMap<>();
    private final HBox taskHB;
    private final ProgressBar taskPB;
    private final Label taskLB;

    public DownloaderComplement(PluginHandler pl, HBox taskHB, ProgressBar taskPB, Label taskLB) {
        this.ps = PluginStore.getInstance();
        this.pl = pl;
        this.taskHB = taskHB;
        this.taskPB = taskPB;
        this.taskLB = taskLB;
    }

    public void installRequired(PluginDownloadData pd, FutureCallable<Void> onRefresh) {
        to_install.clear();
        findAllRequired(pd);
        findLibraries(pd);
        to_install.forEach((t, u) -> {
            WebConnection.DownloadTask dt = ps.download(u);
            dt.setOnSucceeded(e -> {
                onDownloadEnd(dt, u, onRefresh);
            });
            startDownload(dt);
        });
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(final_t);
        executorService.shutdown();
    }

    private void findAllRequired(PluginDownloadData pd) {
        pd.getRequires().forEach(uid -> {
            PluginDownloadData plugin_to_install = ps.getPlugin(uid);
            if (!pl.installed(uid)) {
                to_install.put(uid, plugin_to_install);
                findLibraries(pd);
                findAllRequired(plugin_to_install);
            }
        });
    }

    private void findLibraries(PluginDownloadData pd) {
        pd.getLibraries().forEach(uid -> {
            LibraryDownloadData lib = ps.getLibrary(uid);
            libraries.put(uid, lib);
            WebConnection.DownloadTask dt = ps.download(lib);
            dt.setOnSucceeded(e -> {
                onDownloadEnd(dt, lib);
            });
            startDownload(dt);
        });
    }

    private Task<Void> final_t = null;
    private final ArrayList<FutureCallable<Void>> onrefr = new ArrayList<>();

    public void refresh(FutureCallable<Void> onRefresh) {
        if (onRefresh != null) {
            onrefr.add(onRefresh);
        }
        if (final_t == null) {
            final_t = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    ps.refresh();
                    onrefr.forEach(t -> t.execute(to_install));
                    return null;
                }
            };
        }
    }

    public void forceRefresh(FutureCallable<Void> onRefresh) {
        new Thread() {
            @Override
            public void run() {
                ps.refresh();
                if (onRefresh != null) {
                    onRefresh.execute(to_install);
                }
            }
        }.start();
    }

    public void zipInstall() {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(SetupInit.getInstance().getDOWNLOAD_FOLDER());
        File selected_file = fc.showOpenDialog(StaticReferences.getMainWindowScene().getWindow());
        if (selected_file != null) {
            if (Installer.generate(selected_file, pl).installZipComplete()) {
                DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Installazione completata", "Il plugin è stato installato.\n Riavviare il programma per applicare le modifiche.", null);
            } else {
                DialogCreator.showAlert(Alert.AlertType.ERROR, "Installazione fallita", "Non è stato possibile installare il plugin selezionato.", null);
            }
        }
    }

    public void downloadSuccess(Runnable r) {
        taskHB.setVisible(false);
        taskPB.progressProperty().unbind();
        taskLB.textProperty().unbind();
        if (r != null) {
            r.run();
//        autoInstallHL.setDisable(false);
        }
        DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Scaricamento completato", "Il plugin è stato scaricato.", null);
        taskHB.setVisible(false);
    }

    public void installSuccess() {
        DialogCreator.showAlert(Alert.AlertType.INFORMATION, "Installazione completata", "Il plugin è stato scaricato e installato.\n Riavviare il programma per applicare le modifiche.", null);
        taskHB.setVisible(false);
    }

    public void startDownload(WebConnection.DownloadTask dt) {
        taskHB.setVisible(true);
        taskPB.progressProperty().bind(dt.progressProperty());
        taskLB.textProperty().bind(dt.messageProperty());
        Thread td = new Thread(dt);
        td.start();
    }

    public void onDownloadEnd(WebConnection.DownloadTask dt, PluginDownloadData pd, FutureCallable<Void> onRefresh) {
        taskHB.setVisible(false);
        taskPB.progressProperty().unbind();
        taskLB.textProperty().unbind();
//        autoInstallHL.setDisable(false);
        Installer.generate(dt.getOutput(), pl).installFrom(pd);
        refresh(onRefresh);
    }

    public void onDownloadEnd(WebConnection.DownloadTask dt, LibraryDownloadData pd) {
        taskHB.setVisible(false);
        taskPB.progressProperty().unbind();
        taskLB.textProperty().unbind();
        Installer.generate(dt.getOutput(), pl).installLibraries(pd);
    }

}
