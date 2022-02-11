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
package theopenhand.commons.connection.runtime.impls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;

/**
 *
 * @author gabri
 * @param <T>
 */
public class ResultHolderImpl<T extends BindableResult> implements ResultHolder<T> {

    ArrayList<T> ordered = new ArrayList<>();
    HashMap<Long, T> vals = new HashMap<>();
    private T last_insert;
    private Exception last_ex;

    /**
     *
     */
    public ResultHolderImpl() {
    }

    /**
     *
     * @param single_row
     */
    @Override
    public void addResult(T single_row) {
        var id = single_row.getID();
        if (id != null) {
            vals.put(id.longValue(), single_row);
            ordered.add(single_row);
            last_insert = single_row;
        }
    }

    /**
     *
     * @param rows
     */
    @Override
    public void addResults(List<T> rows) {
        rows.stream().forEach(f -> {
            var id = f.getID();
            if (id != null) {
                vals.put(id.longValue(), f);
            }
        });
        ordered.addAll(rows);
    }

    /**
     *
     */
    @Override
    public void clearResults() {
        vals.clear();
        ordered.clear();
    }

    /**
     *
     * @return
     */
    @Override
    public T getLastInsert() {
        return last_insert;
    }

    /**
     *
     * @return
     */
    @Override
    public List<T> getList() {
        return ordered;
    }

    /**
     *
     * @param id
     * @return
     */
    public T find(long id) {
        return vals.get(id);
    }

    @Override
    public Exception getExecutionException() {
        return last_ex;
    }

    @Override
    public void setLastException(Exception e) {
        last_ex = e;
    }

}
