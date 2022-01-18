/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.runtime.connection.runtime.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.runtime.connection.runtime.types.CallableQueryStatement;
import theopenhand.runtime.connection.runtime.types.PreparedQueryStatement;
import theopenhand.runtime.connection.runtime.types.StringQueryBuilder;
import static theopenhand.runtime.connection.runtime.utils.Utils.boxPrimitiveClass;
import static theopenhand.runtime.connection.runtime.utils.Utils.isPrimitive;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 * @param <C>
 * @param <T>
 */
public final class ConnectionEngine<C extends BindableResult, T extends ResultHolder<C>> {

    private final Class<T> binded;
    private final Class<C> binded_result;
    private T instance;
    private StringQueryBuilder<C, T> queryBuilder;

    public ConnectionEngine(Class<T> to_bind, Class<C> result_class) {
        binded = to_bind;
        binded_result = result_class;
        init();
    }

    private void init() {
        try {
            instance = binded.getConstructor().newInstance();
            queryBuilder = new StringQueryBuilder<>(instance, binded_result);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ConnectionEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void executeQuery(int id) {
        executeQuery(id, null);
    }

    /**
     *
     * @param id Id della query da eseguire
     * @param assoc Classe risultato associata
     */
    public void executeQuery(int id, C assoc) {
        try {
            PreparedQueryStatement<C> query = queryBuilder.getQuery(id);
            instance.clearResults();
            try (ResultSet rs = query.execute(assoc)) {
                if (query.hasResult()) {
                    Constructor<C> cns = binded_result.getConstructor();
                    while (rs.next()) {
                        C inst = cns.newInstance();
                        copyData(rs, inst, queryBuilder);
                        instance.addResult(inst);
                    }
                }
            } catch (NoSuchMethodException ex) {
                DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore plugin", "Il plugin corrente contiene un'errore di programmazione, non è perciò possibile usarlo", null).show();
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException ex) {
            Logger.getLogger(ConnectionEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void executeCall(int id) {
        executeCall(id, null);
    }

    public void executeCall(int id, C assoc) {
        try {
            CallableQueryStatement<C> call = queryBuilder.getCallable(id);
            call.execute(assoc);
            instance.addResult(assoc);
        } catch (IllegalArgumentException | SecurityException ex) {
            Logger.getLogger(ConnectionEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void copyData(ResultSet rst, Object object, StringQueryBuilder<?, ?> pqs) throws SQLException, IllegalArgumentException, IllegalAccessException {
        Map<String, Field> fieldsMap = pqs.getFieldsMap();
        Set<String> keySet = fieldsMap.keySet();
        boolean skip_this_field;
        for (String s : keySet) {
            Field field = fieldsMap.get(s);
            int col;
            skip_this_field = false;
            field.setAccessible(true);
            try {
                col = rst.findColumn(s);
            } catch (SQLException sqe) {
                continue;
            }
            Object value = rst.getObject(col);
            Class<?> type = field.getType();
            if (isPrimitive(type)) {
                Class<?> boxed = boxPrimitiveClass(type);
                try {
                    if (boxed.equals(Boolean.class)) {
                        value = Integer.class.cast(value) == 1;
                    } else {
                        value = boxed.cast(value);
                    }
                } catch (java.lang.ClassCastException CCE) {
                    skip_this_field = true;
                }
            }
            if (type.equals(Boolean.class)) {
                value = Integer.class.cast(value) == 1;
            }
            if (!skip_this_field) {
                field.set(object, value);
            }
        }
    }

    public T getHolderInstance() {
        return instance;
    }

    public void close() {
        queryBuilder.close();
        queryBuilder = null;
        instance = null;
    }

}
