/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.runtime.connection.runtime.types;

import java.io.Closeable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import theopenhand.statics.StaticReferences;
import theopenhand.commons.connection.runtime.annotations.Query;
import theopenhand.commons.connection.runtime.annotations.QueryField;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;

/**
 *
 * @author gabri
 * @param <X>
 * @param <T>
 */
public final class StringQueryBuilder<X extends BindableResult, T extends ResultHolder<X>> implements Closeable {

    private final T instance;
    private final Class<?> binded_class;
    private final Class<?> result_class;

    private final HashMap<String, Field> str_fields = new HashMap<>();
    private final HashMap<Integer, Field> n_fields = new HashMap<>();

    private final HashMap<Integer, PreparedQueryStatement<X>> queries = new HashMap<>();
    private final HashMap<Integer, CallableQueryStatement<X>> calls = new HashMap<>();

    public StringQueryBuilder(T instance, Class<X> binded) {
        this.instance = instance;
        result_class = instance.getClass();
        binded_class = binded;
        init();
    }

    private void init() {
        findFields();
        findQueries();
    }

    private void findQueries() {
        for (Field field : result_class.getDeclaredFields()) {
            field.setAccessible(true);
            Query q = field.getAnnotation(Query.class);
            if (field.getType() == String.class && q != null) {
                try {
                    String query = (String) field.get(instance);
                    if (q.isStoredProcedureCall()) {
                        CallableQueryStatement<X> cqs = prepareCallableStatement(query, q.isUpdate(), q.hasBindedParams(), q.outPrams());
                        calls.put(q.queryID(), cqs);
                    } else {
                        PreparedQueryStatement<X> pqs = prepareStatement(query, q.isUpdate(), q.hasResult());
                        queries.put(q.queryID(), pqs);
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(StringQueryBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void findFields() {
        for (Field field : binded_class.getDeclaredFields()) {
            field.setAccessible(true);
            QueryField qf = field.getAnnotation(QueryField.class);
            if (qf != null) {
                n_fields.put(qf.fieldID(), field);
                str_fields.put(qf.name(), field);
            }
        }
    }

    private ArrayList<Field> findOrederedFields(String query) {
        ArrayList<Field> fls = new ArrayList<>();
        Pattern p = Pattern.compile("(%N([0-9]++))");
        Matcher matcher = p.matcher(query);
        while (matcher.find()) {
            String numb = matcher.group(2);
            Integer id = Integer.parseInt(numb);
            fls.add(n_fields.get(id));
        }
        return fls;
    }

    private CallableQueryStatement<X> prepareCallableStatement(String query, boolean is_update, boolean has_bindings, int[] binds) {
        try {
            ArrayList<Field> fls = findOrederedFields(query);
            query = query.replaceAll("(%N[0-9]++)", "?");
            CallableStatement prepareCall = StaticReferences.getConnection().getConn().prepareCall(query);
            CallableQueryStatement<X> pc = new CallableQueryStatement<>(prepareCall, is_update);
            pc.setFields(fls);
            pc.setHasBindings(has_bindings);
            if (has_bindings) {
                pc.setBindings(binds);
            }
            return pc;
        } catch (SQLException ex) {
            Logger.getLogger(StringQueryBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private PreparedQueryStatement<X> prepareStatement(String query, boolean is_update, boolean has_result) {
        ArrayList<Field> fls = findOrederedFields(query);
        for (Field f : fls) {
            f.setAccessible(true);
            QueryField qf = f.getAnnotation(QueryField.class);
            query = query.replaceAll("%N" + qf.fieldID(), qf.name());
            query = query.replaceAll("%V" + qf.fieldID(), "?");
        }
        try {
            PreparedStatement ps = StaticReferences.getConnection().getConn().prepareStatement(query);
            PreparedQueryStatement<X> pqs = new PreparedQueryStatement<>(ps, is_update);
            pqs.setFields(fls);
            pqs.setHasResult(has_result);
            return pqs;
        } catch (SQLException ex) {
            Logger.getLogger(StringQueryBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*
    private PreparedOrderedQueryStatement<X> prepareOrderedStatement(String query, boolean is_update, boolean has_result, QueryOrderBy qob) {
        ArrayList<Field> fls = findOrederedFields(query);
        for (Field f : fls) {
            f.setAccessible(true);
            QueryField qf = f.getAnnotation(QueryField.class);
            query = query.replaceAll("%N" + qf.fieldID(), qf.name());
            query = query.replaceAll("%V" + qf.fieldID(), "?");
        }
        try {
            PreparedStatement ps = StaticReferences.getConnection().getConn().prepareStatement(query);
            PreparedOrderedQueryStatement<X> pqs = new PreparedOrderedQueryStatement<>(ps, is_update, qob);
            pqs.setFields(fls);
            pqs.setHasResult(has_result);
            return pqs;
        } catch (SQLException ex) {
            Logger.getLogger(StringQueryBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
     */
    public PreparedQueryStatement<X> getQuery(int i) {
        return queries.get(i);
    }

    public CallableQueryStatement<X> getCallable(int i) {
        return calls.get(i);
    }

    public Map<String, Field> getFieldsMap() {
        return Collections.unmodifiableMap(str_fields);
    }

    @Override
    public void close() {
        queries.values().forEach(PreparedQueryStatement::close);
        calls.values().forEach(CallableQueryStatement::close);
    }
}
