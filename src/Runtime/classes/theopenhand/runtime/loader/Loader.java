/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.runtime.loader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import theopenhand.commons.Pair;
import theopenhand.commons.events.programm.utils.ListEventListener;
import theopenhand.runtime.SubscriptionHandler;
import theopenhand.runtime.ambient.DataEnvironment;
import theopenhand.runtime.loader.folder.PluginFolderHandler;
import theopenhand.runtime.loader.folder.xml.PluginElement;
import theopenhand.runtime.templates.LinkableClass;
import theopenhand.runtime.templates.RuntimeReference;

/**
 *
 * @author gabri
 */
public class Loader {

    private static Loader instance;

    private final HashMap<UUID, ArrayList<RuntimeReference>> loaded_references;
    private final HashMap<UUID, LinkableClass> loaded_classes;
    private final HashMap<UUID, SettingsLoader> loaded_settings;
    private final HashMap<UUID, PluginElement> loaded_plugins;
    private Pair<ArrayList<Pair<File, PluginElement>>, ArrayList<PluginElement>> readPluginData;

    private final PluginFolderHandler pfh;
    private final DataEnvironment de;
    private ClassLoader loader;
    private UUID current_UUID;

    private Loader() {
        pfh = PluginFolderHandler.getInstance();
        de = DataEnvironment.getInstance();
        loaded_classes = new HashMap<>();
        loaded_settings = new HashMap<>();
        loaded_plugins = new HashMap<>();
        loaded_references = new HashMap<>();
        SubscriptionHandler.addListener(new ListEventListener<RuntimeReference>() {
            @Override
            public void onElementAdded(RuntimeReference element) {
                if (current_UUID != null) {
                    loaded_references.get(current_UUID).add(element);
                }
            }

            @Override
            public void onElementRemoved(RuntimeReference element) {

            }
        });
    }

    /**
     *
     * @return
     */
    public static Loader getInstance() {
        if (instance == null) {
            instance = new Loader();
        }
        return instance;
    }

    /**
     *
     */
    public void activate() {
        readPluginData = pfh.readPluginData();
        createClassLoader(readPluginData.getKey());
        readPluginData.getKey().forEach(p -> loadPlugin(p));
        DBResultLoader.getInstance();
    }

    private void createClassLoader(ArrayList<Pair<File, PluginElement>> jar_files) {
        URL[] arr = new URL[jar_files.size()];
        for (int i = 0; i < arr.length; i++) {
            try {
                arr[i] = jar_files.get(i).getKey().toURI().toURL();
            } catch (MalformedURLException ex) {
                Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        loader = URLClassLoader.newInstance(arr, getClass().getClassLoader());
    }

    private void loadPlugin(Pair<File, PluginElement> p) {
        try {
            PluginElement pe = p.getValue();
            Class<?> clazz = Class.forName(pe.getClass_path(), true, loader);
            Class<? extends LinkableClass> runClass = clazz.asSubclass(LinkableClass.class);
            Constructor<?> ctor = runClass.getConstructor();
            var id = pe.getUUID();
            loaded_plugins.put(id, pe);
            loadClass(ctor, id);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadClass(Constructor<?> ctor, UUID uuid) {
        try {
            LinkableClass lc = (LinkableClass) ctor.newInstance();
            current_UUID = uuid;
            loaded_references.put(uuid, new ArrayList<>());
            var pe = de.createEnv(lc, uuid);
            lc.load();
            current_UUID = null;
            loaded_classes.put(uuid, lc);
            loaded_settings.put(uuid, pe.getSettingsLoader());
            Logger.getLogger(Loader.class.getName()).log(Level.INFO, "Caricato con successo il plugin: {0}", uuid);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return
     */
    public Set<UUID> getUUIDS() {
        return loaded_classes.keySet();
    }

    /**
     *
     * @param id
     * @return
     */
    public LinkableClass findLinkedClass(UUID id) {
        return loaded_classes.get(id);
    }

    /**
     *
     * @param id
     * @return
     */
    public SettingsLoader findSettings(UUID id) {
        return loaded_settings.get(id);
    }

    /**
     *
     * @param id
     * @return
     */
    public String findPluginName(UUID id) {
        return findPlugin(id).getPlugin_name();
    }

    public String findPluginVersion(UUID id) {
        return findPlugin(id).getVersion();
    }

    private PluginElement findPlugin(UUID id) {
        return loaded_plugins.get(id);
    }

    /**
     *
     * @param id
     * @return
     */
    public ArrayList<RuntimeReference> getRuntimeRefrences(UUID id) {
        return loaded_references.get(id);
    }

}
