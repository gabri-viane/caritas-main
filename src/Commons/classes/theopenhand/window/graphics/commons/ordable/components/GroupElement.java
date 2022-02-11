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
import javafx.scene.layout.HBox;
import theopenhand.commons.connection.runtime.annotations.QueryCustom;
import theopenhand.commons.connection.runtime.custom.Clause;
import theopenhand.commons.connection.runtime.custom.ClauseType;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.ValueHolder;

/**
 *
 * @author gabri
 */
public class GroupElement<T extends BindableResult> extends HBox implements ValueHolder<Clause> {

    @FXML
    private CheckBox fieldSelectorCB;

    private final QueryCustom qc_assoc;
    private final Clause cl;

    private ClickListener on_req;

    public GroupElement(Clause cl, QueryCustom qc) {
        this.cl = cl;
        this.qc_assoc = qc;
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/graphics/commons/ordable/components/GroupElement.fxml");
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
        on_req = () -> {
            cl.setClauseType(ClauseType.GROUP_BY);
            cl.setClauseData(null);
        };
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
