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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import theopenhand.commons.ReferenceQuery;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.ExchangeID;
import theopenhand.commons.interfaces.StringTransformer;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.runtime.Utils;
import theopenhand.runtime.templates.RuntimeReference;
import theopenhand.window.graphics.creators.DialogCreator;
import theopenhand.window.graphics.inner.RadioValueButton;
import theopenhand.window.graphics.inner.SelectableElement;

/**
 *
 * @author gabri
 * @param <T>
 * @param <X>
 */
public final class PickerDialogCNTRL<T extends BindableResult, X extends ResultHolder<T>> extends AnchorPane implements ExchangeID<T> {

    @FXML
    private Label titleLBL;

    @FXML
    private Hyperlink refreshLNK;

    @FXML
    private Hyperlink orderLNK;

    @FXML
    private VBox contentVB;

    @FXML
    private Button exitBTN;

    @FXML
    private Button addBTN;

    @FXML
    private Button selectBTN;

    private final ToggleGroup tg;

    /**
     * VARIABILI PER GUI*
     */
    private String title = "Seleziona valore";
    private final DialogComponent on_add;

    /**
     * VARIABILI PER QUERY*
     */
    private X res_holder;
    private final int id_query;
    private final RuntimeReference rr;
    private final Class<T> clazz;

    /**
     * VARIABILI PER ELEMENTI
     *
     */
    boolean show_id = true;
    StringTransformer<T> transformer_text = (element) -> {
        return element.toString();
    };
    StringTransformer<T> transformer_id = (element) -> {
        return "" + element.getID();
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
    public PickerDialogCNTRL(RuntimeReference rr, Class<T> cz, X result_holder, int id_query, String title, DialogComponent on_add) {
        this.rr = rr;
        this.clazz = cz;
        this.res_holder = result_holder;
        this.id_query = id_query;
        this.on_add = on_add;
        tg = new ToggleGroup();
        init();
        setTitle(title);
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/graphics/commons/PickerDialog.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(PickerDialogCNTRL.class.getName()).log(Level.SEVERE, null, ex);
        }
        orderLNK.setOnAction((t) -> {
            SharedReferenceQuery.execute(new ReferenceQuery(rr, clazz, res_holder, id_query), Utils.newInstance(clazz), SharedReferenceQuery.EXECUTION_REQUEST.CUSTOM_QUERY, (os) -> {
                res_holder = (X) ((Optional<ResultHolder>) os[0]).orElse(null);
                onRefresh(false);
                return null;
            });
            orderLNK.setVisited(false);
        });
        refreshLNK.setOnAction(a -> {
            onRefresh(true);
            refreshLNK.setVisited(false);
        });
        if (on_add != null) {
            addBTN.setOnAction(a -> {
                Stage s = DialogCreator.showDialog(on_add.getParentNode(), on_add.getDialogWidth(), on_add.getDialogWidth(), on_add.getTitle());
                ClickListener cl = () -> {
                    s.getScene().setRoot(new Region());
                    s.close();
                };
                on_add.onExitPressed(cl);
                on_add.onAcceptPressed(() -> {
                    refreshLNK.fire();
                    cl.onClick();
                });
            });
        } else {
            addBTN.setVisible(false);
        }
    }

    /**
     *
     * @param reload
     */
    @Override
    public void onRefresh(boolean reload) {
        if (reload) {
            res_holder = (X) ConnectionExecutor.getInstance().executeQuery(rr, id_query, clazz, null).orElse(null);
        }
        if (res_holder != null) {
            ObservableList<Node> els = contentVB.getChildren();
            ObservableList<Toggle> toggles = tg.getToggles();
            els.clear();
            toggles.clear();
            res_holder.getList().stream().forEachOrdered(e -> {
                SelectableElement<PickerElementCNTRL<T>> selectableElement = new SelectableElement<>(new PickerElementCNTRL(e, show_id, transformer_text, transformer_id));
                els.add(selectableElement);
                selectableElement.setToggleGroup(tg);
                if (last_selected != null && e.equals(last_selected)) {
                    selectableElement.setSelected(true);
                }
            });
        }
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

    private T last_selected;

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
                    last_selected = element;
                }
            });
        }
    }

    /**
     *
     * @return
     */
    @Override
    public long getID() {
        if (getValue() != null) {
            return getValue().getID().longValue();
        } else {
            return -1;
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
     * @param transformer_id
     */
    public void setTransformer_id(StringTransformer<T> transformer_id) {
        this.transformer_id = transformer_id;
        show_id = transformer_id != null;
    }

    /**
     *
     * @param transformer_text
     */
    public void setTransformer_text(StringTransformer<T> transformer_text) {
        this.transformer_text = transformer_text;
    }

}
