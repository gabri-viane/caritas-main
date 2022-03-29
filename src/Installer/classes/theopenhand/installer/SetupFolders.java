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

/**
 *
 * @author gabri
 */
public class SetupFolders {

    private SetupFolders() {

    }

    private static final String MAIN_FOLDER_NAME = ".ncdb";
    private static final String PLUGINS_XML_FILE_NAME = "plugins.xml";
    private static final String PLUGINS_FOLDER_NAME = "linker";
    private static final String PLUGINS_DATA_FOLDER_NAME = "pds";
    private static final String SETTINGS_FOLDER_NAME = "data";
    private static final String GENERAL_SETTINGS_FILE_NAME = "_s";
    private static final String CONNECTION_SETTINGS_FILE_NAME = "_c";
    private static final String PLUGINS_SETTINGS_FILE_NAME = "_p";

    private static final String LIBRARIES_FOLDER_NAME = "libs";
    private static final String DOWNLOAD_FOLDER_NAME = "dl";
    private static final String LOGS_FOLDER_NAME = "logs";

    /**
     * Cartella principale di salvataggio del programma
     */
    public static final String PATH_TO_DATA = System.getProperty("user.home") + File.separatorChar + MAIN_FOLDER_NAME;
    /**
     * Cartella principale di salvataggio dei logs
     */
    public static final String PATH_TO_LOGS = PATH_TO_DATA + File.separatorChar + LOGS_FOLDER_NAME;
    /**
     * Cartella per i download temporanei
     */
    public static final String PATH_TO_DOWNLOADS = PATH_TO_DATA + File.separatorChar + DOWNLOAD_FOLDER_NAME;

    /**
     * Cartella per i download temporanei
     */
    public static final String PATH_TO_LIBRARIES = PATH_TO_DATA + File.separatorChar + LIBRARIES_FOLDER_NAME;

    /**
     * *************************** *
     * CARTELLE E FILE PER LE IMPOSTAZIONI ************************
     */
    /**
     * Cartella di salvataggio impostazioni generali e specifiche del programma
     */
    public static final String SETTINGS_PATH_FOLDER = PATH_TO_DATA + File.separatorChar + SETTINGS_FOLDER_NAME;
    /**
     * File di salvataggio impostazioni del programma : solo impostazioni base
     * del programma (interfaccia)
     */
    public static final String GENERAL_SETTINGS_PATH_FILE = SETTINGS_PATH_FOLDER + File.separatorChar + GENERAL_SETTINGS_FILE_NAME;
    /**
     * File di salvataggio impostazioni di connessione : solo per sito internet
     * e database
     */
    public static final String CONNECTIONS_SETTINGS_PATH_FILE = SETTINGS_PATH_FOLDER + File.separatorChar + CONNECTION_SETTINGS_FILE_NAME;
    /**
     * File di salvataggio impostazioni dei plugin : come l'ordine di
     * caricamento dei plugins, delle accorditions,...
     */
    public static final String PLUGINS_SETTINGS_PATH_FILE = SETTINGS_PATH_FOLDER + File.separatorChar + PLUGINS_SETTINGS_FILE_NAME;

    /**
     * *************************** *
     * CARTELLE E FILE PER PLUGINS ************************
     */
    /**
     * Cartella contenente tutti gli elementi dei plugin
     */
    public static final String PLUGINS_PATH_FOLDER = PATH_TO_DATA + File.separatorChar + PLUGINS_FOLDER_NAME;
    /**
     * Cartella contenente i dati di salvataggio dei plugins
     */
    public static final String PLUGINS_DATA_PATH_FOLDER = PLUGINS_PATH_FOLDER + File.separatorChar + PLUGINS_DATA_FOLDER_NAME;
    /**
     * File contenente i percorsi per i file .jar dei plugin e gli UUID
     */
    public static final String XML_INSTRUCTION_PATH_FILE = PLUGINS_PATH_FOLDER + File.separatorChar + PLUGINS_XML_FILE_NAME;
}
