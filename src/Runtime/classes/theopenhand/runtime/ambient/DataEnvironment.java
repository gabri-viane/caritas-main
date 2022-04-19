/*
 * Copyright 2021 gabri.
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
package theopenhand.runtime.ambient;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import theopenhand.installer.SetupFolders;
import theopenhand.runtime.block.KeyUnlock;
import theopenhand.runtime.data.SubscribeData;
import theopenhand.runtime.loader.Loader;
import theopenhand.runtime.loader.SettingsLoader;
import theopenhand.runtime.templates.LinkableClass;

/**
 *
 * @author gabri
 */
public class DataEnvironment {

    private static DataEnvironment instance;
    private final HashMap<UUID, PluginEnv> envs;

    private DataEnvironment() {
        envs = new HashMap<>();
        init();
    }

    private void init() {
        SubscribeData.setAccept((param) -> {
            return envs.get(param.getUUID());
        }, KeyUnlock.KEY);
    }

    public static DataEnvironment getInstance() {
        if (instance == null) {
            instance = new DataEnvironment();
        }
        return instance;
    }

    private static File folderRetriver(UUID id) {
        if (id != null) {
            File folder = new File(SetupFolders.PLUGINS_DATA_PATH_FOLDER + File.separatorChar + id.toString());
            return folder;
        }
        return null;
    }

    public PluginEnv createEnv(LinkableClass lc, UUID id) {
        try {
            SettingsLoader sl = new SettingsLoader(lc, id);
            PluginEnv pe = new PluginEnv(folderRetriver(id));
            pe.setSettingsLoader(sl);
            envs.put(id, pe);
            return pe;
        } catch (IOException ex) {
            Logger.getLogger(DataEnvironment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void saveAll() {
        envs.forEach((u, p) -> {
            Loader.getInstance().findLinkedClass(u).unload();
            p.saveEnv();
        });
    }

}
