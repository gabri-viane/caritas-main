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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author gabri
 */
public class ListDataElement<T extends Serializable> implements IDataElement {

    public static final long serialVersionUID = 2L;

    private ArrayList<T> list;
    protected String name;
    private long uid;

    public ListDataElement(String name) {
        list = new ArrayList<>();
        this.name = name;
    }

    public ListDataElement(ArrayList<T> list) {
        this.list = list;
    }

    public ListDataElement(ArrayList<T> list, String name) {
        this.list = list;
        this.name = name;
    }

    public List<T> getList() {
        return Collections.unmodifiableList(list);
    }

    public void setList(ArrayList<T> list) {
        this.list = list;
    }

    public void addElement(T element) {
        list.add(element);
    }

    public void removeElement(T element) {
        list.remove(element);
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
        if (obj instanceof ListDataElement lde) {
            return lde.list.equals(list);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.list);
        return hash;
    }

    @Override
    public String toString() {
        return name;
    }

}
