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
package theopenhand.runtime.data.components;

import java.io.Serializable;
import theopenhand.commons.events.graphics.ListableElement;

/**
 *
 * @author gabri
 */
public interface IDataElement extends Serializable, ListableElement {

    public String getName();

    public void setName(String name);

    public long getUid();

    public void setUid(long uid);
}
