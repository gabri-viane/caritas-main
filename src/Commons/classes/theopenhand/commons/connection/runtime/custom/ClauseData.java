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

/**
 *
 * @author gabri
 */
public enum ClauseData {
    ASC_ORDER("ASC", "Crescente"), DESC_ORDER("DESC", "Decrescente"), LIKE("LIKE", "Simile a"),
    EQUALS("=", "Uguale"), LESS_THAN("<", "Minore"), MORE_THAN(">", "Maggiore"), LEQUALS("<=", "Minore uguale"), MEQUALS(">=", "Maggiore uguale");

    private final String data;
    private final String stringify;

    private ClauseData(String s, String s2) {
        data = s;
        stringify = s2;
    }

    public String getStringify() {
        return stringify;
    }

    @Override
    public String toString() {
        return data;
    }

    public static ClauseData fromString(String s) {
        return switch (s.toLowerCase()) {
            case "crescente" ->
                ASC_ORDER;
            case "descrescente" ->
                DESC_ORDER;
            case "simile a" ->
                LIKE;
            case "uguale" ->
                EQUALS;
            case "minore" ->
                LESS_THAN;
            case "maggiore" ->
                MORE_THAN;
            case "minore uguale" ->
                LEQUALS;
            case "maggiore uguale" ->
                MEQUALS;
            default ->
                null;
        };
    }

}
