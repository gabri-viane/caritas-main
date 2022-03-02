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
package theopenhand.installer.online.update;

import java.util.UUID;
import theopenhand.installer.online.store.PluginDownloadData;
import theopenhand.installer.online.store.PluginStore;
import theopenhand.installer.utils.WebConnection;

/**
 *
 * @author gabri
 */
public class PluginAutoupdate {

    private final UUID plugin_uuid;
    private PluginDownloadData online_data;

    public PluginAutoupdate(UUID pid) {
        plugin_uuid = pid;
        init();
    }

    private void init() {
        if (plugin_uuid != null) {
            online_data = PluginStore.getInstance().getPlugin(plugin_uuid);
        }
    }

    public int compare(Integer installed_ver) {
        if (online_data != null) {
            if (installed_ver != null) {
                Integer online_ver = online_data.getVersion();
                if (online_ver != null) {
                    if (online_ver.intValue() == installed_ver.intValue()) {
                        return 0;
                    } else if (online_ver > installed_ver) {
                        return -1;
                    } else if (online_ver < installed_ver) {
                        return 1;
                    }
                }
                return 1;
            }
            return -1;
        }
        return 0;
    }

    public WebConnection.DownloadTask update(Integer version) {
        if (compare(version) == -1) {
            WebConnection.DownloadTask download = PluginStore.getInstance().download(online_data);
            return download;
        }
        return null;
    }

}
