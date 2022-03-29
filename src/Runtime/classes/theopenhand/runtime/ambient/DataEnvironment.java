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
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipFile;
import theopenhand.commons.events.programm.DataRequest;
import theopenhand.installer.SetupFolders;
import theopenhand.installer.utils.Installer;
import theopenhand.runtime.data.DataElement;
import theopenhand.runtime.data.SubscribeData;
import theopenhand.runtime.loader.Loader;
import theopenhand.runtime.loader.SettingsLoader;
import theopenhand.runtime.templates.LinkableClass;
import theopenhand.runtime.templates.Settings;

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
        SubscribeData.setFCData(new DataRequest<DataElement, String, Serializable>() {
            @Override
            public DataElement onRequest(Settings st, String data) {
                if (st != null && data != null) {
                    return envs.get(st.getUUID()).getData(data + ".data");
                }
                return null;
            }

            @Override
            public DataElement onSubscribe(Settings st, String data, Serializable data2) {
                if (st != null && data != null && data2 != null) {
                    DataElement de = new DataElement(data, data2);
                    envs.get(st.getUUID()).registerData(de);
                    return de;
                }
                return null;
            }
        });
        SubscribeData.setFCFiles(new DataRequest<File, String, File>() {
            @Override
            public File onRequest(Settings st, String data) {
                if (st != null && data != null) {
                    File folderRetriver = folderRetriver(st.getUUID());
                    return new File(folderRetriver.getAbsolutePath() + File.separatorChar + data);
                }
                return null;
            }

            @Override
            public File onSubscribe(Settings st, String data, File data2) {
                try {
                    File to_save = new File(folderRetriver(st.getUUID()).getAbsolutePath() + File.separatorChar + data);
                    Files.copy(data2.toPath(), to_save.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    DataElement de = new DataElement(data, data2);
                    envs.get(st.getUUID()).registerData(de);
                    return to_save;
                } catch (IOException ex) {
                    Logger.getLogger(DataEnvironment.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }
        });
        SubscribeData.setFCExtraction(new DataRequest<Void, ZipFile, String>() {
            @Override
            public Void onRequest(Settings st, ZipFile data) {
                return null;
            }

            @Override
            public Void onSubscribe(Settings st, ZipFile data, String data2) {
                if (st != null) {
                    ArrayList<File> zipExtract = Installer.zipExtract(data, folderRetriver(st.getUUID()));
                    DataElement refs = new DataElement(data2 + ".data", zipExtract);
                    envs.get(st.getUUID()).registerData(refs);
                }
                return null;
            }
        });
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
