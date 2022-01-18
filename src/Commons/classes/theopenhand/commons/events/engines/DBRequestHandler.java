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
package theopenhand.commons.events.engines;

import java.util.ArrayList;
import java.util.Optional;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.runtime.templates.RuntimeReference;

/**
 *
 * @author gabri
 */
public class DBRequestHandler {

    private final ArrayList<DBRequestListener> listeners = new ArrayList<>();

    /**
     *
     */
    public DBRequestHandler() {
    }

    /**
     *
     * @param rr
     * @param id
     * @param clz
     * @param instance
     * @return
     */
    public Optional<ResultHolder> onRequest(RuntimeReference rr, int id, Class<? extends BindableResult> clz, BindableResult instance) {
        ArrayList<ResultHolder> res = new ArrayList<>();
        listeners.forEach(l -> res.add(l.executeRequest(rr, id, clz, instance)));
        return res.stream().findFirst();
    }

    /**
     *
     * @param dbrl
     */
    public void addListener(DBRequestListener dbrl) {
        listeners.add(dbrl);
    }

    /**
     *
     * @param dbrl
     */
    public void removeListener(DBRequestListener dbrl) {
        listeners.remove(dbrl);
    }
}
