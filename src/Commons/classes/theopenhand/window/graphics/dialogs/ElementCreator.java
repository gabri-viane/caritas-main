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
package theopenhand.window.graphics.dialogs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import theopenhand.commons.DataUtils;
import theopenhand.commons.Pair;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.interfaces.graphics.ColumnData;
import theopenhand.commons.interfaces.graphics.TableAssoc;
import theopenhand.window.graphics.inner.DisplayTableValue;

/**
 *
 * @author gabri
 */
public class ElementCreator {

    private ElementCreator() {

    }

    /**
     *
     * @param <T>
     * @param clazz
     * @param res
     * @return
     */
    public static <T extends TableAssoc> DisplayTableValue<T> generateTable(Class<T> clazz, ResultHolder<T> res) {
        DisplayTableValue<T> table = new DisplayTableValue<>();
        Field[] dfs = clazz.getDeclaredFields();
        HashMap<String, Field> fields = new HashMap<>();
        ArrayList<Pair<Integer, String>> str = new ArrayList<>();
        for (Field f : dfs) {
            f.setAccessible(true);
            ColumnData annotation = f.getAnnotation(ColumnData.class);
            if (annotation != null) {
                fields.put(annotation.Title(), f);
                str.add(new Pair(annotation.order(), annotation.Title()));
            }
        }
        Collections.sort(str, (o1, o2) -> {
            return o1.getKey().compareTo(o2.getKey());
        });

        str.stream().forEachOrdered(p -> {
            String title = p.getValue();
            Field field = fields.get(title);
            TableColumn<T, String> col = new TableColumn<>(title);
            col.setCellValueFactory(new PVFExtension(field));
            table.addColumn(col);
        });
        return table;
    }

    static class PVFExtension<S, T> extends PropertyValueFactory<S, T> {

        private final Field field;

        public PVFExtension(Field f) {
            super(f.getName());
            this.field = f;
        }

        @Override
        public ObservableValue<T> call(TableColumn.CellDataFeatures<S, T> cdf) {
            Class<?> type = field.getType();
            String val = "N/A";
            try {
                if (type.equals(Date.class)) {
                    Date d = (Date) field.get(cdf.getValue());
                    if (d != null) {
                        val = DataUtils.format(d);
                    }
                } else if (type.equals(Boolean.class)) {
                    Boolean b = (Boolean) field.get(cdf.getValue());
                    if (b != null) {
                        val = b ? "Sì" : "No";
                    }
                } else if (cdf.getValue() != null) {
                    return super.call(cdf);
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(ElementCreator.class.getName()).log(Level.SEVERE, null, ex);
            }
            return new SimpleObjectProperty(val);
        }

    }

}
