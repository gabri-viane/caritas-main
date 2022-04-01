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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import theopenhand.commons.Pair;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.events.programm.OpExecutor;
import theopenhand.commons.events.programm.utils.ListEventListener;
import theopenhand.installer.SetupInit;
import theopenhand.runtime.SubscriptionHandler;
import theopenhand.runtime.ambient.DataEnvironment;
import theopenhand.runtime.ambient.PluginEnv;
import theopenhand.runtime.loader.folder.PluginFolderHandler;
import theopenhand.runtime.loader.folder.xml.PluginLoaderElement;
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
    private final HashMap<UUID, PluginLoaderElement> loaded_plugins;
    private Pair<ArrayList<Pair<File, PluginLoaderElement>>, ArrayList<PluginLoaderElement>> readPluginData;

    private final PluginFolderHandler pfh;
    private final DataEnvironment de;
    //private ClassLoader loader;
    private UUID current_UUID;

    private final LinkedHashMap<UUID, ClassLoader> loaders;

    private Loader() {
        pfh = PluginFolderHandler.getInstance();
        de = DataEnvironment.getInstance();
        loaded_classes = new HashMap<>();
        loaded_settings = new HashMap<>();
        loaded_plugins = new HashMap<>();
        loaded_references = new HashMap<>();
        loaders = new LinkedHashMap<>();
        SubscriptionHandler.addListener(new ListEventListener<RuntimeReference>() {
            @Override
            public void onElementAdded(RuntimeReference element) {
                if (current_UUID != null) {
                    SharedReferenceQuery.getInstance().registerRR(element);
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
     * Quando viene chiamato seguono i seguenti passaggi:
     * <ol>
     * <li>Viene chiamato {@link PluginFolderHandler#readPluginData() } e il
     * file {@link SetupInit#PLUGINS_XML} viene caricato.</li>
     * <li>Vengono aggiunti tutti i file jar, contenuti nei
     * {@link PluginLoaderElement} letti, al ClassLoader corrente.</li>
     * <li>Per ogni {@link PluginLoaderElement} letto viene caricata la
     * {@link LinkableClass} del plugin.</li>
     * <ol>
     * <li>Viene chiamato il costruttore della classe {@link LinkableClass}</li>
     * <li>Viene chiamato il metodo {@link DataEnvironment#createEnv(theopenhand.runtime.templates.LinkableClass, java.util.UUID)
     * }</li>
     * <li>Viene chiamato il metodo {@link LinkableClass#load() } (in cui
     * teoricamente il plugin aggiunge le {@link RuntimeReference} e tutte le
     * {@link BindableResult} associate)</li>
     * <li>Viene chiamato il metodo {@link PluginEnv#getSettingsLoader() } e
     * salvato il risultato ({@link SettingsLoader})</li>
     * </ol>
     * </ol>
     * Vengno perciò salvati per ogni plugin (associati al UUID):
     * <ul>
     * <li>{@link PluginLoaderElement}</li>
     * <li>Un ArrayList di {@link RuntimeReference} registrate dal plugin</li>
     * <li>{@link LinkableClass}</li>
     * <li>{@link SettingsLoader} preso dal {@link PluginEnv} creato</li>
     * </ul>
     */
    public void activate() {
        readPluginData = pfh.readPluginData();
        createClassLoader(readPluginData.getKey());
        readPluginData.getKey().forEach(p -> loadPlugin(p));
        DBResultLoader.getInstance();
    }

    public OpExecutor<Void> remove(UUID uid) {
        return () -> {
            var pid = findPlugin(uid);
            pfh.removePluginData(uid);
            List<UUID> requiredBy = pfh.getRequiredBy(uid);
            if (requiredBy != null) {
                requiredBy.forEach(uuid -> {
                    remove(uuid).onCall();
                });
            }
            loaders.remove(uid);
            System.gc();
            new File(pid.getFile_path()).deleteOnExit();//TODO: Classe che segni che file sono da eliminare al prossimo avvio
            return null;
        };
    }

    private void createClassLoader(ArrayList<Pair<File, PluginLoaderElement>> jar_files) {
        /*
        URL[] arr = new URL[jar_files.size()];
        for (int i = 0; i < arr.length; i++) {
            try {
                arr[i] = jar_files.get(i).getKey().toURI().toURL();
            } catch (MalformedURLException ex) {
                Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        loader = URLClassLoader.newInstance(arr, getClass().getClassLoader());*/
        jar_files.forEach(p -> {
            try {
                loaders.put(p.getValue().getUUID(), URLClassLoader.newInstance(new URL[]{p.getKey().toURI().toURL()}, getClass().getClassLoader()));
            } catch (MalformedURLException ex) {
                Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    private void loadPlugin(Pair<File, PluginLoaderElement> p) {
        try {
            PluginLoaderElement pe = p.getValue();
//            Class<?> clazz = Class.forName(pe.getClass_path(), true, loader);
            Class<?> clazz = Class.forName(pe.getClass_path(), true, loaders.get(pe.getUUID()));
            Class<? extends LinkableClass> runClass = clazz.asSubclass(LinkableClass.class);
            Constructor<?> ctor = runClass.getConstructor();
            var id = pe.getUUID();
            loaded_plugins.put(id, pe);
            loadClass(ctor, id);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.lang.IncompatibleClassChangeError ce) {
            PluginFolderHandler.getInstance().removePluginData(p.getValue());
            p.getKey().deleteOnExit();
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
    public synchronized Set<UUID> getUUIDS() {
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

    private PluginLoaderElement findPlugin(UUID id) {
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
