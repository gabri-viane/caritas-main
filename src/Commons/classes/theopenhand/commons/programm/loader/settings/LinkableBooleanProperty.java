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
package theopenhand.commons.programm.loader.settings;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.CheckBox;
import theopenhand.commons.events.programm.utils.ChangeListener;

/**
 *
 * @author gabri
 */
public class LinkableBooleanProperty implements Serializable{

    /**
     *
     */
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
    private CheckBox cb = null;
    private final SimpleBooleanProperty value;
    
    /**
     *
     */
    public LinkableBooleanProperty() {
        this.listeners = new ArrayList<>();
        value = new SimpleBooleanProperty();
    }

    /**
     *
     * @param cb
     */
    public LinkableBooleanProperty(CheckBox cb) {
        this.cb = cb;
        this.cb.setAllowIndeterminate(true);
        this.listeners = new ArrayList<>();
        value = new SimpleBooleanProperty(true);
        this.cb.selectedProperty().bindBidirectional(value);
    }

    /**
     *
     * @param value
     */
    public LinkableBooleanProperty(boolean value) {
        this.value = new SimpleBooleanProperty(value);
        listeners = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public Boolean getValue() {
        return value.getValue();
    }

    /**
     *
     * @param b
     */
    public void set(Boolean b) {
        var previuos = value;
        value.setValue(b);
        throw_event(previuos.getValue());
    }

    /**
     *
     * @param b
     */
    public void setValue(boolean b) {
        set(b);
    }

    /**
     *
     * @param listener
     */
    public void addListener(ChangeListener<Boolean> listener) {
        listeners.add(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeListener(ChangeListener<Boolean> listener) {
        listeners.remove(listener);
    }

    /**
     *
     * @return
     */
    public int consult() {
        return value.getValue() != null ? (value.get() ? TRUE : FALSE) : INDETERMINATE;
    }

    private void throw_event(Boolean previous) {
        listeners.stream().forEachOrdered(cl -> {
            cl.onChange(previous, value.getValue());
        });
    }

}
