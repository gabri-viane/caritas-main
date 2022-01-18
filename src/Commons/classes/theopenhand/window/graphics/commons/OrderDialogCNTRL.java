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
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.runtime.templates.RuntimeReference;

/**
 *
 * @author gabri
 * @param <T>
 * @param <X>
 */
public final class OrderDialogCNTRL<T extends BindableResult, X extends ResultHolder<T>> extends AnchorPane implements DialogComponent {

    @FXML
    private Button exitBTN;

    @FXML
    private Button loadFilterBTN;

    @FXML
    private VBox orderContentVB;

    @FXML
    private Button resetBTN;

    @FXML
    private Button saveFilterBTN;

    @FXML
    private VBox searchContentVB;

    @FXML
    private Button selectBTN;

    @FXML
    private Label titleLBL;

    /**
     * VARIABILI PER GUI*
     */
    private String title = "Seleziona valore";

    /**
     * VARIABILI PER QUERY*
     */
    private X res_holder;
    private final int id_query;
    private final RuntimeReference rr;
    private final Class<T> clazz;

    /**
     *
     * @param rr
     * @param cz
     * @param result_holder
     * @param id_query
     * @param title
     * @param on_add
     * @param on_order
     */
    public OrderDialogCNTRL(RuntimeReference rr, Class<T> cz, X result_holder, int id_query, String title, DialogComponent on_add, DialogComponent on_order) {
        this.rr = rr;
        this.clazz = cz;
        this.res_holder = result_holder;
        this.id_query = id_query;
        init();
        setTitle(title);
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/graphics/commons/OrderDialog.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(PickerDialogCNTRL.class.getName()).log(Level.SEVERE, null, ex);
        }
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

}
