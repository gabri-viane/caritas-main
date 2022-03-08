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
package theopenhand.runtime.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import theopenhand.commons.Pair;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.events.engines.DBCustomRequestListener;
import theopenhand.commons.events.engines.DBRequestListener;
import theopenhand.commons.events.programm.utils.ListEventListener;
import theopenhand.runtime.SubscriptionHandler;
import theopenhand.runtime.connection.runtime.engine.ConnectionEngine;
import theopenhand.runtime.connection.runtime.engine.EngineBuilder;
import theopenhand.runtime.templates.RuntimeReference;

/**
 *
 * @author gabri
 */
public class DBResultLoader {

    private static final DBResultLoader instance = new DBResultLoader();
    private final HashMap<RuntimeReference, ArrayList<Pair<Class<? extends BindableResult>, ConnectionEngine>>> classes;

    private DBResultLoader() {
        classes = new HashMap<>();
        init();
    }

    /**
     *
     * @return
     */
    public static DBResultLoader getInstance() {
        return instance;
    }

    private void init() {
        load();
        SubscriptionHandler.addListener(new ListEventListener<RuntimeReference>() {
            @Override
            public void onElementAdded(RuntimeReference element) {

            }

            @Override
            public void onElementRemoved(RuntimeReference element) {
                unload(element);
            }
        });
        DBRequestListener query_listener = (RuntimeReference rr, int id, Class<? extends BindableResult> clz, BindableResult instance1) -> {
            ConnectionEngine findEngine = EngineBuilder.getInstance().findEngine(clz);
            findEngine.executeQuery(id, instance1);
            return findEngine.getHolderInstance();
        };
        DBRequestListener call_listener = (RuntimeReference rr, int id, Class<? extends BindableResult> clz, BindableResult instance1) -> {
            ConnectionEngine findEngine = EngineBuilder.getInstance().findEngine(clz);
            ResultHolder<?> rc = findEngine.getHolderInstance();
            rc.setLastException(findEngine.executeCall(id, instance1));
            return rc;
        };
        ConnectionExecutor.getInstance().addListener(query_listener, call_listener);
        DBCustomRequestListener order_listener = (rr, id, clz, inst, vals) -> {
            ConnectionEngine findEngine = EngineBuilder.getInstance().findEngine(clz);
            findEngine.executeCustomQuery(id, inst, vals);
            return findEngine.getHolderInstance();
        };
        ConnectionExecutor.getInstance().addListener(order_listener);
    }

    /**
     *
     */
    public void load() {
        List<Pair<RuntimeReference, Pair<Class<? extends ResultHolder<?>>, Class<? extends BindableResult>>>> dbObjects = SubscriptionHandler.getDBObjects();
        EngineBuilder eb = EngineBuilder.getInstance();
        dbObjects.forEach(p -> {
            RuntimeReference rr = p.getKey();
            Pair<Class<? extends ResultHolder<?>>, Class<? extends BindableResult>> p2 = p.getValue();
            ArrayList<Pair<Class<? extends BindableResult>, ConnectionEngine>> engs = classes.get(rr);
            if (engs == null) {
                engs = new ArrayList<>();
                classes.put(rr, engs);
            }
            engs.add(new Pair(p2.getValue(), eb.registerClass(p2.getKey(), p2.getValue())));
        });
    }

    /**
     * Data un {@link RuntimeReference} prende l'istanza di
     * {@link EngineBuilder} e richiede tutte le {@link BindableResult}
     * sottoscritte al caricamento da questa <i>Reference</i>.
     * <br>
     * Successivamente rimuove tutti i {@link ConnectionEngine} associati alle
     * classi registrate.
     *
     * @param rr La {@link RuntimeReference} da scaricare (disattivare per la
     * sessione corrente).
     */
    public void unload(RuntimeReference rr) {
        EngineBuilder eb = EngineBuilder.getInstance();
        ArrayList<Pair<Class<? extends BindableResult>, ConnectionEngine>> get = classes.remove(rr);
        if (get != null) {
            get.forEach(p -> {
                eb.removeConnectionEngine(p.getKey());
            });
        }
    }

}
