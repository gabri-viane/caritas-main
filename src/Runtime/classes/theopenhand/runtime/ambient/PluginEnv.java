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

import theopenhand.runtime.ambient.xml.PluginAmbientElement;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import theopenhand.commons.Pair;
import theopenhand.installer.SetupInit;
import theopenhand.runtime.ambient.loadables.SerializedData;
import theopenhand.runtime.ambient.loadables.SettingsFieldLoader;
import theopenhand.runtime.ambient.xml.SavedDataElement;
import theopenhand.runtime.ambient.xml.SavedElement;
import theopenhand.runtime.ambient.xml.SettingField;
import theopenhand.runtime.ambient.xml.SettingsElement;
import theopenhand.runtime.annotations.SettingProperty;
import theopenhand.runtime.block.KeyUnlock;
import theopenhand.runtime.data.DataElement;
import theopenhand.runtime.loader.SettingsLoader;
import ttt.utils.xml.document.XMLDocument;
import ttt.utils.xml.engine.XMLEngine;
import ttt.utils.xml.io.XMLWriter;

/**
 * W
 *
 * @author gabri
 */
public class PluginEnv {

    private final XMLWriter writer;

    private final XMLDocument written_data;
    private PluginAmbientElement pl;
    private SavedDataElement savedData;
    private SettingsFieldLoader fields_loader;
    private final File folder;
    private final File settings_file;

    private final HashMap<String, SerializedData> els = new HashMap<>();
    private final HashMap<String, DataElement> els_link = new HashMap<>();

    private SettingsLoader loader;

    public PluginEnv(File file) throws IOException {
        if (file != null) {
            if (file.exists() && file.isDirectory()) {
                folder = file;
                settings_file = new File(file.getAbsolutePath() + File.separatorChar + "settings.xml");
                written_data = new XMLDocument(settings_file);
                writer = new XMLWriter(settings_file);
                finalizeElements();
            } else if (!file.exists()) {
                file.mkdirs();
                folder = file;
                settings_file = new File(file.getAbsolutePath() + File.separatorChar + "settings.xml");
                settings_file.createNewFile();
                written_data = new XMLDocument(settings_file);
                writer = new XMLWriter(settings_file);
                writeDefaultElements();
                //finalizeElements();
            } else {
                throw new IllegalArgumentException("Il percorso specificato per il salvataggio dati del plugin non è valido.");
            }
        } else {
            throw new IllegalArgumentException("Il percorso specificato per il salvataggio dati del plugin non è valido.");
        }
    }

    public PluginEnv(String folder) throws IOException {
        this(new File(folder));
    }

    private void writeDefaultElements() {
        pl = new PluginAmbientElement();
        savedData = new SavedDataElement();
        pl.addSubElement(new SettingsElement());
        pl.addSubElement(savedData);
        written_data.addSubElement(pl);
        writer.writeDocument(written_data, true);
    }

    private void finalizeElements() {
        if (settings_file.exists() && settings_file.isFile()) {
            try {
                XMLEngine eng = new XMLEngine(settings_file, PluginAmbientElement.class, SettingsElement.class, SavedDataElement.class, SavedElement.class, SettingField.class);
                eng.morph(written_data);
                pl = (PluginAmbientElement) written_data.getRoot();
                savedData = pl.getSavedData();
            } catch (IOException ex) {
                Logger.getLogger(PluginEnv.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Salva e serializza un nuovo DataElement: viene salvato in
     * {@link SetupInit#PLUGINS_DATA_FOLDER} nel folder specifico del plugin e
     * aggiunto nella lista {@link SavedDataElement} come {@link SavedElement}
     * nel file "settings.xml" del plugin.
     *
     * @param el
     */
    public void registerData(DataElement el) {
        SavedElement se = new SavedElement();
        se.setPath_to_file(folder.getAbsolutePath() + File.separatorChar + el.getName());
        se.setValue(el.getName());
        savedData.addSubElement(se);
        els.put(el.getName(), new SerializedData(se.getPath_to_file(), se.getValue()));
        try {
            File f = new File(se.getPath_to_file());
            if (!f.exists()) {
                f.createNewFile();
            }
            try ( ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
                oos.writeObject(el);
                oos.flush();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PluginEnv.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(PluginEnv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DataElement getData(String name) {
        return els_link.get(name);
    }

    public void removeData(String name) {
        SerializedData remove = els.remove(name);
        els_link.remove(name);
        if (remove != null) {
            File f = remove.getF();
            if (f != null && f.exists()) {
                f.delete();
                savedData.getElements().stream().filter((t) -> {
                    SavedElement se = (SavedElement) t;
                    return se.getValue().equals(name);
                });
            }
        }
    }

    /**
     * Carica tutti i DataElements (prende dal file settings.xml e carica tutti
     * i "ref"(SavedElement) e li ricarica come SerializedData a questo punto li
     * converte in DataElements (cioè legge il file serializzato e lo
     * riconverte)) e li salva nelle impostazioni (classe Settings) del plugin.
     */
    private void loadDataElements() {
        savedData.getElements().forEach(sd -> {
            SavedElement se = (SavedElement) sd;
            SerializedData sd2 = new SerializedData(se.getPath_to_file(), se.getValue());
            els.put(sd2.getName(), sd2);
            els_link.put(sd2.getName(), sd2.load());
        });
        loader.getInstance().setDataElements(KeyUnlock.KEY, els_link);
    }

    private void registerSettingProperty(Field f, SettingProperty sp) {
        SettingField sf = new SettingField();
        sf.setClazz(f.getType().getName());
        sf.setId(sp.id());
        pl.getSettings().addSubElement(sf);
        pl.getSettings().onLoad();
    }

    public ArrayList<SettingField> getSettings() {
        return pl.getSettings().getSettingFields();
    }

    public void saveEnv() {
        fields_loader.save();
        writer.writeDocument(written_data, true);
    }

    public void setSettingsLoader(SettingsLoader sl) {
        loader = sl;
        loader.init();
        fields_loader = new SettingsFieldLoader(this);
        if (loader != null) {
            ArrayList<SettingField> settingFields = pl.getSettings().getSettingFields();
            HashMap<String, Pair<Field, SettingProperty>> fields = loader.getFields();
            boolean fl = settingFields.size() != fields.size();
            if (fl) {
                pl.getSettings().getElements().clear();
                fields.forEach((t, u) -> {
                    var f = u.getKey();
                    var sf = u.getValue();
                    registerSettingProperty(f, sf);
                });
                fields_loader.init();
                fields_loader.load(true);
                fields_loader.save();
            } else {
                fields_loader.load(false);
            }
        }
        loader.createNodes();
        loadDataElements();
    }

    public SettingsLoader getSettingsLoader() {
        return loader;
    }

    public File getFolder() {
        return folder;
    }

    public PluginAmbientElement getPluginData() {
        return pl;
    }
    
}
