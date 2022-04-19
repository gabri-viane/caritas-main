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
package theopenhand.window.graphics.commons.ordable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import theopenhand.commons.Pair;
import theopenhand.commons.connection.runtime.annotations.QueryCustom;
import theopenhand.commons.connection.runtime.annotations.QueryField;
import theopenhand.commons.connection.runtime.custom.Clause;
import theopenhand.commons.connection.runtime.custom.ClauseFactory;
import theopenhand.commons.connection.runtime.custom.ClauseType;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.runtime.block.KeyUnlock;
import theopenhand.window.graphics.commons.ordable.components.GroupElement;
import theopenhand.window.graphics.commons.ordable.components.OrderElement;
import theopenhand.window.graphics.commons.ordable.components.SearchElement;

/**
 *
 * @author gabri
 */
public class OrdableWindowFactory {

    private static final HashMap<Class<? extends BindableResult>, OrdableWindow> saved = new HashMap<>();
    private static final OrdableWindowFactory instance = new OrdableWindowFactory();

    private OrdableWindowFactory() {

    }

    public static OrdableWindowFactory getInstance() {
        return instance;
    }

    public static <T extends BindableResult> OrdableWindow<T> generate(Class<? extends BindableResult> cl, int query_id, T in) {
        if (!saved.containsKey(cl)) {
            ArrayList<Pair<Clause, QueryCustom>> cls = instance.findClauseFields(cl);
            OrdableWindow<T> ow = new OrdableWindow<>();
            for (Pair<Clause, QueryCustom> p : cls) {
                Clause cl_s = p.getKey();
                Clause tmp;
                QueryCustom enbls = p.getValue();
                for (ClauseType ct : enbls.enabled()) {
                    tmp = ClauseFactory.duplicate(cl_s);
                    switch (ct) {
                        case WHERE -> {
                            tmp.setClauseType(ClauseType.WHERE);
                            ow.addElement(new SearchElement<>(tmp, p.getValue(), in));
                        }
                        case ORDER_BY -> {
                            tmp.setClauseType(ClauseType.ORDER_BY);
                            ow.addElement(new OrderElement<>(tmp, p.getValue()));
                        }
                        case GROUP_BY -> {
                            tmp.setClauseType(ClauseType.GROUP_BY);
                            ow.addElement(new GroupElement<>(tmp, p.getValue()));
                        }
                    }
                }
            }
            saved.putIfAbsent(cl, ow);
            ow.trim();
            return ow;
        } else {
            OrdableWindow get = saved.get(cl);
            get.setInstance(in);
            return get;
        }
    }

    public static Map<String, Clause> getFiltrableFields(Class<? extends BindableResult> clbr) {
        Field[] declaredFields = clbr.getDeclaredFields();
        HashMap<String, Clause> cls = new HashMap<>();
        for (Field f : declaredFields) {
            QueryCustom qc = f.getAnnotation(QueryCustom.class);
            QueryField qf = f.getAnnotation(QueryField.class);
            if (qc != null && qf != null) {
                Clause generate = ClauseFactory.generate(KeyUnlock.KEY, f, qf);
                generate.setClauseType(ClauseType.WHERE);
                cls.put(qf.name(), generate);
            }
        }
        return Collections.unmodifiableMap(cls);
    }

    private ArrayList<Pair<Clause, QueryCustom>> findClauseFields(Class<? extends BindableResult> clbr) {
        Field[] declaredFields = clbr.getDeclaredFields();
        ArrayList<Pair<Clause, QueryCustom>> arr = new ArrayList<>();
        for (Field f : declaredFields) {
            QueryCustom qc = f.getAnnotation(QueryCustom.class);
            QueryField qf = f.getAnnotation(QueryField.class);
            if (qc != null && qf != null) {
                arr.add(new Pair<>(ClauseFactory.generate(KeyUnlock.KEY, f, qf), qc));
            }
        }
        return arr;
    }

}
