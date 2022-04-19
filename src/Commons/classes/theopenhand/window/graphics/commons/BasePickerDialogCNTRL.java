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
package theopenhand.window.graphics.commons;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.events.graphics.ListableContainer;
import theopenhand.commons.interfaces.GenericPicker;
import theopenhand.commons.interfaces.StringTransformer;
import theopenhand.window.graphics.inner.RadioValueButton;
import theopenhand.window.graphics.inner.SelectableElement;

/**
 *
 * @author gabri
 */
public final class BasePickerDialogCNTRL<T extends Serializable, R extends ListableContainer<String, T>> extends AnchorPane implements GenericPicker<T> {

    @FXML
    private VBox contentVB;

    @FXML
    private Button exitBTN;

    @FXML
    private Button selectBTN;

    @FXML
    private Label titleLBL;

    private final ToggleGroup tg;

    /**
     * VARIABILI PER GUI*
     */
    private String title = "Seleziona elemento";

    private final R container;

    StringTransformer<T> transformer_text = (element) -> {
        return element.toString();
    };

    /**
     *
     * @param rr
     * @param cz
     * @param result_holder
     * @param id_query
     * @param title
     * @param on_add
     */
    public BasePickerDialogCNTRL(String title, R container) {
        tg = new ToggleGroup();
        this.container = container;
        init();
        setTitle(title);
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/graphics/commons/BasePickerDialog.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(PickerDialogCNTRL.class.getName()).log(Level.SEVERE, null, ex);
        }
        onRefresh(true);
    }

    /**
     *
     * @param reload
     */
    @Override
    public void onRefresh(boolean reload) {
        ObservableList<Node> children = contentVB.getChildren();
        children.clear();
        Map<String, T> elements = container.getElements();
        elements.forEach((s, t) -> {
            SelectableElement<PickerElementCNTRL<T>> elem = new SelectableElement<>(new PickerElementCNTRL<>(t, false, transformer_text, null));
            children.add(elem);
            elem.setToggleGroup(tg);
        });
    }

    /**
     *
     * @return
     */
    @Override
    public T getValue() {
        RadioValueButton<PickerElementCNTRL<T>> selectedToggle = (RadioValueButton<PickerElementCNTRL<T>>) tg.getSelectedToggle();
        if (selectedToggle == null) {
            return null;
        }
        return selectedToggle.getValue().getValue();
    }

    /**
     *
     * @return
     */
    @Override
    public Parent getParentNode() {
        return this;
    }

    /**
     *
     * @param cl
     */
    @Override
    public void onExitPressed(ClickListener cl) {
        exitBTN.setOnAction(a -> cl.onClick());
    }

    /**
     *
     * @param cl
     */
    @Override
    public void onAcceptPressed(ClickListener cl) {
        selectBTN.setOnAction(a -> cl.onClick());
    }

    /**
     *
     * @param element
     */
    @Override
    public void select(T element) {
        if (element != null) {
            contentVB.getChildren().stream().forEach(e -> {
                SelectableElement<PickerElementCNTRL<T>> se = (SelectableElement<PickerElementCNTRL<T>>) e;
                if (se.getValue().getValue().equals(element)) {
                    se.setSelected(true);
                }
            });
        }
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        if (title != null) {
            this.title = title;
            titleLBL.setText(title);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return
     */
    @Override
    public double getDialogWidth() {
        return getPrefWidth();
    }

    /**
     *
     * @return
     */
    @Override
    public double getDialogHeight() {
        return getPrefHeight();
    }

    /**
     *
     * @param transformer_text
     */
    public void setTransformer_text(StringTransformer<T> transformer_text) {
        this.transformer_text = transformer_text;
    }

}
