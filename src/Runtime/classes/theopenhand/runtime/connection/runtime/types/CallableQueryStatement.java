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
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import theopenhand.commons.connection.runtime.annotations.QueryField;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.runtime.Utils;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 * @param <T>
 */
public class CallableQueryStatement<T extends BindableResult> implements Closeable {

    private final CallableStatement query;
    private final boolean is_update;
    private final ArrayList<Field> fields;
    private final HashMap<Integer, Field> fs;

    private int[] binded_fields;
    private boolean has_bindings;

    private Exception last_error;

    public CallableQueryStatement(CallableStatement query, boolean is_update) {
        this.query = query;
        this.is_update = is_update;
        fields = new ArrayList<>();
        fs = new HashMap<>();
    }

    public ResultSet execute(T object) {
        try {
            last_error = null;
            query.clearParameters();
            if (object != null) {
                for (int i = 0, y = 1; i < fields.size(); i++, y++) {
                    Field f = fields.get(i);
                    Class<?> t = f.getType();
                    int type = Types.OTHER;
                    Object ob = f.get(object);
                    boolean eval = ob != null;
                    if (t == int.class || t == Integer.class) {
                        type = Types.INTEGER;
                        if (eval) {
                            query.setInt(y, (Integer) ob);
                        }
                    } else if (t == float.class || t == Float.class) {
                        if (eval) {
                            query.setFloat(y, (Float) ob);
                        }
                        type = Types.FLOAT;
                    } else if (t == double.class || t == Double.class) {
                        if (eval) {
                            query.setDouble(y, (Double) ob);
                        }
                        type = Types.DOUBLE;
                    } else if (t == long.class || t == Long.class) {
                        if (eval) {
                            query.setLong(y, (Long) ob);
                        }
                        type = Types.BIGINT;
                    } else if (t == boolean.class || t == Boolean.class) {
                        if (eval) {
                            query.setBoolean(y, (Boolean) ob);
                        }
                        type = Types.BOOLEAN;
                    } else if (t == String.class) {
                        if (eval) {
                            query.setString(y, (String) ob);
                        }
                        type = Types.LONGVARCHAR;
                    } else if (t == Blob.class) {
                        if (eval) {
                            query.setBlob(y, (Blob) ob);
                        }
                        type = Types.BLOB;
                    } else if (t == Date.class) {
                        if (eval) {
                            query.setDate(y, new java.sql.Date(((Date) ob).getTime()));
                        }
                        type = Types.TIMESTAMP;
                    } else if (t == BigInteger.class) {
                        if (eval) {
                            query.setLong(y, ((BigInteger) ob).longValue());
                        }
                        type = Types.BIGINT;
                    }
                    if (!eval) {
                        query.setNull(y, type);
                    }
                    if (has_bindings && (binded_fields.length == 0 || Utils.contains(binded_fields, i))) {
                        QueryField qf = f.getAnnotation(QueryField.class);
                        if (qf.registerOut()) {
                            fs.put(y, f);
                            query.registerOutParameter(y, type);
                        }
                    }
                }
            }
            if (is_update) {
                query.executeLargeUpdate();
            } else {
                query.executeQuery();
            }

            if (has_bindings && object != null) {
                fs.forEach((k, v) -> {
                    try {
                        v.setAccessible(true);
                        Class<?> type = v.getType();
                        v.set(object, getValue(type, k, query));
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        Logger.getLogger(CallableQueryStatement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }

        } catch (java.sql.SQLIntegrityConstraintViolationException duplicate) {
            DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore Duplicato", "I valori inseriti creerebbero un duplicato.\nUno o più dati non sono stati perciò salvati.", null);
            last_error = duplicate;
            Logger.getLogger(PreparedQueryStatement.class.getName()).log(Level.SEVERE, null, duplicate);
        } catch (SQLException | IllegalArgumentException | IllegalAccessException ex) {
            last_error = ex;
            Logger.getLogger(PreparedQueryStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static Object getValue(Class<?> t, int id, CallableStatement qr) {
        try {
            if (t == int.class || t == Integer.class) {
                return qr.getInt(id);
            } else if (t == float.class || t == Float.class) {
                return qr.getFloat(id);
            } else if (t == double.class || t == Double.class) {
                return qr.getDouble(id);
            } else if (t == long.class || t == Long.class) {
                return qr.getLong(id);
            } else if (t == boolean.class || t == Boolean.class) {
                return qr.getBoolean(id);
            } else if (t == String.class) {
                return qr.getString(id);
            } else if (t == Blob.class) {
                return qr.getBlob(id);
            } else if (t == Date.class) {
                return qr.getDate(id);
            } else if (t == BigInteger.class) {
                return BigInteger.valueOf(qr.getLong(id));
            }
        } catch (SQLException sqle) {
            return null;
        }
        return null;
    }

    public void setFields(ArrayList<Field> fs) {
        this.fields.clear();
        Iterator<Field> iterator = fs.iterator();
        while (iterator.hasNext()) {
            fields.add(iterator.next());
        }
    }

    public void addField(Field f) {
        fields.add(f);
    }

    public ArrayList<Field> getFields() {
        return this.fields;
    }

    protected void setHasBindings(boolean has_bindings) {
        this.has_bindings = has_bindings;
    }

    @Override
    public void close() {
        try {
            query.close();
        } catch (SQLException ex) {
            Logger.getLogger(CallableQueryStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setBindings(int[] binds) {
        this.binded_fields = binds;
    }

    public Exception getLastError() {
        return last_error;
    }

}
