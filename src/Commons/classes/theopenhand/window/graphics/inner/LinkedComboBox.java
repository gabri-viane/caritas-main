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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import theopenhand.commons.connection.runtime.interfaces.GraphicBindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;

/**
 *
 * @author gabri
 * @param <T>
 */
public class LinkedComboBox<T extends GraphicBindableResult> extends HBox {
//ComboBoxValueHolder

    @FXML
    private ComboBox<T> cb;

    private final ResultHolder<T> res_h;
    private T selected;
    private ListView<T> lv;

    Callback<ListView<T>, ListCell<T>> cellFactory = (ListView<T> l) -> new ListCell<T>() {
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setGraphic(null);
            } else {
                setText(item.displayable());
            }
        }
    };

    /**
     *
     * @param res
     */
    public LinkedComboBox(ResultHolder<T> res) {
        res_h = res;
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/graphics/inner/LinkedComboBox.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(LinkedComboBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        cb.setCellFactory(cellFactory);
        cb.getEditor().textProperty().addListener((ObservableValue<? extends String> ov, String t, String t1) -> {
            filterList(t1);
        });
        cb.selectionModelProperty().addListener((ov, t, t1) -> {
            if (!t1.isEmpty()) {
                selected = t1.getSelectedItem();
            } else {
                t1.selectFirst();
                selected = t1.getSelectedItem();
            }
        });

        refresh();
    }

    public void refresh() {
        cb.getEditor().clear();
        ObservableList<T> its = cb.getItems();
        its.clear();
        its.setAll(res_h.getList());
        //cb.getSelectionModel().selectFirst();
    }

    private void filterList(String search) {
        Platform.runLater(() -> {
            ObservableList<T> its = cb.getItems();
            if (!cb.isShowing()) {
                cb.show();
                lv = (ListView<T>) ((ComboBoxListViewSkin<?>) cb.getSkin()).getPopupContent();
            }
            for (T it : its) {
                if (it.displayable().toLowerCase().contains(search.toLowerCase())) {
                    lv.selectionModelProperty().get().select(it);
                    break;
                }
            }
        });
    }

}
