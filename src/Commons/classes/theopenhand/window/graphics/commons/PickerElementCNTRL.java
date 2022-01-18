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
package theopenhand.window.graphics.commons;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import theopenhand.commons.interfaces.StringTransformer;
import theopenhand.commons.interfaces.graphics.ValueHolder;

/**
 *
 * @author gabri
 * @param <T>
 */
public class PickerElementCNTRL<T> extends HBox implements ValueHolder<T> {

    @FXML
    private Label idLBL;

    @FXML
    private Label elemIdLBL;

    @FXML
    private Label sepLBL;

    @FXML
    private Label elemTextLBL;

    private final T elem;

    private final boolean id_enabled;
    private final StringTransformer<T> id;
    private final StringTransformer<T> text;

    /**
     *
     * @param t
     * @param bln
     * @param st
     * @param st1
     */
    public PickerElementCNTRL(T f, boolean id_enabled, StringTransformer<T> text, StringTransformer<T> id) {
        this.id_enabled = id_enabled;
        this.id = id;
        this.text = text;
        elem = f;
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/graphics/commons/PickerElement.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(PickerElementCNTRL.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (id_enabled) {
            elemIdLBL.setText(id.transform(elem));
        } else {
            //Rimuovi id;
            this.getChildren().remove(elemIdLBL);
            this.getChildren().remove(idLBL);
            this.getChildren().remove(sepLBL);
        }
        elemTextLBL.setText(text.transform(elem));
    }

    /**
     *
     * @return
     */
    @Override
    public T getValue() {
        return elem;
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
