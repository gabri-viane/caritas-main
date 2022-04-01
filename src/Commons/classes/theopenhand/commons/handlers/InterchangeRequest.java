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
package theopenhand.commons.handlers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.stage.Stage;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.events.programm.FutureDialogRequest;
import theopenhand.commons.events.programm.FutureObjectRequest;
import theopenhand.commons.events.programm.ValueAcceptListener;
import theopenhand.commons.interfaces.ExchangeID;
import theopenhand.window.graphics.creators.DialogCreator;

/**
 *
 * @author gabri
 */
public class InterchangeRequest {

    private final static HashMap<String, Class<? extends ExchangeID<?>>> registered = new HashMap<>();
    private final static HashMap<String, FutureDialogRequest> registered_r = new HashMap<>();
    private final static HashMap<String, FutureObjectRequest<? extends BindableResult>> registered_o = new HashMap<>();
    private static final BooleanProperty freed = new SimpleBooleanProperty(true);
    private static final Queue<Runnable> queue = new LinkedList<>();

    static {
        freed.addListener((o) -> {
            if (freed.getValue() && !queue.isEmpty()) {
                queue.remove().run();
            }
        });
    }

    private InterchangeRequest() {

    }

    /**
     *
     * @param request
     * @param eid
     */
    public static void register(String request, Class<? extends ExchangeID<?>> eid) {
        if (!registered.containsKey(request)) {
            registered.put(request, eid);
        }
    }

    /**
     *
     * @param request
     * @param r
     */
    public static void register(String request, FutureDialogRequest r) {
        if (!registered_r.containsKey(request)) {
            registered_r.put(request, r);
        }
    }

    /**
     *
     * @param request
     * @param r
     */
    public static void register(String request, FutureObjectRequest<? extends BindableResult> r) {
        if (!registered_o.containsKey(request)) {
            registered_o.put(request, r);
        }
    }

    /**
     *
     * @param request
     * @param id
     * @return
     */
    public static BindableResult generateFromID(String request, Long id) {
        FutureObjectRequest<? extends BindableResult> get = registered_o.get(request);
        if (get != null) {
            return get.castToObject(id);
        }
        return null;
    }

    /**
     *
     * @param request
     * @param val
     * @param o
     */
    public static void request(String request, ValueAcceptListener<Optional<Long>> val, Object o) {
        Class<? extends ExchangeID<?>> get = registered.get(request);
        if (get != null) {
            Runnable r = () -> {
                try {
                    Constructor<? extends ExchangeID<?>> constructor = get.getConstructor();
                    ExchangeID<?> e = constructor.newInstance();
                    Parent node = e.getParentNode();
                    Stage showDialog = DialogCreator.showDialog(node, e.getDialogWidth(), e.getDialogHeight(), null);
                    freed.setValue(Boolean.FALSE);
                    ClickListener cl = () -> {
                        val.onAccept(Optional.empty());
                        showDialog.close();
                        freed.setValue(Boolean.TRUE);
                    };
                    showDialog.setOnCloseRequest(eh -> {
                        cl.onClick();
                    });
                    e.onExitPressed(cl);
                    e.onAcceptPressed(() -> {
                        val.onAccept(Optional.of(e.getID()));
                        showDialog.close();
                        freed.setValue(Boolean.TRUE);
                    });
                    e.onRefresh(true);
                    if (o != null) {
                        e.selectNullable(o);
                    }
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(InterchangeRequest.class.getName()).log(Level.SEVERE, null, ex);
                }
            };
            if (freed.getValue()) {
                r.run();
            } else {
                queue.add(r);
            }
        } else {
            FutureDialogRequest get1 = registered_r.get(request);
            if (get1 != null) {
                Runnable r = () -> {
                    try {
                        ExchangeID<?> e = get1.onRequest();
                        Parent node = e.getParentNode();
                        Stage showDialog = DialogCreator.showDialog(node, e.getDialogWidth(), e.getDialogHeight(), null);
                        freed.setValue(Boolean.FALSE);
                        ClickListener cl = () -> {
                            val.onAccept(Optional.empty());
                            showDialog.close();
                            freed.setValue(Boolean.TRUE);
                        };
                        showDialog.setOnCloseRequest(eh -> {
                            cl.onClick();
                        });
                        e.onExitPressed(cl);
                        e.onAcceptPressed(() -> {
                            val.onAccept(Optional.of(e.getID()));
                            showDialog.close();
                            freed.setValue(Boolean.TRUE);
                        });
                        e.onRefresh(true);
                        if (o != null) {
                            e.selectNullable(o);
                        }
                    } catch (IllegalArgumentException | SecurityException ex) {
                        Logger.getLogger(InterchangeRequest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                };
                if (freed.getValue()) {
                    r.run();
                } else {
                    queue.add(r);
                }
            } else {
                val.onAccept(Optional.empty());
            }
        }
    }

    public static boolean isAvailable(String request) {
        return registered.containsKey(request) || registered_r.containsKey(request) || registered_o.containsKey(request);
    }

}
