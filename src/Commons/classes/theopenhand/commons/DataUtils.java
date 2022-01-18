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

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author gabri
 */
public class DataUtils {

    private static final SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
    
    /**
     *
     * @param dt
     * @return
     */
    public static String format(Date dt) {
        return df.format(dt);
    }

    private DataUtils() {

    }

    /**
     *
     * @param ld
     * @return
     */
    public static Date toDate(LocalDate ld) {
        if (ld != null) {
            return Date.from(ld.atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toInstant());
        }
        return new Date(0);
    }

    /**
     *
     * @param d
     * @return
     */
    public static LocalDate toLocalDate(Date d) {
        if (d != null) {
            return Instant.ofEpochMilli(d.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return LocalDate.MIN;
    }

    /**
     *
     * @param s
     * @return
     */
    public static boolean softValidText(String s) {
        return s != null && !s.isEmpty() && !s.trim().isEmpty() && s.length() < 256;
    }

}
