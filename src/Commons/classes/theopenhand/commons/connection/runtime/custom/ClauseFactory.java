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
import theopenhand.commons.connection.runtime.annotations.QueryField;
import theopenhand.runtime.block.KeyUnlock;
import theopenhand.statics.privates.StaticData;

/**
 *
 * @author gabri
 */
public class ClauseFactory {

    private ClauseFactory() {

    }

    public static Clause generate(KeyUnlock key, Field f, QueryField qf) {
        return new Clause(f, qf);
    }

    public static Clause duplicate(Clause cl) {
        return new Clause(cl.getField(), cl.getQueryField());
    }

    public static String formatField(Clause fk) {
        if (fk != null) {
            StringBuilder sb = new StringBuilder();
            switch (fk.getClauseType()) {
                case GROUP_BY ->
                    sb.append(StaticData.TEMP_QUERY_NAME).append(".%N").append(fk.getQueryField().fieldID());
                case ORDER_BY ->
                    sb.append(StaticData.TEMP_QUERY_NAME).append(".%N").append(fk.getQueryField().fieldID()).append(" ").append(fk.getClauseData());
                case WHERE -> {
                    sb.append(StaticData.TEMP_QUERY_NAME).append(".%N").append(fk.getQueryField().fieldID()).append(" ").append(fk.getClauseData());
                    sb.append(" %V").append(fk.getQueryField().fieldID());
                }
            }
            return sb.toString();
        }
        return "";
    }

}
