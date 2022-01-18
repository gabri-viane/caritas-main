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
package theopenhand.runtime.ambient.xml;

import ttt.utils.engines.enums.FieldType;
import ttt.utils.engines.interfaces.EngineField;
import ttt.utils.xml.document.XMLElement;
import ttt.utils.xml.engine.annotations.Element;
import ttt.utils.xml.engine.annotations.Tag;

/**
 *
 * @author gabri
 */
@Element(Name = "property")
public class SettingField extends XMLElement {

    @EngineField(FieldType = FieldType.READ_AND_WRITE)
    @Tag(Name = "class")
    private String _clazz;

    @EngineField(FieldType = FieldType.READ_AND_WRITE)
    @Tag(Name = "id")
    private String id;

    public SettingField() {
        super("property");
    }

    public String getId() {
        return id;
    }

    public String getClazz() {
        return _clazz;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClazz(String _clazz) {
        this._clazz = _clazz;
    }

}
