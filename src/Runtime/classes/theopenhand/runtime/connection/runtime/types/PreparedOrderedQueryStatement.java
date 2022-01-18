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
package theopenhand.runtime.connection.runtime.types;

import java.io.Closeable;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import theopenhand.commons.Pair;
import theopenhand.commons.connection.runtime.annotations.QueryField;
import theopenhand.commons.connection.runtime.annotations.QueryOrderBy;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class PreparedOrderedQueryStatement<T extends BindableResult> implements Closeable {

    private final QueryOrderBy qob;

    private PreparedStatement tmp_query;
    private final String query;
    private final boolean is_update;
    private final ArrayList<Field> fields;
    private final HashMap<String, Field> fs;
    private final HashMap<Integer, Pair<QueryField, Field>> fs2;

    private boolean has_result;

    public PreparedOrderedQueryStatement(String query, boolean is_update, QueryOrderBy qob) {
        this.query = query;
        this.is_update = is_update;
        fields = new ArrayList<>();
        fs = new HashMap<>();
        fs2 = new HashMap<>();
        this.qob = qob;
    }

    public ResultSet execute(T object) {
        try {
            tmp_query.clearParameters();
            if (object != null) {
                for (int i = 0, y = 1; i < fields.size(); i++, y++) {
                    Field f = fields.get(i);
                    Class<?> t = f.getType();
                    if (t == int.class || t == Integer.class) {
                        tmp_query.setInt(y, f.getInt(object));
                    } else if (t == float.class || t == Float.class) {
                        tmp_query.setFloat(y, f.getFloat(object));
                    } else if (t == double.class || t == Double.class) {
                        tmp_query.setDouble(y, f.getDouble(object));
                    } else if (t == long.class || t == Long.class) {
                        tmp_query.setLong(y, f.getLong(object));
                    } else if (t == boolean.class || t == Boolean.class) {
                        tmp_query.setBoolean(y, f.getBoolean(object));
                    } else if (t == String.class) {
                        tmp_query.setString(y, (String) f.get(object));
                    } else if (t == Blob.class) {
                        tmp_query.setBlob(y, (Blob) f.get(object));
                    } else if (t == Date.class) {
                        tmp_query.setDate(y, (java.sql.Date) f.get(object));
                    } else if (t == BigInteger.class) {
                        tmp_query.setLong(y, ((BigInteger) f.get(object)).longValue());
                    }
                }
            }
            if (is_update) {
                tmp_query.executeLargeUpdate();
                return null;
            } else {
                return tmp_query.executeQuery();
            }
        } catch (SQLException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(PreparedQueryStatement.class.getName()).log(Level.SEVERE, null, ex);
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore ricezione dati", "Non è stato possibile completare la richiesta di ricerca/aggiornamento dati.", null).show();
        }
        return null;
    }

    public void setFields(ArrayList<Field> fs) {
        this.fields.clear();
        this.fs.clear();
        Iterator<Field> iterator = fs.iterator();
        while (iterator.hasNext()) {
            Field f = iterator.next();
            fields.add(f);
            QueryField ann = f.getAnnotation(QueryField.class);
            this.fs.put(ann.name(), f);
            this.fs2.put(ann.fieldID(), new Pair<>(ann, f));
        }
    }

    public void addField(Field f) {
        fields.add(f);
    }

    public ArrayList<Field> getFields() {
        return this.fields;
    }

    public HashMap<String, Field> getFieldsMap() {
        return fs;
    }

    protected void setHasResult(boolean has_result) {
        this.has_result = has_result;
    }

    public boolean hasResult() {
        return has_result;
    }

    public HashMap<Integer, Pair<QueryField, Field>> getFieldIDs() {
        return fs2;
    }

    @Override
    public void close() {
        if (tmp_query != null) {
            try {
                tmp_query.close();
            } catch (SQLException ex) {
                Logger.getLogger(PreparedQueryStatement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public QueryOrderBy getOrdinableFields() {
        return qob;
    }

}
