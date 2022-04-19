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
package theopenhand.window.graphics.creators;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import theopenhand.commons.DataUtils;
import theopenhand.commons.Pair;
import theopenhand.commons.ReferenceQuery;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.connection.runtime.custom.Clause;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.events.programm.FutureCallable;
import theopenhand.commons.events.programm.ValueAcceptListener;
import theopenhand.commons.interfaces.graphics.ColumnData;
import theopenhand.commons.interfaces.graphics.TableAssoc;
import theopenhand.window.graphics.commons.ordable.OrdableWindow;
import theopenhand.window.graphics.commons.ordable.OrdableWindowFactory;
import theopenhand.window.graphics.inner.DisplayTableValue;

/**
 *
 * @author gabri
 */
public class ElementCreator {

    /**
     *
     * @return
     */
    public static TextField buildNumericField() {
        TextField tf = new TextField();
        transformNumericField(tf);
        return tf;
    }

    /**
     *
     * @param tf
     */
    public static void transformNumericField(TextField tf) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        tf.setTextFormatter(textFormatter);
    }

    private ElementCreator() {

    }

    /**
     *
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T extends TableAssoc> DisplayTableValue<T> generateTable(Class<T> clazz) {
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

    public static <T extends BindableResult> OrdableWindow<T> getOrdableControl(ReferenceQuery<T, ? extends ResultHolder<T>> rq, T instance, FutureCallable<Void> fc, FutureCallable<Void> on_exit) {
        Class<T> cl = rq.getBinded_class();
        if (cl != null) {
            OrdableWindow<T> generate = OrdableWindowFactory.generate(cl, rq.getQuery_id(), instance);
            ValueAcceptListener<Optional<ArrayList<Clause>>> on_order = (value) -> {
                if (value.isPresent()) {
                    Optional<ResultHolder> requestOrderQuery = ConnectionExecutor.getInstance().requestOrderQuery(rq, instance, value.get());
                    if (fc != null) {
                        fc.execute(requestOrderQuery);
                    }
                }
            };
            generate.onAcceptPressed(() -> {
                on_order.onAccept(Optional.of(generate.generateClauses()));
            });
            generate.onExitPressed(() -> {
                on_order.onAccept(Optional.empty());
                on_exit.execute();
            });
            return generate;
        }
        return null;
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
