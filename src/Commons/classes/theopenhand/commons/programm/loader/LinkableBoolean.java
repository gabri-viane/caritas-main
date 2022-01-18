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
public class LinkableBoolean implements Serializable, LinkableVariableInterface<Boolean> {

    /**
     *
     */
    @Serial
    public static final long serialVersionUID = 1L;

    /**
     *
     */
    public final static int TRUE = 1;

    /**
     *
     */
    public final static int FALSE = 0;

    /**
     *
     */
    public final static int INDETERMINATE = -1;

    private final ArrayList<ChangeListener<Boolean>> listeners;
    private Boolean value;

    /**
     *
     */
    public LinkableBoolean() {
        this.listeners = new ArrayList<>();
        value = false;
    }

    /**
     *
     * @param value
     */
    public LinkableBoolean(boolean value) {
        this.value = value;
        listeners = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    @Override
    public Boolean getValue() {
        return value;
    }

    /**
     *
     * @param b
     */
    @Override
    public void setValue(Boolean b) {
        var previuos = value;
        value = b;
        throw_event(previuos);
    }

    /**
     *
     * @param b
     */
    public void set(boolean b) {
        setValue(b);
    }

    /**
     *
     * @param listener
     */
    @Override
    public void addListener(ChangeListener<Boolean> listener) {
        listeners.add(listener);
    }

    /**
     *
     * @param listener
     */
    @Override
    public void removeListener(ChangeListener<Boolean> listener) {
        listeners.remove(listener);
    }

    /**
     *
     * @return
     */
    public int consult() {
        return value != null ? (value ? TRUE : FALSE) : INDETERMINATE;
    }

    private void throw_event(Boolean previous) {
        listeners.stream().forEachOrdered(cl -> {
            cl.onChange(previous, value);
        });
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : null;
    }
}
