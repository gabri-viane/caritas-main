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
package theopenhand.installer.plugins;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import theopenhand.installer.plugins.store.PluginData;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import theopenhand.installer.SetupInit;
import theopenhand.installer.plugins.store.PluginStore;
import theopenhand.installer.utils.WebConnection;
import theopenhand.installer.utils.WebConnection.DownloadTask;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class WebsiteComplement {

    private static final String VIEW_PLUGIN_STORE = "https://vnl-eng.net/sections/TCE/ncdb/store/store.php?t=1";
    private static final String VIEW_PLUGIN_DESCRIPTION_STORE = "https://vnl-eng.net/sections/TCE/ncdb/store/store.php?t=2&d=";
    private static final String DOWNLOAD_PLUGIN = "https://vnl-eng.net/sections/TCE/ncdb/store/";

    public WebsiteComplement() {
    }

    public void sendViewRequest(PluginStore ps) {
        try {
            String s = WebConnection.comunicate(VIEW_PLUGIN_STORE, "");
            if (s != null) {
                JsonObject arr2 = (JsonObject) JsonParser.parseString(s);
                arr2.entrySet().forEach(e -> {
                    JsonObject jo = e.getValue().getAsJsonObject();
                    PluginData pd = new PluginData();
                    pd.setUuid(e.getKey());
                    pd.setDownload_path(jo.getAsJsonPrimitive("down_path").getAsString());
                    pd.setName(jo.getAsJsonPrimitive("pknm").getAsString());
                    pd.setZip_path(jo.getAsJsonPrimitive("zip_path").getAsString());
                    pd.setInj_point(jo.getAsJsonPrimitive("injnm").getAsString());
                    pd.setVersion(jo.getAsJsonPrimitive("ver").getAsInt());
                    ps.addPlugin(pd);
                });
            } else {
                DialogCreator.showAlert(Alert.AlertType.WARNING, "Connessione fallita", "Impossibile connettersi al Plugin Store.", null);
            }
        } catch (JsonSyntaxException ex) {
            Logger.getLogger(WebsiteComplement.class.getName()).log(Level.SEVERE, null, ex);
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore interno", "Impossibile comunicare correttamente con il Plugin Store.", null);
        }
    }

    public void sendDescriptionRequest(PluginData pd) {
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

    public DownloadTask sendDownloadRequest(PluginData pd) {
        return new DownloadTask(DOWNLOAD_PLUGIN + pd.getDownload_path(), pd.getName(), SetupInit.getInstance().getDOWNLOAD_FOLDER());
    }
}
