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
package theopenhand.installer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabri
 */
public final class SetupInit {

    private static SetupInit instance;

    private File MAIN_FOLDER;
    private File LOGS_FOLDER;
    private File DOWN_FOLDER;
    private File SETTINGS_FOLDER;
    private File GENERAL_SETTINGS;
    private File CONNECTIONS_SETTINGS;
    private File PLUGINS_SETTINGS;
    private File PLUGINS_FOLDER;
    private File PLUGINS_DATA_FOLDER;
    private File PLUGINS_XML;

    private SetupInit() {
        init();
    }

    /**
     *
     * @return
     */
    public static SetupInit getInstance() {
        if (instance == null) {
            instance = new SetupInit();
        }
        return instance;
    }

    private void init() {
        initGeneral();
        initSettings();
        initPlugins();
    }

    private void initGeneral() {
        MAIN_FOLDER = new File(SetupFolders.PATH_TO_DATA);
        LOGS_FOLDER = new File(SetupFolders.PATH_TO_LOGS);
        DOWN_FOLDER = new File(SetupFolders.PATH_TO_DOWNLOADS);
        SETTINGS_FOLDER = new File(SetupFolders.SETTINGS_PATH_FOLDER);
        PLUGINS_FOLDER = new File(SetupFolders.PLUGINS_PATH_FOLDER);
        if (!MAIN_FOLDER.exists() || !MAIN_FOLDER.isDirectory()) {
            MAIN_FOLDER.mkdir();
            SETTINGS_FOLDER.mkdir();
            PLUGINS_FOLDER.mkdir();
            LOGS_FOLDER.mkdir();
            DOWN_FOLDER.mkdir();
        } else {
            if (!SETTINGS_FOLDER.exists()) {
                SETTINGS_FOLDER.mkdir();
            }
            if (!PLUGINS_FOLDER.exists()) {
                PLUGINS_FOLDER.mkdir();
            }
            if (!LOGS_FOLDER.exists()) {
                LOGS_FOLDER.mkdir();
            }
            if (!DOWN_FOLDER.exists()) {
                DOWN_FOLDER.mkdir();
            }
        }
    }

    private void initSettings() {
        CONNECTIONS_SETTINGS = new File(SetupFolders.CONNECTIONS_SETTINGS_PATH_FILE);
        GENERAL_SETTINGS = new File(SetupFolders.GENERAL_SETTINGS_PATH_FILE);
        PLUGINS_SETTINGS = new File(SetupFolders.PLUGINS_SETTINGS_PATH_FILE);
        try {
            if (!GENERAL_SETTINGS.exists()) {
                GENERAL_SETTINGS.createNewFile();
            }
            if (!PLUGINS_SETTINGS.exists()) {
                PLUGINS_SETTINGS.createNewFile();
            }
            if (!CONNECTIONS_SETTINGS.exists()) {
                CONNECTIONS_SETTINGS.createNewFile();
            }
        } catch (IOException ex) {
            Logger.getLogger(SetupInit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initPlugins() {
        PLUGINS_DATA_FOLDER = new File(SetupFolders.PLUGINS_DATA_PATH_FOLDER);
        PLUGINS_XML = new File(SetupFolders.XML_INSTRUCTION_PATH_FILE);
        if (!PLUGINS_DATA_FOLDER.exists()) {
            PLUGINS_DATA_FOLDER.mkdir();
        }
        //Non creo il file XML dei plugin, lo so, guarda il file PluginFolderHandler nel modulo Runtime, metodo init(), devo usare
        //Le cose per scrivere il documento XML che qua non potrei...
    }

    /**
     *
     * @return
     */
    public File getMAIN_FOLDER() {
        return MAIN_FOLDER;
    }

    /**
     *
     * @return
     */
    public File getSETTINGS_FOLDER() {
        return SETTINGS_FOLDER;
    }

    /**
     *
     * @return
     */
    public File getDOWNLOAD_FOLDER() {
        return DOWN_FOLDER;
    }

    /**
     *
     * @return
     */
    public File getLOGS_FOLDER() {
        return LOGS_FOLDER;
    }

    /**
     *
     * @return
     */
    public File getGENERAL_SETTINGS() {
        return GENERAL_SETTINGS;
    }

    /**
     *
     * @return
     */
    public File getCONNECTIONS_SETTINGS() {
        return CONNECTIONS_SETTINGS;
    }

    /**
     *
     * @return
     */
    public File getPLUGINS_SETTINGS() {
        return PLUGINS_SETTINGS;
    }

    /**
     *
     * @return
     */
    public File getPLUGINS_FOLDER() {
        return PLUGINS_FOLDER;
    }

    /**
     *
     * @return
     */
    public File getPLUGINS_DATA_FOLDER() {
        return PLUGINS_DATA_FOLDER;
    }

    /**
     *
     * @return
     */
    public File getPLUGINS_XML() {
        return PLUGINS_XML;
    }

}
