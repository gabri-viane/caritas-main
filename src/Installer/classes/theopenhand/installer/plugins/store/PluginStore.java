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
package theopenhand.installer.plugins.store;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import theopenhand.installer.plugins.WebsiteComplement;
import theopenhand.installer.utils.WebConnection;

/**
 *
 * @author gabri
 */
public class PluginStore {

    private final HashMap<UUID, PluginData> availables;
    private static PluginStore instance;
    private final WebsiteComplement wc;

    private PluginStore() {
        availables = new HashMap<>();
        wc = new WebsiteComplement();
    }

    public void refresh() {
        availables.clear();
        wc.sendViewRequest(this);
    }

    public static PluginStore getInstance() {
        if (instance == null) {
            instance = new PluginStore();
        }
        return instance;
    }

    public void addPlugin(PluginData pd) {
        availables.put(pd.getUuid(), pd);
    }

    public Map<UUID, PluginData> getPlugins() {
        return Collections.unmodifiableMap(availables);
    }

    public PluginData getPlugin(UUID uid) {
        return availables.get(uid);
    }

    public void retriveDescription(PluginData pd) {
        if (pd != null) {
            wc.sendDescriptionRequest(pd);
        }
    }
    
    public WebConnection.DownloadTask download(PluginData pd){
        if(pd!=null){
            return wc.sendDownloadRequest(pd);
        }
        return null;
    }

}
