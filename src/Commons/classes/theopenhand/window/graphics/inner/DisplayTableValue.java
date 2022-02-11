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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import theopenhand.commons.events.programm.ValueAcceptListener;
import theopenhand.commons.interfaces.graphics.TableAssoc;

/**
 *
 * @author gabri
 * @param <T>
 */
public class DisplayTableValue<T extends TableAssoc> extends AnchorPane {

    @FXML
    private TableView<T> containerTV;

    private PopupControl row_pc = null;

    ValueAcceptListener<T> val = (T) -> {
    };

    /**
     *
     */
    public DisplayTableValue() {
        init();
    }

    public DisplayTableValue(ValueAcceptListener<T> on_double_click) {
        val = on_double_click;
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/graphics/inner/DisplayTableValue.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(DisplayTableValue.class.getName()).log(Level.SEVERE, null, ex);
        }

        containerTV.setOnKeyTyped(eh -> {
            if (eh.getCode().equals(KeyCode.ENTER)) {
                val.onAccept(containerTV.getSelectionModel().getSelectedItem());
            }
        });
        containerTV.setOnMouseClicked(eh -> {
            if (eh.getButton().equals(MouseButton.PRIMARY)) {
                if (eh.getClickCount() == 2) {
                    val.onAccept(containerTV.getSelectionModel().getSelectedItem());
                }
            } else if (row_pc != null && eh.getButton().equals(MouseButton.SECONDARY) && !containerTV.getSelectionModel().isEmpty()) {
                this.row_pc.show(this, eh.getScreenX(), eh.getScreenY());
            }
        });
    }

    public void setValueAcceptListener(ValueAcceptListener<T> val) {
        if (val != null) {
            this.val = val;
        }
    }

    public void setPopUpMenu(PopupControl pc) {
        if (pc != null) {
            row_pc = pc;
            row_pc.setAutoHide(true);
            row_pc.setAutoFix(true);
            row_pc.setHideOnEscape(true);
        }
    }

    /**
     *
     * @param column
     */
    public void addColumn(TableColumn<T, String> column) {
        containerTV.getColumns().add(column);
    }

    /**
     *
     * @param dt
     */
    public void addData(T dt) {
        containerTV.getItems().add(dt);
    }

    /**
     *
     * @param dts
     */
    public void addData(List<T> dts) {
        dts.stream().forEachOrdered(dt -> containerTV.getItems().add(dt));
    }

    /**
     *
     * @param dts
     */
    public void setData(List<T> dts) {
        containerTV.getItems().clear();
        dts.stream().forEachOrdered(dt -> containerTV.getItems().add(dt));
    }

    /**
     *
     * @return
     */
    public TableViewSelectionModel<T> getSelectionModel() {
        return containerTV.getSelectionModel();
    }

}
