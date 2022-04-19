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
import java.util.Objects;

/**
 *
 * @author gabri
 */
public class PairDataElement<T extends Serializable, V extends Serializable> implements IDataElement {

    private T sx;
    private V dx;
    protected String name;
    private long uid;

    public PairDataElement(String name) {
        this.name = name;
    }

    public PairDataElement(String name, T sx, V dx) {
        this.name = name;
        this.sx = sx;
        this.dx = dx;
    }

    public PairDataElement(T sx, V dx) {
        this.sx = sx;
        this.dx = dx;
    }

    public T getSxValue() {
        return sx;
    }

    public V getDxValue() {
        return dx;
    }

    public void setDx(V dx) {
        this.dx = dx;
    }

    public void setSx(T sx) {
        this.sx = sx;
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
        if (obj instanceof PairDataElement pde) {
            return pde.getSxValue().equals(sx);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.sx);
        return hash;
    }

    @Override
    public String toString() {
        return name;
    }

}
