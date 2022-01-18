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
package theopenhand.runtime.loader.folder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import theopenhand.commons.Pair;
import theopenhand.installer.SetupInit;
import theopenhand.runtime.ambient.DataEnvironment;
import theopenhand.runtime.loader.Loader;
import theopenhand.runtime.loader.folder.xml.PluginElement;
import theopenhand.runtime.loader.folder.xml.PluginsElement;
import ttt.utils.xml.document.XMLDocument;
import ttt.utils.xml.engine.XMLEngine;
import ttt.utils.xml.engine.interfaces.IXMLElement;
import ttt.utils.xml.io.XMLReader;
import ttt.utils.xml.io.XMLWriter;

/**
 *
 * @author gabri
 */
public class PluginFolderHandler {

    private static File XML_FILE;
    private XMLDocument main_doc;

    private final ArrayList<Pair<File, PluginElement>> plugin_files;
    private final ArrayList<PluginElement> not_found;

    private static PluginFolderHandler instance;

    private PluginFolderHandler() {
        plugin_files = new ArrayList<>();
        not_found = new ArrayList<>();
        init();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                flush();
            }
        });
    }

    /**
     *
     * @return
     */
    public static PluginFolderHandler getInstance() {
        if (instance == null) {
            instance = new PluginFolderHandler();
        }
        return instance;
    }

    private void init() {
        XML_FILE = SetupInit.getInstance().getPLUGINS_XML();
        if (!XML_FILE.exists()) {
            try {
                XML_FILE.createNewFile();
                XMLWriter xmlw = new XMLWriter(XML_FILE);
                main_doc = new XMLDocument(XML_FILE);
                main_doc.addSubElement(new PluginsElement());
                xmlw.writeDocument(main_doc, true);
            } catch (IOException ex) {
                Logger.getLogger(PluginFolderHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                XMLReader xmlr = new XMLReader(XML_FILE);
                XMLEngine engine = new XMLEngine(xmlr, PluginElement.class, PluginsElement.class);
                main_doc = new XMLDocument(XML_FILE);
                engine.morph(main_doc);
            } catch (IOException ex) {
                Logger.getLogger(PluginFolderHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @return
     */
    public Pair<ArrayList<Pair<File, PluginElement>>, ArrayList<PluginElement>> readPluginData() {
        plugin_files.clear();
        not_found.clear();
        main_doc.getRoot().getElements().forEach(e -> {
            PluginElement pe = (PluginElement) e;
            String file_path = pe.getFile_path();
            File f = new File(file_path);
            if (f.exists() && f.isFile() && f.getName().endsWith(".jar")) {
                try {
                    JarFile jf = new JarFile(f);
                    Manifest manifest = jf.getManifest();
                    if (manifest != null) {
                        plugin_files.add(new Pair(f, pe));
                    }
                } catch (IOException ex) {
                    Logger.getLogger(PluginFolderHandler.class.getName()).log(Level.SEVERE, null, ex);
                    not_found.add(pe);
                }
            } else {
                not_found.add(pe);
            }
        });
        return new Pair(plugin_files, not_found);
    }

    /**
     *
     * @param f
     * @param name
     * @param ver
     * @param path_to_class
     */
    public void addPluginData(File f, String name, String ver, String path_to_class) {
        addPluginData(f, name, path_to_class, ver, UUID.randomUUID());
    }

    /**
     *
     * @param f
     * @param name
     * @param path_to_class
     * @param ver
     * @param uid
     */
    public void addPluginData(File f, String name, String path_to_class, String ver, UUID uid) {
        if (f != null && name != null && path_to_class != null && uid != null) {
            PluginElement pl = new PluginElement(f.getAbsolutePath(), name, path_to_class, ver, uid);
            main_doc.getRoot().addSubElement(pl);
        }
    }

    /**
     *
     * @param uuid
     */
    public void removePluginData(String uuid) {
        removePluginData(UUID.fromString(uuid));
    }

    /**
     *
     * @param uuid
     */
    public void removePluginData(UUID uuid) {
        Optional<IXMLElement> findFirst = main_doc.getRoot().getElements().stream().filter((t) -> {
            PluginElement pe = (PluginElement) t;
            return pe.getUUID().equals(uuid);
        }).findFirst();
        findFirst.ifPresent((t) -> {
            PluginElement pe = (PluginElement) t;
            Loader.getInstance().findLinkedClass(uuid).unload();
            main_doc.getRoot().removeSubElement(pe);
        });
    }

    /**
     *
     * @param pe
     */
    public void removePluginData(PluginElement pe) {
        removePluginData(pe.getUUID());
    }

    /**
     *
     * @return
     */
    public Pair<ArrayList<Pair<File, PluginElement>>, ArrayList<PluginElement>> getAvailablePlugins() {
        return new Pair(plugin_files, not_found);
    }

    /**
     *
     */
    public void flush() {
        XMLWriter w = new XMLWriter(XML_FILE);
        w.writeDocument(main_doc, true);
        DataEnvironment.getInstance().saveAll();
    }

}
