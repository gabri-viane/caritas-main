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
package theopenhand.commons.interfaces.graphics;

import java.util.Objects;

/**
 *
 * @author gabri
 * @param <X>
 * @param <T>
 */
public class DisplayableValue<X, T> {

    private final X value;
    private final T display;

    /**
     *
     * @param x
     * @param display
     */
    public DisplayableValue(X value, T display) {
        if (value != null && display != null) {
            this.value = value;
            this.display = display;
        } else {
            throw new NullPointerException("DisplayableValue non valido");
        }
    }

    /**
     *
     * @return
     */
    public T getDisplay() {
        return display;
    }

    /**
     *
     * @return
     */
    public X getValue() {
        return value;
    }

    @Override
    public String toString() {
        return display.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof DisplayableValue) {
                DisplayableValue kd = (DisplayableValue) obj;
                return value.equals(kd.value);
            } else {
                return value.equals(obj);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.value);
        return hash;
    }

}
