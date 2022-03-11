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
package theopenhand.window.graphics.commons.ordable.components;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import theopenhand.commons.DataUtils;
import theopenhand.commons.connection.runtime.annotations.QueryCustom;
import theopenhand.commons.connection.runtime.custom.Clause;
import theopenhand.commons.connection.runtime.custom.ClauseData;
import theopenhand.commons.connection.runtime.custom.ClauseType;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.ValueHolder;
import theopenhand.runtime.Utils;
import theopenhand.window.graphics.creators.ElementCreator;

/**
 *
 * @author gabri
 */
public class SearchElement<T extends BindableResult> extends HBox implements ValueHolder<Clause> {

    @FXML
    private CheckBox fieldSelectorCB;

    @FXML
    private ChoiceBox<ClauseData> optionsCB;

    @FXML
    private HBox subContainerHB;

    private final Field assoc;
    private final QueryCustom qc_assoc;
    private final Clause cl;
    private T instance;

    private ClickListener on_req = () -> {
    };

    public SearchElement(Clause cl, QueryCustom qc, T instance) {
        this.cl = cl;
        this.assoc = cl.getField();
        this.qc_assoc = qc;
        this.instance = instance;
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/graphics/commons/ordable/components/SearchElement.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(SearchElement.class.getName()).log(Level.SEVERE, null, ex);
        }
        fieldSelectorCB.setText(qc_assoc.displayName());
        initSetup();
    }

    private void initSetup() {
        Class<?> type = assoc.getType();
        Node n = null;

        if (type.equals(int.class) || type.equals(Integer.class) || type.equals(BigInteger.class)
                || type.equals(long.class) || type.equals(Long.class)) {
            optionsCB.getItems().addAll(ClauseData.EQUALS, ClauseData.LEQUALS, ClauseData.MEQUALS, ClauseData.LESS_THAN, ClauseData.MORE_THAN);
            TextField tf = ElementCreator.buildNumericField();
            on_req = () -> {
                cl.setClauseType(ClauseType.WHERE);
                cl.setClauseData(optionsCB.getValue());
                storeValue(Utils.castTo(tf.getText(), type));
            };
            n = tf;
            tf.textProperty().addListener((ov, t, t1) -> {
                this.fieldSelectorCB.setSelected((t1 != null && !t1.isEmpty()));
            });
        } else if (type.equals(String.class)) {
            optionsCB.getItems().addAll(ClauseData.LIKE, ClauseData.EQUALS);
            TextField tf = new TextField();
            on_req = () -> {
                cl.setClauseType(ClauseType.WHERE);
                cl.setClauseData(optionsCB.getValue());
                storeValue(tf.getText());
            };
            n = tf;
            tf.textProperty().addListener((ov, t, t1) -> {
                this.fieldSelectorCB.setSelected((t1 != null && !t1.isEmpty()));
            });
        } else if (type.equals(Date.class)) {
            optionsCB.getItems().addAll(ClauseData.EQUALS, ClauseData.LEQUALS, ClauseData.MEQUALS, ClauseData.LESS_THAN, ClauseData.MORE_THAN);
            DatePicker dp = new DatePicker(LocalDate.now());
            on_req = () -> {
                cl.setClauseType(ClauseType.WHERE);
                cl.setClauseData(optionsCB.getValue());
                storeValue(DataUtils.toDate(dp.getValue()));
            };
            n = dp;
            dp.valueProperty().addListener((ov, t, t1) -> {
                this.fieldSelectorCB.setSelected(true);
            });
        } else if (type.equals(Boolean.class) | type.equals(boolean.class)) {
            optionsCB.getItems().add(ClauseData.EQUALS);
            CheckBox cb = new CheckBox("(Seleziona per sì/vero)");
            on_req = () -> {
                cl.setClauseType(ClauseType.WHERE);
                cl.setClauseData(optionsCB.getValue());
                storeValue(cb.isSelected());
            };
            n = cb;
            cb.setOnAction(a -> {
                this.fieldSelectorCB.setSelected(true);
            });
        }
        optionsCB.getSelectionModel().selectFirst();
        optionsCB.setConverter(new StringConverter<ClauseData>() {
            @Override
            public String toString(ClauseData t) {
                if (t != null) {
                    return t.getStringify();
                }
                return "Nessuno";
            }

            @Override
            public ClauseData fromString(String string) {
                return ClauseData.fromString(string);
            }
        });
        optionsCB.setOnShown((t) -> {
            this.fieldSelectorCB.setSelected(true);
        });
        subContainerHB.getChildren().add(n);
    }

    public void storeValue(Object value) {
        try {
            Field field = cl.getField();
            field.setAccessible(true);
            field.set(instance, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(SearchElement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isSelected() {
        return this.fieldSelectorCB.isSelected();
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }

    @Override
    public Clause getValue() {
        on_req.onClick();
        return cl;
    }

    @Override
    public Parent getParentNode() {
        return this.getParent();
    }

}
