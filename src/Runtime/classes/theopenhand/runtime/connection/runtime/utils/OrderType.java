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

/**
 *
 * @author gabri
 */
public class OrderType {

    private final int fieldId;
    private boolean descendant;

    public OrderType(int field_id) {
        this.fieldId = field_id;
        this.descendant = true;
    }

    public OrderType(int field_id, boolean descendant) {
        this.fieldId = field_id;
        this.descendant = descendant;
    }

    public int getFieldId() {
        return fieldId;
    }

    public boolean isDescendant() {
        return descendant;
    }

    public void setDescendant(boolean descendant) {
        this.descendant = descendant;
    }

}
