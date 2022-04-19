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
package theopenhand.runtime.data.components;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import theopenhand.commons.events.graphics.ListableContainer;

/**
 *
 * @author gabri
 */
public class MapDataElement<K extends Serializable, V extends Serializable> implements IDataElement, ListableContainer<K, V> {

    private HashMap<K, V> map;
    protected String name;
    private long uid;

    public MapDataElement() {
        map = new HashMap<>();
    }

    public MapDataElement(String name) {
        this.map = new HashMap<>();
        this.name = name;
    }

    public void addElement(K key, V value) {
        map.put(key, value);
    }

    public void removeElement(K key) {
        map.remove(key);
    }

    public void setMap(HashMap<K, V> map) {
        this.map = map;
    }

    protected HashMap<K, V> getMap() {
        return map;
    }

    @Override
    public Map<K, V> getElements() {
        return Collections.unmodifiableMap(map);
    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public long getUid() {
        return uid;
    }

    @Override
    public void setUid(long uid) {
        this.uid = uid;
    }

    @Override
    public long getID() {
        return uid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MapDataElement mde) {
            return mde.map.equals(map);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.map);
        return hash;
    }

    @Override
    public String toString() {
        return name;
    }

}
