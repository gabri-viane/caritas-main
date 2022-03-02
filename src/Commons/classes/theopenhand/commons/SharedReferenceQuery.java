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
package theopenhand.commons;

import java.util.HashMap;
import java.util.Optional;
import javafx.scene.control.Alert;
import theopenhand.commons.connection.runtime.ConnectionExecutor;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.events.programm.FutureCallable;
import theopenhand.runtime.templates.ReferenceController;
import theopenhand.runtime.templates.RuntimeReference;
import theopenhand.window.graphics.dialogs.DialogCreator;

/**
 *
 * @author gabri
 */
public class SharedReferenceQuery {

    public static enum EXECUTION_REQUEST {
        QUERY, CALL, CUSTOM_QUERY
    }

    private final HashMap<Class<? extends ReferenceController>, ReferenceController> controllers_pool;
    private final HashMap<String, RuntimeReference> references;
    private final HashMap<RuntimeReference, HashMap<String, Pair<Class<? extends BindableResult>, ResultHolder>>> holders;
    private static final SharedReferenceQuery instance = new SharedReferenceQuery();

    private SharedReferenceQuery() {
        this.holders = new HashMap<>();
        this.references = new HashMap<>();
        this.controllers_pool = new HashMap<>();
    }

    public static SharedReferenceQuery getInstance() {
        return instance;
    }

    public void register(RuntimeReference rr, String reference_query_name, Class<? extends BindableResult> bindable_result, Class<? extends ReferenceController> rc) {
        if (controllers_pool.containsKey(rc)) {
            if (references.containsKey(rr.getName())) {
                HashMap<String, Pair<Class<? extends BindableResult>, ResultHolder>> get = holders.get(rr);
                get.put(reference_query_name, new Pair<>(bindable_result, controllers_pool.get(rc).getRH()));
            } else {
                HashMap<String, Pair<Class<? extends BindableResult>, ResultHolder>> hm = insertRR(rr);
                hm.put(reference_query_name, new Pair<>(bindable_result, controllers_pool.get(rc).getRH()));
            }
        } else {
            controllers_pool.put(rc, null);
        }
    }

    public void unregister(RuntimeReference rr) {
        references.remove(rr.getName());
        holders.remove(rr);
    }

    public Optional<ReferenceQuery> generate(String rr_name, String p_name, int query_id) {
        RuntimeReference get = references.get(rr_name);
        if (get != null) {
            HashMap<String, Pair<Class<? extends BindableResult>, ResultHolder>> get1 = holders.get(get);
            Pair<Class<? extends BindableResult>, ResultHolder> get2 = get1.get(p_name);
            if (get2 != null) {
                ReferenceQuery rq = new ReferenceQuery(get, get2.getKey(), get2.getValue(), query_id);
                return Optional.of(rq);
            }
        }
        return Optional.empty();
    }

    public ReferenceQuery forceGenerate(String rr_name, String p_name, int query_id) {
        RuntimeReference get = references.get(rr_name);
        if (get != null) {
            HashMap<String, Pair<Class<? extends BindableResult>, ResultHolder>> get1 = holders.get(get);
            Pair<Class<? extends BindableResult>, ResultHolder> get2 = get1.get(p_name);
            if (get2 != null) {
                ReferenceQuery rq = new ReferenceQuery(get, get2.getKey(), get2.getValue(), query_id);
                return rq;
            }
        }
        DialogCreator.showAlert(Alert.AlertType.ERROR, "Errore plugin", "Il plugin che gestisce l'aggiornamento di questi dati contiene un errore.", null);
        return null;
    }

    private HashMap<String, Pair<Class<? extends BindableResult>, ResultHolder>> insertRR(RuntimeReference rr) {
        references.put(rr.getName(), rr);
        HashMap<String, Pair<Class<? extends BindableResult>, ResultHolder>> hm = new HashMap<>();
        holders.put(rr, hm);
        return hm;
    }

    public void registerRR(RuntimeReference rr) {
        references.put(rr.getName(), rr);
        rr.getPluginReferenceControllers().forEach((t) -> {
            controllers_pool.put(t.getClass(), t);
        });
        HashMap<String, Pair<Class<? extends BindableResult>, ResultHolder>> hm = new HashMap<>();
        holders.put(rr, hm);
    }

    public static <T extends BindableResult> void execute(ReferenceQuery<T, ? extends ResultHolder<T>> rq, T instance, EXECUTION_REQUEST er) {
        execute(rq, instance, er, null);
    }

    public static <T extends BindableResult> void execute(ReferenceQuery<T, ? extends ResultHolder<T>> rq, T instance, EXECUTION_REQUEST er, FutureCallable<Void> fc) {
        if (rq != null) {
            switch (er) {
                case CALL -> {
                    Optional<ResultHolder> executeCall = ConnectionExecutor.getInstance().executeCall(rq, instance);
                    if (fc != null) {
                        fc.execute(executeCall);
                    }
                }
                case QUERY -> {
                    Optional<ResultHolder> executeQuery = ConnectionExecutor.getInstance().executeQuery(rq, instance);
                    if (fc != null) {
                        fc.execute(executeQuery);
                    }
                }
                case CUSTOM_QUERY -> {
                    DialogCreator.createSearcher(rq, instance, (value) -> {
                        if (value.isPresent()) {
                            Optional<ResultHolder> requestOrderQuery = ConnectionExecutor.getInstance().requestOrderQuery(rq, instance, value.get());
                            if (fc != null) {
                                fc.execute(requestOrderQuery);
                            }
                        }
                    });
                }
            }
        }
    }

    public static <T extends ReferenceController> T getController(Class<T> cl) {
        ReferenceController get = instance.controllers_pool.get(cl);
        if (get != null) {
            return cl.cast(get);
        }
        return null;
    }

}
