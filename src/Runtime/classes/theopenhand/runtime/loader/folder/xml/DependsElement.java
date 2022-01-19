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
package theopenhand.runtime.loader.folder.xml;

import java.util.UUID;
import ttt.utils.xml.document.XMLElement;
import ttt.utils.xml.engine.annotations.Element;

/**
 * Elemento che rappresenta una dipendenza di un plugin ad un'altro tramite
 * UUID. Questo elemento contiene il valore UUID relativo all'plugin da cui
 * dipende.
 *
 * @author gabri
 */
@Element(Name = "depends", CanHaveTags = false)
public class DependsElement extends XMLElement {

    public DependsElement() {
        super("depends");
    }

    public UUID getUUID() {
        return UUID.fromString(getValue());
    }

}
