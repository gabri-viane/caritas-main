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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.window.graphics.inner.SelectableElement;

/**
 *
 * @author gabri
 */
public final class ValueDialog extends AnchorPane implements DialogComponent {

    @FXML
    private Button acceptBTN;

    @FXML
    private HBox containerHB;

    @FXML
    private Label descriptionLB;

    @FXML
    private Button exitBTN;

    @FXML
    private Label titleLB;

    public ValueDialog() {
        init();
    }

    public ValueDialog(String title) {
        this();
        setTitle(title);
    }

    public ValueDialog(String title, String description) {
        this(title);
        setDescription(description);
    }

    public ValueDialog(String title, String description, Node n) {
        this(title, description);
        addControl(n);
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/graphics/commons/ValueDialog.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(SelectableElement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setTitle(String title) {
        titleLB.setText(title);
    }

    public void setDescription(String description) {
        descriptionLB.setText(description);
    }

    public void addControl(Node n) {
        containerHB.getChildren().add(n);
    }

    public void removeControl(Node n) {
        containerHB.getChildren().remove(n);
    }

    @Override
    public double getDialogWidth() {
        return getPrefWidth();
    }

    @Override
    public double getDialogHeight() {
        return getPrefHeight();
    }

    @Override
    public String getTitle() {
        return this.titleLB.getText();
    }

    @Override
    public Parent getParentNode() {
        return this;
    }

    @Override
    public void onExitPressed(ClickListener cl) {
        exitBTN.setOnAction((a) -> {
            if (cl != null) {
                cl.onClick();
            }
        });
    }

    @Override
    public void onAcceptPressed(ClickListener cl) {
        acceptBTN.setOnAction((a) -> {
            if (cl != null) {
                cl.onClick();
            }
        });
    }

}
