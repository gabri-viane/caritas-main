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
import ttt.utils.engines.enums.MethodType;
import ttt.utils.engines.interfaces.EngineMethod;
import ttt.utils.xml.document.XMLElement;
import ttt.utils.xml.engine.annotations.Element;

/**
 *
 * @author gabri
 */
@Element(Name = "library", CanHaveTags = false)
public class LibraryElement extends XMLElement {

    private UUID uuid;

    public LibraryElement() {
        super("library");
    }

    @EngineMethod(MethodType = MethodType.CALC)
    public void calc() {
        uuid = UUID.fromString(getValue());
    }

    public UUID getUUID() {
        return uuid;
    }
}
