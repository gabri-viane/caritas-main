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
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import theopenhand.commons.connection.runtime.annotations.QueryCustom;
import theopenhand.commons.connection.runtime.custom.Clause;
import theopenhand.commons.connection.runtime.custom.ClauseData;
import theopenhand.commons.connection.runtime.custom.ClauseType;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.ValueHolder;

/**
 *
 * @author gabri
 */
public class OrderElement<T extends BindableResult> extends HBox implements ValueHolder<Clause> {

    @FXML
    private CheckBox fieldSelectorCB;

    @FXML
    private ChoiceBox<ClauseData> optionsCB;

    private final QueryCustom qc_assoc;
    private final Clause cl;

    private ClickListener on_req;

    public OrderElement(Clause cl, QueryCustom qc) {
        this.cl = cl;
        this.qc_assoc = qc;
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/graphics/commons/ordable/components/OrderElement.fxml");
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
        optionsCB.getItems().addAll(ClauseData.ASC_ORDER, ClauseData.DESC_ORDER);
        optionsCB.setOnShown((t) -> {
            this.fieldSelectorCB.setSelected(true);
        });
        on_req = () -> {
            cl.setClauseType(ClauseType.ORDER_BY);
            cl.setClauseData(optionsCB.getValue());
        };
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
    }

    public boolean isSelected() {
        return this.fieldSelectorCB.isSelected();
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
