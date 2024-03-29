/**
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package theopenhand.runtime.loader;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import theopenhand.commons.Pair;
import theopenhand.commons.programm.loader.LinkableBoolean;
import theopenhand.commons.programm.loader.LinkableInteger;
import theopenhand.runtime.annotations.SettingProperty;
import theopenhand.runtime.Utils;
import theopenhand.runtime.block.KeyUnlock;
import theopenhand.runtime.templates.LinkableClass;
import theopenhand.runtime.templates.Settings;
import theopenhand.window.graphics.creators.DialogCreator;
import theopenhand.window.graphics.creators.ElementCreator;
import theopenhand.window.resources.ui.settings.plugins.PluginSettingField;

/**
 * Carica le impostazioni di un plugin.
 * </p>
 * Data la {@link LinkableClass} viene chiamato il metodo {@link LinkableClass#getSettings()
 * } e vengono cercati tutti i campi con l'annotazione {@link SettingProperty}.
 * </br>
 * Questa classe si occupa di chiamare il metodo {@link Settings#setUUID(theopenhand.runtime.block.KeyUnlock, java.util.UUID)
 * } che assegna l'UUID al plugin.
 *
 * @author gabri
 */
public class SettingsLoader {

    private final ArrayList<PluginSettingField> controls;
    private final HashMap<String, Pair<Field, SettingProperty>> flds;
    private Class<?> settins_class;
    private final Settings instance;
    private final LinkableClass lc;
    private final UUID uuid;

    /**
     *
     * @param lc
     * @param uuid
     */
    public SettingsLoader(LinkableClass lc, UUID uuid) {
        flds = new HashMap<>();
        controls = new ArrayList<>();
        this.lc = lc;
        this.uuid = uuid;
        instance = lc.getSettings();
    }

    /**
     *
     */
    public void init() {
        if (instance != null) {
            instance.setUUID(KeyUnlock.KEY, uuid);
            findFields();
        }
    }

    /**
     *
     * @return
     */
    public Settings getInstance() {
        return instance;
    }

    private void findFields() {
        flds.clear();
        settins_class = instance.getClass();
        Field[] declaredFields = settins_class.getDeclaredFields();
        for (Field f : declaredFields) {
            f.setAccessible(true);
            SettingProperty ann = f.getAnnotation(SettingProperty.class);
            if (ann != null) {
                flds.put(ann.id(), new Pair(f, ann));
            }
        }
    }

    /**
     *
     * @return
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     *
     * @return
     */
    public HashMap<String, Pair<Field, SettingProperty>> getFields() {
        return flds;
    }

    /**
     * Rigenera gli elementi grafici associati al campo della proprietà della
     * classe {@link Settings} del plugin.
     */
    public void createNodes() {
        controls.clear();
        flds.forEach((t, u) -> {
            Field f = u.getKey();
            Class<?> c = f.getType();
            c = Utils.isPrimitive(c) ? Utils.boxPrimitiveClass(f.getType()) : c;
            if (c.equals(String.class) || c.equals(Integer.class) || c.equals(Boolean.class) || c.equals(Float.class)) {
                controls.add(generateLabel(f, u.getValue()));
            } else {
                if (c.equals(LinkableBoolean.class)) {
                    controls.add(generateCheckBox(f, u.getValue()));
                } else if (c.equals(LinkableInteger.class)) {
                    controls.add(generateNumberField(f, u.getValue()));
                }
            }
        });
    }

    private PluginSettingField generateLabel(Field f, SettingProperty ann) {
        try {
            Object val = f.get(instance);
            Label n = new Label(val != null ? val.toString() : "N/A");
            n.setMaxWidth(Double.MAX_VALUE);
            n.setWrapText(true);
            HBox.setHgrow(n, Priority.ALWAYS);
            PluginSettingField pl = new PluginSettingField(() -> {
            }, n, ann);
            pl.setButtonVisible(false);
            return pl;
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(SettingsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private PluginSettingField generateCheckBox(Field f, SettingProperty ann) {
        try {
            LinkableBoolean lb = (LinkableBoolean) f.get(instance);
            CheckBox cb = new CheckBox(ann.description());
            if (lb.getValue() == null) {
                cb.setIndeterminate(true);
            } else {
                cb.setAllowIndeterminate(false);
                cb.setSelected(lb.getValue());
            }
            cb.selectedProperty().addListener((ov, t, t1) -> {
                lb.setValue(t1);
            });
            PluginSettingField pl = new PluginSettingField(() -> {
            }, cb, ann);
            pl.setButtonVisible(false);
            return pl;
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(SettingsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private PluginSettingField generateNumberField(Field f, SettingProperty ann) {
        try {
            LinkableInteger li = (LinkableInteger) f.get(instance);
            TextField tf = new TextField();
            ElementCreator.transformNumericField(tf);
            tf.setPromptText(ann.description());
            tf.textProperty().addListener((ov, t, t1) -> {
                try {
                    Number parse = NumberFormat.getIntegerInstance().parse(t1);
                    li.setValue(parse.intValue());
                } catch (ParseException ex) {
                    DialogCreator.showAlert(Alert.AlertType.ERROR, "Valore invalido", "Il campo corrente ammette solo valori numerici interi.", null);
                }
            });
            if (li.getValue() == null) {
                tf.setText("-1");
            } else {
                tf.setText(li.getValue().toString());
            }
            PluginSettingField pl = new PluginSettingField(() -> {
            }, tf, ann);
            pl.setButtonVisible(false);
            return pl;
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(SettingsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Ritorna la lista di elementi grafici generati ed associati ai fields
     * delle proprietà.
     *
     * @return La lista di impostazioni grafiche.
     */
    public ArrayList<PluginSettingField> getNodes() {
        return controls;
    }

    /**
     * Ritorna la classe di caricamento principale del plugin
     * {@link LinkableClass}
     *
     * @return La classe entry-point del plugin
     */
    public LinkableClass getLinkableClass() {
        return lc;
    }

}
