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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import theopenhand.commons.Pair;
import theopenhand.installer.SetupInit;
import theopenhand.installer.interfaces.PluginHandler;
import theopenhand.installer.online.update.PluginAutoupdate;
import theopenhand.runtime.ambient.DataEnvironment;
import theopenhand.runtime.loader.Loader;
import theopenhand.runtime.loader.folder.xml.DependsElement;
import theopenhand.runtime.loader.folder.xml.LibraryElement;
import theopenhand.runtime.loader.folder.xml.PluginLoaderElement;
import theopenhand.runtime.loader.folder.xml.PluginsLoaderElement;
import theopenhand.runtime.templates.LinkableClass;
import theopenhand.statics.StaticReferences;
import ttt.utils.xml.document.XMLDocument;
import ttt.utils.xml.engine.XMLEngine;
import ttt.utils.xml.engine.interfaces.IXMLElement;
import ttt.utils.xml.io.XMLReader;
import ttt.utils.xml.io.XMLWriter;

/**
 * Gestisce la cartella "link" che contiene tutti i plugin e il file XML che
 * contiene le informazioni di caricamento.
 * <h3>Singleton</h3>
 * Esiste una sola instanza di questa classe ed è possibile accedervi tramite il
 * metodo {@link #getInstance() }. L'instanza viene creata la prima volta che
 * questo metodo viene chiamato, di partenza non esiste.
 * <h3>Inizializzazione</h3>
 * Quando la classe viene instanziata viene letto il file xml che contiene le
 * informazioni dei plugin e vengono creati i relativi
 * {@link PluginLoaderElement}
 * <h3>Uscita</h3>
 * Questa classe è stottoscritta all'evento di uscita dal programma: alla
 * chiusura del programma viene chiamato il metodo {@link #flush() }
 *
 *
 * @author gabri
 */
public class PluginFolderHandler implements PluginHandler {

    private static File XML_FILE;
    private XMLDocument main_doc;

    private final ArrayList<Pair<File, PluginLoaderElement>> plugin_files;
    private final ArrayList<PluginLoaderElement> not_found;
    private final HashMap<UUID, PluginLoaderElement> registered;
    private final ArrayList<PluginLoaderElement> load_order;

    private final HashMap<UUID, LinkedList<UUID>> dependes_on;
    private final HashMap<UUID, LinkedList<UUID>> requires_libs;
    private final ArrayList<UUID> to_update = new ArrayList<>();

    private static PluginFolderHandler instance;

    private PluginFolderHandler() {
        plugin_files = new ArrayList<>();
        not_found = new ArrayList<>();
        load_order = new ArrayList<>();
        registered = new HashMap<>();
        dependes_on = new HashMap<>();
        requires_libs = new HashMap<>();
        init();
        StaticReferences.subscribeOnExit(() -> {
            flush();
        });
    }

