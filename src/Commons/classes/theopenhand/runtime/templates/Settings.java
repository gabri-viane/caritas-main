/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package theopenhand.runtime.templates;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import theopenhand.runtime.block.KeyUnlock;
import theopenhand.runtime.data.DataElement;

/**
 *
 * @author gabri
 */
public abstract class Settings {

    private UUID uuid;
    private HashMap<String, DataElement> df4_elements = new HashMap<>();

    /**
     *
     * @param key
     * @param des
     */
    public final void setDataElements(KeyUnlock key, HashMap<String, DataElement> des) {
        if (key != null) {
            df4_elements = des;
        }
    }

    /**
     *
     * @param de
     */
    public final void addDataElement(DataElement de) {
        df4_elements.put(de.getName(), de);
    }

    /**
     *
     * @param de
     */
    public final void removeDataElement(DataElement de) {
        df4_elements.remove(de.getName());
    }

    /**
     *
     * @param name
     */
    public final void removeDataElement(String name) {
        df4_elements.remove(name);
    }

    /**
     *
     * @return
     */
    public final Map<String, DataElement> getDataElements() {
        return Collections.unmodifiableMap(df4_elements);
    }

    /**
     *
     * @return
     */
    public final UUID getUUID() {
        return this.uuid;
    }

    /**
     *
     * @param key
     * @param uuid
     */
    public final void setUUID(KeyUnlock key, UUID uuid) {
        if (key != null) {
            this.uuid = uuid;
        }
    }
}
