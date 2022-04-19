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
package theopenhand.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import theopenhand.commons.connection.runtime.custom.Clause;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.runtime.templates.RuntimeReference;

/**
 *
 * @author gabri
 * @param <T>
 * @param <X>
 */
public class ReferenceQuery<T extends BindableResult, X extends ResultHolder<T>> {

    private RuntimeReference runtime_reference;
    private Class<T> binded_class;
    private X result_holder;
    private int query_id;

    private ArrayList<Clause> cls;

    /**
     *
     */
    public ReferenceQuery() {
    }

    /**
     *
     * @param rr
     * @param binded_class
     * @param result_holder
     * @param query_id
     */
    public ReferenceQuery(RuntimeReference rr, Class<T> binded_class, X result_holder, int query_id) {
        this.runtime_reference = rr;
        this.binded_class = binded_class;
        this.result_holder = result_holder;
        this.query_id = query_id;
    }

    /**
     *
     * @param runtime_reference
     */
    public void setRuntime_reference(RuntimeReference runtime_reference) {
        this.runtime_reference = runtime_reference;
    }

    /**
     *
     * @param binded_class
     */
    public void setBinded_class(Class<T> binded_class) {
        this.binded_class = binded_class;
    }

    /**
     *
     * @param result_holder
     */
    public void setResult_holder(X result_holder) {
        this.result_holder = result_holder;
    }

    /**
     *
     * @param query_id
     */
    public void setQuery_id(int query_id) {
        this.query_id = query_id;
    }

    /**
     *
     * @return
     */
    public RuntimeReference getRuntime_reference() {
        return runtime_reference;
    }

    /**
     *
     * @return
     */
    public Class<T> getBinded_class() {
        return binded_class;
    }

    /**
     *
     * @return
     */
    public X getResult_holder() {
        return result_holder;
    }

    /**
     *
     * @return
     */
    public int getQuery_id() {
        return query_id;
    }

    public void subcribeClauses(Clause... clss) {
        if (cls != null) {
            cls.clear();
        }
        cls = new ArrayList<>();
        cls.addAll(Arrays.asList(clss));
    }

    public void subscribeClauses(List<Clause> clss) {
        if (cls != null) {
            cls.clear();
        }
        cls = new ArrayList<>();
        cls.addAll(clss);
    }

    public ArrayList<Clause> flushClauses() {
        if (cls != null) {
            return cls;
        }
        return new ArrayList<>();
    }

}
