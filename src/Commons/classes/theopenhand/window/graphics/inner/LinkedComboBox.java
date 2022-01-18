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

import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.interfaces.graphics.DisplayableValue;

/**
 *
 * @author gabri
 * @param <X>
 * @param <T>
 */
public class LinkedComboBox<X extends BindableResult, T> extends ComboBox<DisplayableValue<X, T>> {

    private ResultHolder<X> rs;

    /**
     *
     */
    public LinkedComboBox() {

    }

    /**
     *
     * @param dv
     */
    public void addElement(DisplayableValue<X, T> dv) {
        getItems().add(dv);
    }

    /**
     *
     * @param dv
     */
    public void removeElement(DisplayableValue<X, T> dv) {
        getItems().remove(dv);
    }

    /**
     *
     * @param value
     */
    public void removeElements(X value) {
        Iterator<DisplayableValue<X, T>> iterator = getItems().iterator();
        while (iterator.hasNext()) {
            DisplayableValue<X, T> next = iterator.next();
            if (next.equals(value)) {
                iterator.remove();
            }
        }
    }

    /**
     *
     * @param r
     */
    public void linkTo(ResultHolder<X> r) {
        rs = r;
    }

    /**
     *
     */
    public void populateByRH() {
        if (rs != null) {
            ObservableList<DisplayableValue<X, T>> itms = getItems();
            itms.clear();
            rs.getList().forEach(e -> {
                DisplayableValue dv = new DisplayableValue<>(e, e.toString());
                itms.add(dv);
            });
            if (itms.size() > 0) {
                this.getSelectionModel().selectFirst();
            }
        }
    }

    /**
     *
     * @return
     */
    public X getSelected() {
        SingleSelectionModel<DisplayableValue<X, T>> selectionModel = this.getSelectionModel();
        if (selectionModel != null && !selectionModel.isEmpty()) {
            return selectionModel.getSelectedItem().getValue();
        }
        return null;
    }

    /**
     *
     * @param value
     */
    public void selectElement(X value) {
        Iterator<DisplayableValue<X, T>> iterator = getItems().iterator();
        while (iterator.hasNext()) {
            DisplayableValue<X, T> next = iterator.next();
            if (next.equals(value)) {
                this.getSelectionModel().select(next);
                return;
            }
        }
    }

    /**
     *
     * @param value
     */
    public void selectElement(T value) {
        Iterator<DisplayableValue<X, T>> iterator = getItems().iterator();
        while (iterator.hasNext()) {
            DisplayableValue<X, T> next = iterator.next();
            if (next.getDisplay().equals(value)) {
                this.getSelectionModel().select(next);
                return;
            }
        }
    }

}
