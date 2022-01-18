/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.runtime.connection.runtime.types;

import java.io.Closeable;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.ArrayList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import theopenhand.commons.Pair;
import theopenhand.commons.connection.runtime.annotations.QueryField;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 * @param <T>
 */
public class PreparedQueryStatement<T extends BindableResult> implements Closeable {

    private final PreparedStatement query;
    private final boolean is_update;
    private final ArrayList<Field> fields;
    private final HashMap<String, Field> fs;
    private final HashMap<Integer, Pair<QueryField, Field>> fs2;

    private boolean has_result;

    public PreparedQueryStatement(PreparedStatement query, boolean is_update) {
        this.query = query;
        this.is_update = is_update;
        fields = new ArrayList<>();
        fs = new HashMap<>();
        fs2 = new HashMap<>();
    }

    public ResultSet execute(T object) {
        try {
            query.clearParameters();
            if (object != null) {
                for (int i = 0, y = 1; i < fields.size(); i++, y++) {
                    Field f = fields.get(i);
                    Class<?> t = f.getType();
                    if (t == int.class || t == Integer.class) {
                        query.setInt(y, f.getInt(object));
                    } else if (t == float.class || t == Float.class) {
                        query.setFloat(y, f.getFloat(object));
                    } else if (t == double.class || t == Double.class) {
                        query.setDouble(y, f.getDouble(object));
                    } else if (t == long.class || t == Long.class) {
                        query.setLong(y, f.getLong(object));
                    } else if (t == boolean.class || t == Boolean.class) {
                        query.setBoolean(y, f.getBoolean(object));
                    } else if (t == String.class) {
                        query.setString(y, (String) f.get(object));
                    } else if (t == Blob.class) {
                        query.setBlob(y, (Blob) f.get(object));
                    } else if (t == Date.class) {
                        query.setDate(y, (java.sql.Date) f.get(object));
                    } else if (t == BigInteger.class) {
                        query.setLong(y, ((BigInteger) f.get(object)).longValue());
                    }
                }
            }
            if (is_update) {
                query.executeLargeUpdate();
                return null;
            } else {
                return query.executeQuery();
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
        try {
            query.close();
        } catch (SQLException ex) {
            Logger.getLogger(PreparedQueryStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
