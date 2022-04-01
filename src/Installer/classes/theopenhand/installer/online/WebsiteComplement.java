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
package theopenhand.installer.online;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.util.UUID;
import theopenhand.installer.online.store.PluginDownloadData;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import theopenhand.installer.SetupInit;
import theopenhand.installer.online.store.LibraryDownloadData;
import theopenhand.installer.online.store.PluginStore;
import theopenhand.installer.utils.WebConnection;
import theopenhand.installer.utils.WebConnection.DownloadTask;
import theopenhand.statics.privates.StaticData;
import theopenhand.window.graphics.creators.DialogCreator;

/**
 *
 * @author gabri
 */
public class WebsiteComplement {

    private static final String VIEW_PLUGIN_STORE = "https://vnl-eng.net/sections/TCE/ncdb/store/store.php?t=1";
    private static final String VIEW_LIBRARY_STORE = "https://vnl-eng.net/sections/TCE/ncdb/store/store.php?t=4";
    private static final String VIEW_PLUGIN_DESCRIPTION_STORE = "https://vnl-eng.net/sections/TCE/ncdb/store/store.php?t=2&d=";
    //private static final String VIEW_LIBRARY_DESCRIPTION_STORE = "https://vnl-eng.net/sections/TCE/ncdb/store/store.php?t=5&d=";
    private static final String DOWNLOAD_PLUGIN = "https://vnl-eng.net/sections/TCE/ncdb/store/";
    private static final String VIEW_VERSION_PROGRAMM = "https://vnl-eng.net/sections/TCE/ncdb/program/program.php?t=4";
    private static final String DOWNLOAD_PROGRAMM = "https://vnl-eng.net/sections/TCE/ncdb/program/program.php?t=5&d=";
    private static final String DOWNLOAD_OUTER_HAND = "https://vnl-eng.net/sections/TCE/ncdb/program/program.php?t=6";

    public WebsiteComplement() {
    }

    public JsonObject sendProgramUpdateRequest() {
        String s = WebConnection.comunicate(VIEW_VERSION_PROGRAMM, "");
        if (s != null) {
            JsonObject jo = JsonParser.parseString(s).getAsJsonObject();
            return jo;
        } else {
            DialogCreator.showAlert(Alert.AlertType.WARNING, "Connessione fallita", "Impossibile trovare aggiornamenti.", null);
        }
        return null;
    }

    public void sendViewRequest(PluginStore ps) {
        try {
            String s = WebConnection.comunicate(VIEW_PLUGIN_STORE, "");
            if (s != null) {
                JsonObject arr2 = (JsonObject) JsonParser.parseString(s);
                arr2.entrySet().forEach(e -> {
                    JsonObject jo = e.getValue().getAsJsonObject();
                    PluginDownloadData pd = new PluginDownloadData();
                    pd.setUuid(e.getKey());
                    pd.setDownload_path(jo.getAsJsonPrimitive("down_path").getAsString());
                    pd.setName(jo.getAsJsonPrimitive("pknm").getAsString());
                    pd.setZip_path(jo.getAsJsonPrimitive("zip_path").getAsString());
                    pd.setInj_point(jo.getAsJsonPrimitive("injnm").getAsString());
                    pd.setVersion(jo.getAsJsonPrimitive("ver").getAsInt());
                    if (jo.has("req")) {
                        jo.getAsJsonArray("req").forEach(je -> {
                            pd.addRequires(UUID.fromString(je.getAsString()));
                        });
                    }
                    if (jo.has("libs")) {
                        if (jo.isJsonArray()) {
                            jo.getAsJsonArray("libs").forEach(je -> {
                                pd.addLibrary(UUID.fromString(je.getAsString()));
                            });
                        } else if (jo.get("libs").isJsonPrimitive()) {
                            pd.addLibrary(UUID.fromString(jo.get("libs").getAsString()));
                        }
                    }
                    ps.addPlugin(pd);
                });
            } else {
                DialogCreator.showAlert(Alert.AlertType.WARNING, "Connessione fallita", "Impossibile connettersi al Plugin Store.", null);
            }
            s = WebConnection.comunicate(VIEW_LIBRARY_STORE, "");
            if (s != null) {
                JsonObject arr2 = (JsonObject) JsonParser.parseString(s);
                arr2.entrySet().forEach(e -> {
                    JsonObject jo = e.getValue().getAsJsonObject();
                    LibraryDownloadData ld = new LibraryDownloadData();
                    ld.setUuid(e.getKey());
                    ld.setDownload_path(jo.getAsJsonPrimitive("down_path").getAsString());
                    ld.setZip_path(jo.getAsJsonPrimitive("zip_path").getAsString());
                    ld.setVersion(jo.getAsJsonPrimitive("ver").getAsInt());
                    if (jo.has("link")) {
                        ld.setLink_zip_name(jo.getAsJsonPrimitive("link").getAsString());
                    }
                    if (jo.has("install")) {
                        ld.setInstall_zip_name(jo.getAsJsonPrimitive("install").getAsString());
                    }
                    ps.addLibrary(ld);
                });
            } else {
                DialogCreator.showAlert(Alert.AlertType.WARNING, "Connessione fallita", "Impossibile connettersi al Plugin Store.", null);
            }
        } catch (JsonSyntaxException ex) {
            Logger.getLogger(WebsiteComplement.class.getName()).log(Level.SEVERE, null, ex);
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore interno", "Impossibile comunicare correttamente con il Plugin Store.", null);
        }
    }

    public void sendDescriptionRequest(PluginDownloadData pd) {
        String s = WebConnection.comunicate(VIEW_PLUGIN_DESCRIPTION_STORE + pd.getUuid().toString(), "");
        if (s != null) {
            try {
                JsonObject jo = JsonParser.parseString(s).getAsJsonObject();
                pd.setDescription(jo.getAsJsonPrimitive("description").getAsString());
            } catch (JsonSyntaxException ex) {
                Logger.getLogger(WebsiteComplement.class.getName()).log(Level.SEVERE, null, ex);
                DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore interno", "Impossibile comunicare correttamente con il Plugin Store.", null);
            }
        } else {
            DialogCreator.showAlert(Alert.AlertType.WARNING, "Connessione fallita", "Impossibile connettersi al Plugin Store.", null);
        }
    }

    public DownloadTask sendDownloadRequest(PluginDownloadData pd) {
        return new DownloadTask(DOWNLOAD_PLUGIN + pd.getDownload_path(), pd.getName(), SetupInit.getInstance().getDOWNLOAD_FOLDER());
    }

    public DownloadTask sendDownloadRequest(LibraryDownloadData pd) {
        return new DownloadTask(DOWNLOAD_PLUGIN + pd.getDownload_path(), pd.getUuid().toString(), SetupInit.getInstance().getDOWNLOAD_FOLDER());
    }

    public DownloadTask sendDownloadRequest(Long programm_version) {
        return new DownloadTask(DOWNLOAD_PROGRAMM + programm_version, StaticData.ZIP_UPDATE_PROGRAMM_NAME, SetupInit.getInstance().getDOWNLOAD_FOLDER());
    }

    public DownloadTask sendDownloadRequestOuterHand() {
        return new DownloadTask(DOWNLOAD_OUTER_HAND, StaticData.ZIP_UPDATE_OUTER_HANDS_NAME, SetupInit.getInstance().getDOWNLOAD_FOLDER());
    }

    public long sendVersionRequest() {
        JsonObject jo = sendProgramUpdateRequest();
        return jo != null ? jo.get("version").getAsLong() : -1;
    }
}
