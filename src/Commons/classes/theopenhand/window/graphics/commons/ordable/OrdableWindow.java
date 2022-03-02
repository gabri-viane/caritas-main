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
package theopenhand.window.graphics.commons.ordable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import theopenhand.commons.connection.runtime.custom.Clause;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.window.graphics.commons.ordable.components.GroupElement;
import theopenhand.window.graphics.commons.ordable.components.OrderElement;
import theopenhand.window.graphics.commons.ordable.components.SearchElement;

/**
 *
 * @author gabri
 * @param <T>
 */
public class OrdableWindow<T extends BindableResult> extends AnchorPane implements DialogComponent {

    @FXML
    private Button applyBTN;

    @FXML
    private Accordion containerAC;

    @FXML
    private Button exitBTN;

    @FXML
    private TitledPane groupTP;

    @FXML
    private VBox groupVB;

    @FXML
    private VBox orderVB;

    @FXML
    private TitledPane orderTP;

    @FXML
    private TitledPane searchTP;

    @FXML
    private VBox searchVB;

    ArrayList<SearchElement<T>> ses = new ArrayList<>();
    ArrayList<OrderElement<T>> oes = new ArrayList<>();
    ArrayList<GroupElement<T>> ges = new ArrayList<>();

    ClickListener on_apply = () -> {
        
    };

    protected OrdableWindow() {
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/graphics/commons/ordable/OrdableWindow.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(OrdableWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addElement(SearchElement<T> se) {
        ses.add(se);
        searchVB.getChildren().add(se);
    }

    public void addElement(OrderElement<T> oe) {
        oes.add(oe);
        orderVB.getChildren().add(oe);
    }

    public void addElement(GroupElement ge) {
        ges.add(ge);
        groupVB.getChildren().add(ge);
    }

    public void trim() {
        if (searchVB.getChildren().isEmpty()) {
            containerAC.getPanes().remove(searchTP);
        }
        if (orderVB.getChildren().isEmpty()) {
            containerAC.getPanes().remove(orderTP);
        }
        if (groupVB.getChildren().isEmpty()) {
            containerAC.getPanes().remove(groupTP);
        }
    }

    public ArrayList<Clause> generateClauses() {
        ArrayList<Clause> cls = new ArrayList<>();
        ses.stream().filter((t) -> {
            return t.isSelected();
        }).forEachOrdered((t) -> {
            cls.add(t.getValue());
        });
        oes.stream().filter((t) -> {
            return t.isSelected();
        }).forEachOrdered((t) -> {
            cls.add(t.getValue());
        });
        ges.stream().filter((t) -> {
            return t.isSelected();
        }).forEachOrdered((t) -> {
            cls.add(t.getValue());
        });
        return cls;
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
        return "Filtra risultati";
    }

    @Override
    public Parent getParentNode() {
        return this;
    }

    @Override
    public void onExitPressed(ClickListener cl) {
        if (cl != null) {
            exitBTN.setOnAction((t) -> {
                cl.onClick();
            });
        }
    }

    @Override
    public void onAcceptPressed(ClickListener cl) {
        if (cl != null) {
            applyBTN.setOnAction(a -> {
                on_apply.onClick();
                cl.onClick();
            });
        } else {
            applyBTN.setOnAction(a -> {
                on_apply.onClick();
            });
        }
    }

    protected void setInstance(T in) {
        ses.forEach(se->{
            se.setInstance(in);
        });
    }


}
