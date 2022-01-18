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
package theopenhand.runtime.ambient.loadables;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import theopenhand.commons.programm.loader.LinkableBoolean;
import theopenhand.commons.programm.loader.LinkableFloat;
import theopenhand.commons.programm.loader.LinkableInteger;
import theopenhand.commons.programm.loader.LinkableString;
import theopenhand.commons.programm.loader.LinkableVariableInterface;
import theopenhand.runtime.ambient.PluginEnv;
import theopenhand.runtime.ambient.xml.SettingField;
import theopenhand.runtime.connection.runtime.utils.Utils;
import theopenhand.runtime.templates.Settings;

/**
 *
 * @author gabri
 */
public class SettingsFieldLoader {

    private final HashMap<String, SettingField> to_load;
    private final PluginEnv pe;

    public SettingsFieldLoader(PluginEnv pe) {
        this.to_load = new HashMap<>();
        this.pe = pe;
        init();
    }

    public final void init() {
        to_load.clear();
        pe.getSettings().stream().forEach(sf -> {
            to_load.put(sf.getId(), sf);
        });
    }

    public void load(boolean first_load) {
        pe.getSettingsLoader().getFields().forEach((t, u) -> {
            SettingField sf = to_load.get(t);
            Field f = u.getKey();
            if (sf == null) {
                sf = new SettingField();
                sf.setClazz(f.getType().getName());
                sf.setId(t);
                sf.setValue(null);
            }
            loadValueToField(sf, f, first_load);
        });
    }

    public void save() {
        pe.getSettingsLoader().getFields().forEach((t, u) -> {
            SettingField sf = to_load.get(t);
            Field f = u.getKey();
            if (sf == null) {
                sf = new SettingField();
                sf.setClazz(f.getType().getName());
                sf.setId(t);
                pe.getPluginData().getSettings().addSubElement(sf);
            }
            downloadValueToXML(sf, f);
        });
    }

    private void loadValueToField(SettingField sf, Field f, boolean first_load) {
        f.setAccessible(true);
        Class<?> cz = f.getType();
        boolean primary = Utils.isPrimitive(cz);
        Settings s = pe.getSettingsLoader().getInstance();
        try {
            if (primary) {
                cz = Utils.boxPrimitiveClass(f.getType());
                String val = sf.getValue();
                if (val != null) {
                    if (cz.equals(Integer.class)) {
                        f.set(s, Integer.parseInt(val));
                    } else if (cz.equals(Boolean.class)) {
                        f.set(s, val.equalsIgnoreCase("true"));
                    } else if (cz.equals(String.class)) {
                        f.set(s, val);
                    } else if (cz.equals(Float.class)) {
                        f.set(s, Float.parseFloat(val));
                    }
                }
            } else {
                String val = sf.getValue();
                LinkableVariableInterface<?> lv = null;
                Object v = f.get(s);
                if (cz.equals(LinkableBoolean.class)) {
                    lv = (val != null ? new LinkableBoolean(val.equalsIgnoreCase("true")) : new LinkableBoolean());
                } else if (cz.equals(LinkableString.class)) {
                    lv = (val != null ? new LinkableString(val) : new LinkableString());
                } else if (cz.equals(LinkableInteger.class)) {
                    lv = (val != null ? new LinkableInteger(Integer.parseInt(val)) : new LinkableInteger());
                } else if (cz.equals(LinkableFloat.class)) {
                    lv = (val != null ? new LinkableFloat(Float.parseFloat(val)) : new LinkableFloat());
                }
                if (!first_load && v != null && lv != null && v instanceof LinkableVariableInterface lvi) {
                    lvi.setValue(lv.getValue());
                } else if (v == null) {
                    f.set(s, lv);
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(SettingsFieldLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void downloadValueToXML(SettingField sf, Field f) {
        try {
            f.setAccessible(true);
            Settings s = pe.getSettingsLoader().getInstance();
            Object get = f.get(s);
            sf.setValue(get != null ? get.toString() : null);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(SettingsFieldLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
