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
package theopenhand.runtime.connection.runtime.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Supplier;
import java.util.stream.Stream;
import theopenhand.commons.Pair;
import theopenhand.commons.connection.runtime.custom.Clause;
import theopenhand.commons.connection.runtime.custom.ClauseFactory;
import theopenhand.commons.connection.runtime.custom.ClauseType;
import theopenhand.statics.privates.StaticData;

/**
 *
 * @author gabri
 */
public class QueryFormatter {

    public static Pair<String, LinkedList<Clause>> fixQueryString(String select_query, ArrayList<Clause> clauses) {
        StringBuilder sb = new StringBuilder("SELECT * FROM (");
        sb.append(select_query.replaceAll(";", "")).append(") AS ");
        sb.append(StaticData.TEMP_QUERY_NAME);

        LinkedList<Clause> cls = new LinkedList<>();

        shHlp(sb, ClauseType.WHERE, clauses, cls);
        shHlp(sb, ClauseType.ORDER_BY, clauses, cls);
        shHlp(sb, ClauseType.GROUP_BY, clauses, cls);

        return new Pair<>(sb.toString(), cls);
    }

    private static void shHlp(StringBuilder sb, ClauseType filter, ArrayList<Clause> clauses, LinkedList<Clause> ld) {
        Supplier<Stream<Clause>> sfilter = () -> clauses.stream().filter(cl -> {
            return cl.getClauseType().equals(filter);
        });
        if (sfilter.get().count() > 0) {
            sb.append(" ").append(filter).append(" ");
            Iterator<Clause> it = sfilter.get().iterator();
            boolean hn;
            do {
                Clause cl = it.next();
                sb.append(ClauseFactory.formatField(cl));
                hn = it.hasNext();
                if (hn) {
                    sb.append(filter.getSeparator());
                }
                ld.add(cl);
            } while (hn);
            sb.append(" ");
        }
    }

}
