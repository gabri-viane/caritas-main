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

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import theopenhand.commons.interfaces.graphics.ValueHolder;

/**
 *
 * @author gabri
 * @param <T>
 */
public class SelectableElement<T extends ValueHolder> extends HBox implements Toggle{

    @FXML
    private RadioButton toggleBTN;

    @FXML
    private HBox containerHB;

    private final T el;
    private RadioValueButton<T> val;

    /**
     *
     * @param element
     */
    public SelectableElement(T element) {
        el = element;
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/graphics/inner/SelectableElement.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(SelectableElement.class.getName()).log(Level.SEVERE, null, ex);
        }
        containerHB.getChildren().add(el.getParentNode());
        val = new RadioValueButton<>(el,toggleBTN);
    }

    /**
     *
     * @return
     */
    public T getValue() {
        return el;
    }

    /**
     *
     * @return
     */
    public RadioButton getRadioButton() {
        return val;
    }

    /**
     *
     * @return
     */
    @Override
    public ToggleGroup getToggleGroup() {
        return val.getToggleGroup();
    }

    /**
     *
     * @param tg
     */
    @Override
    public void setToggleGroup(ToggleGroup tg) {
        val.setToggleGroup(tg);
    }

    /**
     *
     * @return
     */
    @Override
    public ObjectProperty<ToggleGroup> toggleGroupProperty() {
        return val.toggleGroupProperty();
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isSelected() {
        return val.isSelected();
    }

    /**
     *
     * @param bln
     */
    @Override
    public void setSelected(boolean bln) {
        val.setSelected(bln);
    }

    /**
     *
     * @return
     */
    @Override
    public BooleanProperty selectedProperty() {
        return val.selectedProperty();
    }

}
