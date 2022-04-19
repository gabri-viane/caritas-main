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
package theopenhand.runtime.data.components;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author gabri
 */
public class DataElement implements IDataElement {

    /**
     *
     */
    public static final long serialVersionUID = 2L;

    protected String name;
    protected Serializable data;
    private long uid;

    /**
     *
     */
    public DataElement() {

    }

    /**
     *
     * @param name
     */
    public DataElement(String name) {
        this.name = name;
    }

    /**
     *
     * @param name
     * @param data
     */
    public DataElement(String name, Serializable data) {
        this.name = name;
        this.data = data;
    }

    /**
     *
     * @param data
     */
    public void setData(Serializable data) {
        this.data = data;
    }

    /**
     *
     * @return
     */
    public Serializable getData() {
        return data;
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
        if (obj instanceof DataElement de) {
            return de.name.equals(name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.name);
        hash = 17 * hash + Objects.hashCode(this.data);
        return hash;
    }

    @Override
    public String toString() {
        return name;
    }

}
