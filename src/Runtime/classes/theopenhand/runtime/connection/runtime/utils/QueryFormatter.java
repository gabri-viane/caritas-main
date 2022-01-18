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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import theopenhand.commons.Pair;
import theopenhand.commons.connection.runtime.annotations.QueryField;
import theopenhand.runtime.connection.runtime.types.PreparedOrderedQueryStatement;

/**
 *
 * @author gabri
 */
public class QueryFormatter {

    private final static String ORDER_BY = "ORDER BY";
    private final static String ASC = " ASC";
    private final static String DESC = " DESC";

    public static enum SearchFormatter {
        LIKE, EQUALS;

        public static String format(SearchFormatter s, String name) {
            switch (s) {
                case EQUALS -> {
                    return name + " = ?";
                }
                case LIKE -> {
                    return name + " LIKE ?";
                }
                default -> {
                    return "";
                }
            }
        }

        public static String format(String value) {
            return value != null ? value.replaceAll("%", "\\%") : value;
        }

    }

    public static ArrayList<OrderType> availableOrderTypes(PreparedOrderedQueryStatement<?> pqs) {
        int[] orderBy = pqs.getOrdinableFields().orderBy();
        ArrayList<OrderType> arr = new ArrayList<>();
        for (int id : orderBy) {
            arr.add(new OrderType(id));
        }
        return arr;
    }

    public static String generateOrderBy(LinkedList<OrderType> sels, PreparedOrderedQueryStatement<?> pqs) {
        if (sels != null && !sels.isEmpty()) {
            HashMap<Integer, Pair<QueryField, Field>> fieldIDs = pqs.getFieldIDs();
            Iterator<OrderType> iterator = sels.iterator();

            StringBuilder sb = new StringBuilder(ORDER_BY);

            while (iterator.hasNext()) {
                OrderType next = iterator.next();
                Pair<QueryField, Field> p = fieldIDs.get(next.getFieldId());
                QueryField key = p.getKey();

                sb.append(key.name());
                if (next.isDescendant()) {
                    sb.append(DESC);
                } else {
                    sb.append(ASC);
                }
                if (iterator.hasNext()) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
        return "";
    }

}
