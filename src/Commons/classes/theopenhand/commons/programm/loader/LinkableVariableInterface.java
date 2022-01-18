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
package theopenhand.commons.programm.loader;

import theopenhand.commons.events.programm.utils.ChangeListener;

/**
 *
 * @author gabri
 * @param <T>
 */
public interface LinkableVariableInterface<T> {

    /**
     *
     * @return
     */
    public T getValue();

    /**
     *
     * @param b
     */
    public void setValue(T b);

    /**
     *
     * @param listener
     */
    public void addListener(ChangeListener<T> listener);

    /**
     *
     * @param listener
     */
    public void removeListener(ChangeListener<T> listener);

}