    /**
     * Ritorna (e nel caso di prima chiamata la crea) l'instanza di questa
     * classe.
     *
     * @return L'instanza unica.
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
                main_doc.addSubElement(new PluginsLoaderElement());
                xmlw.writeDocument(main_doc, true);
            } catch (IOException ex) {
                Logger.getLogger(PluginFolderHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                XMLReader xmlr = new XMLReader(XML_FILE);
                XMLEngine engine = new XMLEngine(xmlr, PluginLoaderElement.class, PluginsLoaderElement.class, DependsElement.class, LibraryElement.class);
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
    public Pair<ArrayList<Pair<File, PluginLoaderElement>>, ArrayList<PluginLoaderElement>> readPluginData() {
        plugin_files.clear();
        not_found.clear();
        main_doc.getRoot().getElements().forEach(e -> {
            PluginLoaderElement pe = (PluginLoaderElement) e;
            PluginAutoupdate pa = new PluginAutoupdate(pe.getUUID());
            int v = Integer.parseInt(pe.getVersion());
            registerDeps(pe);
            registered.put(pe.getUUID(), pe);
            //Se non è da aggiornare lo carico, altrimenti lo aggiungo alla lista da scaricare
            if (!pa.toUpdate(v)) {
                String file_path = pe.getFile_path();
                File f = new File(file_path);
                if (f.exists() && f.isFile() && f.getName().endsWith(".jar")) {
                    try {
                        JarFile jf = new JarFile(f);
                        Manifest manifest = jf.getManifest();
                        if (manifest != null) {
                            load_order.add(pe);
                            plugin_files.add(new Pair(f, pe));
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(PluginFolderHandler.class.getName()).log(Level.SEVERE, null, ex);
                        not_found.add(pe);
                    }
                } else {
                    not_found.add(pe);
                }
            } else {
                to_update.add(pa.getUUID());
            }
        });
        return new Pair(plugin_files, not_found);
    }

    public ArrayList<PluginLoaderElement> getLoadOrder() {
        return load_order;
    }

    public void reorder(ArrayList<PluginLoaderElement> ple) {
        main_doc.getRoot().getElements().clear();
        main_doc.getRoot().getElements().addAll(ple);
    }

    private void registerDeps(PluginLoaderElement pe) {
        List<UUID> dependencies = pe.getDependencies();
        for (UUID uid : dependencies) {
            if (dependes_on.containsKey(uid)) {
                dependes_on.get(uid).add(pe.getUUID());
            } else {
                LinkedList<UUID> ll = new LinkedList<>();
                ll.add(pe.getUUID());
                dependes_on.put(uid, ll);
            }
        }
    }

    private void registerLibs(PluginLoaderElement pe) {
        List<UUID> libraries = pe.getLibraries();
        for (UUID uid : libraries) {
            if (requires_libs.containsKey(uid)) {
                requires_libs.get(uid).add(pe.getUUID());
            } else {
                LinkedList<UUID> ll = new LinkedList<>();
                ll.add(pe.getUUID());
                requires_libs.put(uid, ll);
            }
        }
    }

    public List<UUID> getRequiredBy(UUID id) {
        return dependes_on.get(id);
    }

    /**
     *
     * @param f
     * @param name
     * @param ver
     * @param path_to_class
     */
    @Override
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
    @Override
    public void addPluginData(File f, String name, String path_to_class, String ver, UUID uid) {
        addPluginData(f, name, ver, path_to_class, uid, new ArrayList<>());
    }

    /**
     * Aggiunge i dati di un plugin al file {@link SetupInit#PLUGINS_XML}
     *
     * @param f
     * @param name
     * @param path_to_class
     * @param ver
     * @param uid
     * @param dependencies
     */
    @Override
    public void addPluginData(File f, String name, String path_to_class, String ver, UUID uid, ArrayList<UUID> dependencies) {
        addPluginData(f, name, path_to_class, ver, uid, dependencies, new ArrayList<>());
    }

    /**
     * Aggiunge i dati di un plugin al file {@link SetupInit#PLUGINS_XML}
     *
     * @param f
     * @param name
     * @param path_to_class
     * @param ver
     * @param uid
     * @param dependencies
     */
    @Override
    public void addPluginData(File f, String name, String path_to_class, String ver, UUID uid, ArrayList<UUID> dependencies, ArrayList<UUID> libraries) {
        if (registered.containsKey(uid)) {
            PluginLoaderElement pl = registered.get(uid);
            pl.setPlugin_name(name);
            pl.setFile_path(f.getAbsolutePath());
            pl.setClass_path(path_to_class);
            pl.setVersion(ver);
        } else if (f != null && name != null && path_to_class != null && uid != null) {
            PluginLoaderElement pl = new PluginLoaderElement(f.getAbsolutePath(), name, path_to_class, ver, uid);
            if (dependencies != null) {
                pl.addDependencies(dependencies);
                registerDeps(pl);
            }
            if (libraries != null) {
                pl.addLibraries(libraries);
                registerLibs(pl);
            }
            main_doc.getRoot().addSubElement(pl);
            registered.put(uid, pl);
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
    @Override
    public void removePluginData(UUID uuid) {
        Optional<IXMLElement> findFirst = main_doc.getRoot().getElements().stream().filter((t) -> {
            PluginLoaderElement pe = (PluginLoaderElement) t;
            return pe.getUUID().equals(uuid);
        }).findFirst();
        findFirst.ifPresent((t) -> {
            PluginLoaderElement pe = (PluginLoaderElement) t;
            LinkableClass ps = Loader.getInstance().findLinkedClass(uuid);
            if (ps != null) {
                ps.unload();
            }
            main_doc.getRoot().removeSubElement(pe);
            registered.remove(pe.getUUID());
        });
    }

    /**
     *
     * @param pe
     */
    public void removePluginData(PluginLoaderElement pe) {
        removePluginData(pe.getUUID());
    }

    /**
     *
     * @return
     */
    public Pair<ArrayList<Pair<File, PluginLoaderElement>>, ArrayList<PluginLoaderElement>> getAvailablePlugins() {
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

    @Override
    public boolean installed(UUID uid) {
        return registered.containsKey(uid);
    }

    public ArrayList<UUID> getToUpdate() {
        return to_update;
    }

}
