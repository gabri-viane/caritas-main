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
package theopenhand.window.graphics.inner;

import javafx.scene.Parent;
import javafx.scene.control.RadioButton;
import theopenhand.commons.interfaces.graphics.ValueHolder;

/**
 *
 * @author gabri
 * @param <T>
 */
public class RadioValueButton<T> extends RadioButton implements ValueHolder<T> {

    private final T val;

    /**
     *
     * @param t
     * @param rb
     */
    public RadioValueButton(T value, RadioButton rb) {
        val = value;
        init(rb);
    }

    private void init(RadioButton rb) {
        this.selectedProperty().bindBidirectional(rb.selectedProperty());
    }

    /**
     *
     * @return
     */
    @Override
    public T getValue() {
        return val;
    }

    /**
     *
     * @return
     */
    @Override
    public Parent getParentNode() {
        return this;
    }

}
