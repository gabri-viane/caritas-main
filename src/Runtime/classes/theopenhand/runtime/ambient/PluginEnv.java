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
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import theopenhand.commons.Pair;
import theopenhand.runtime.ambient.loadables.SettingsFieldLoader;
import theopenhand.runtime.ambient.xml.SavedDataElement;
import theopenhand.runtime.ambient.xml.SavedElement;
import theopenhand.runtime.ambient.xml.SettingField;
import theopenhand.runtime.ambient.xml.SettingsElement;
import theopenhand.runtime.annotations.SettingProperty;
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
    private SettingsFieldLoader fields_loader;
    private final File folder;
    private final File settings_file;

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
        pl.addSubElement(new SettingsElement());
        pl.addSubElement(new SavedDataElement());
        written_data.addSubElement(pl);
        writer.writeDocument(written_data, true);
    }

    private void finalizeElements() {
        if (settings_file.exists() && settings_file.isFile()) {
            try {
                XMLEngine eng = new XMLEngine(settings_file, PluginAmbientElement.class, SettingsElement.class, SavedDataElement.class, SavedElement.class, SettingField.class);
                eng.morph(written_data);
                pl = (PluginAmbientElement) written_data.getRoot();
            } catch (IOException ex) {
                Logger.getLogger(PluginEnv.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void registerData(DataElement el) {
        SavedDataElement savedData = ((PluginAmbientElement) written_data.getRoot()).getSavedData();
        SavedElement se = new SavedElement();
        se.setPath_to_file(folder.getAbsolutePath() + File.separatorChar + el.getName());
        savedData.addSubElement(se);
    }

    public void registerSettingProperty(Field f, SettingProperty sp) {
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

    public void setSetingsLoader(SettingsLoader sl) {
        loader = sl;
        loader.init();
        fields_loader = new SettingsFieldLoader(this);
        if (loader != null) {
            ArrayList<SettingField> settingFields = pl.getSettings().getSettingFields();
            HashMap<String, Pair<Field, SettingProperty>> fields = loader.getFields();
            boolean fl = settingFields.size() != fields.size();
            if (fl) {
                pl.getSettings().getElements().forEach(el -> {
                    pl.getSettings().removeSubElement(el);
                });
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
