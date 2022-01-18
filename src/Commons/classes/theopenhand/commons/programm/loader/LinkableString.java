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
package theopenhand.commons.programm.loader;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import theopenhand.commons.events.programm.utils.ChangeListener;

/**
 *
 * @author gabri
 */
public class LinkableString implements Serializable, LinkableVariableInterface<String> {

    /**
     *
     */
    @Serial
    public static final long serialVersionUID = 1L;

    private final ArrayList<ChangeListener<String>> listeners;
    private String value;

    /**
     *
     */
    public LinkableString() {
        this.listeners = new ArrayList<>();
        value = null;
    }

    /**
     *
     * @param value
     */
    public LinkableString(String value) {
        this.value = value;
        listeners = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     *
     * @param b
     */
    @Override
    public void setValue(String b) {
        var previuos = value;
        value = b;
        throw_event(previuos);
    }

    /**
     *
     * @param b
     */
    public void set(String b) {
        setValue(b);
    }

    /**
     *
     * @param listener
     */
    @Override
    public void addListener(ChangeListener<String> listener) {
        listeners.add(listener);
    }

    /**
     *
     * @param listener
     */
    @Override
    public void removeListener(ChangeListener<String> listener) {
        listeners.remove(listener);
    }

    /**
     *
     * @return
     */
    public String consult() {
        return value;
    }

    private void throw_event(String previous) {
        listeners.stream().forEachOrdered(cl -> {
            cl.onChange(previous, value);
        });
    }

    @Override
    public String toString() {
        return value;
    }

}
