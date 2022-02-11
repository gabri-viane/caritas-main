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
package theopenhand.commons.connection.runtime.custom;

import java.lang.reflect.Field;
import java.util.Objects;
import theopenhand.commons.Pair;
import theopenhand.commons.connection.runtime.annotations.QueryField;

/**
 *
 * @author gabri
 */
public class Clause {

    private final Field f;
    private final QueryField qf;
    private ClauseType ct;
    private ClauseData cd;
    private Pair<Object, Class<?>> vpair;

    public Clause() {
        this.f = null;
        this.qf = null;
    }

    protected Clause(Field f, QueryField qf) {
        this.f = f;
        this.qf = qf;
    }

    public void setClauseType(ClauseType ct) throws IllegalArgumentException {
        if (ct != null) {
            this.ct = ct;
        } else {
            throw new IllegalArgumentException("Argomento nullo non valido.");
        }
    }

    public void setClauseData(ClauseData cd) {
        this.cd = cd;
    }

    public ClauseData getClauseData() {
        return cd;
    }

    public ClauseType getClauseType() {
        return ct;
    }

    public void setValue(Object value, Class<?> vclass) {
        vpair = new Pair<>(value, vclass);
    }

    public Pair<Object, Class<?>> getValue() {
        return vpair;
    }

    public Field getField() {
        return f;
    }

    public QueryField getQueryField() {
        return qf;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Clause cl) {
            boolean b2 = cl.ct == ct;
            boolean b3 = cl.f.equals(f);
            return b2 && b3;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.f);
        hash = 97 * hash + Objects.hashCode(this.ct);
        return hash;
    }

}
