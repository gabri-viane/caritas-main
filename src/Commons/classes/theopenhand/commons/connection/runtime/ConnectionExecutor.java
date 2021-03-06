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
package theopenhand.commons.connection.runtime;

import java.util.ArrayList;
import java.util.Optional;
import theopenhand.commons.ReferenceQuery;
import theopenhand.commons.connection.runtime.custom.Clause;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.events.engines.DBCustomRequestHandler;
import theopenhand.commons.events.engines.DBCustomRequestListener;
import theopenhand.commons.events.engines.DBRequestHandler;
import theopenhand.commons.events.engines.DBRequestListener;
import theopenhand.runtime.templates.RuntimeReference;

/**
 *
 * @author gabri
 */
public class ConnectionExecutor {

    private static final ConnectionExecutor instance = new ConnectionExecutor();
    private final DBRequestHandler call_handler;
    private final DBRequestHandler query_handler;
    private final DBCustomRequestHandler order_query_handler;

    private ConnectionExecutor() {
        call_handler = new DBRequestHandler();
        query_handler = new DBRequestHandler();
        order_query_handler = new DBCustomRequestHandler();
    }

    /**
     *
     * @return
     */
    public static ConnectionExecutor getInstance() {
        return instance;
    }

    /**
     *
     * @param rr
     * @param id
     * @param clz
     * @param instance
     * @return
     */
    public Optional<ResultHolder> executeCall(RuntimeReference rr, int id, Class<? extends BindableResult> clz, BindableResult instance) {
        return call_handler.onRequest(rr, id, clz, instance);
    }

    /**
     *
     * @param rq
     * @param instance
     * @return
     */
    public Optional<ResultHolder> executeCall(ReferenceQuery rq, BindableResult instance) {
        return call_handler.onRequest(rq.getRuntime_reference(), rq.getQuery_id(), rq.getBinded_class(), instance);
    }

    /**
     *
     * @param rr
     * @param id
     * @param clz
     * @param instance
     * @return
     */
    public Optional<ResultHolder> executeQuery(RuntimeReference rr, int id, Class<? extends BindableResult> clz, BindableResult instance) {
        return query_handler.onRequest(rr, id, clz, instance);
    }

    public Optional<ResultHolder> executeQuery(ReferenceQuery rq, BindableResult instance) {
        return query_handler.onRequest(rq.getRuntime_reference(), rq.getQuery_id(), rq.getBinded_class(), instance);
    }

    /**
     *
     * @param rr
     * @param id
     * @param clz
     * @param instance
     * @param cls
     * @return
     */
    public Optional<ResultHolder> requestOrderQuery(RuntimeReference rr, int id, Class<? extends BindableResult> clz, BindableResult instance, ArrayList<Clause> cls) {
        return order_query_handler.onRequest(rr, id, clz, instance, cls);
    }

    public Optional<ResultHolder> requestOrderQuery(ReferenceQuery rq, BindableResult instance, ArrayList<Clause> cls) {
        return order_query_handler.onRequest(rq.getRuntime_reference(), rq.getQuery_id(), rq.getBinded_class(), instance, cls);
    }

    /**
     *
     * @param query_listener
     * @param call_listener
     */
    public void addListener(DBRequestListener query_listener, DBRequestListener call_listener) {
        call_handler.addListener(call_listener);
        query_handler.addListener(query_listener);
    }

    public void addListener(DBCustomRequestListener order_query) {
        order_query_handler.addListener(order_query);
    }
}
