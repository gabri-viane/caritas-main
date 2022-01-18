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
package theopenhand.commons.events.programm.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gabri
 * @param <E> Oggetto della lista
 */
public class ListEventHandler<E> {

    private final ArrayList<ListEventListener<E>> listeners = new ArrayList<>();
    private final List<E> list;

    /**
     *
     * @param list
     */
    public ListEventHandler(List<E> list) {
        this.list = list;
    }

    /**
     *
     * @param element
     */
    public void elementAdded(E element) {
        list.add(element);
        listeners.forEach(l -> l.onElementAdded(element));
    }

    /**
     *
     * @param element
     */
    public void elementRemoved(E element) {
        list.remove(element);
        listeners.forEach(l -> l.onElementRemoved(element));
    }

    /**
     *
     * @param cl
     */
    public void addListener(ListEventListener<E> cl) {
        listeners.add(cl);
    }

    /**
     *
     * @param cl
     */
    public void removeListener(ListEventListener<E> cl) {
        listeners.remove(cl);
    }

    /**
     *
     * @return
     */
    public List<E> getList() {
        return list;
    }
}
