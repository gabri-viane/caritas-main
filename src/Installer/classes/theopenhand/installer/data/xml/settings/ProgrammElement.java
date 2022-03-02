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
package theopenhand.installer.data.xml.settings;

import ttt.utils.engines.enums.FieldType;
import ttt.utils.engines.interfaces.EngineField;
import ttt.utils.xml.document.XMLElement;
import ttt.utils.xml.engine.annotations.Element;
import ttt.utils.xml.engine.annotations.Tag;

/**
 *
 * @author gabri
 */
@Element(Name = "programm")
public class ProgrammElement extends XMLElement {

    @EngineField(FieldType = FieldType.READ_AND_WRITE)
    @Tag(Name = "ver", ValueType = Long.class)
    private Long version;

    @EngineField(FieldType = FieldType.READ_AND_WRITE)
    @Tag(Name = "au", ValueType = Boolean.class)
    private boolean auto_update ;

    public ProgrammElement() {
        super("programm");
    }

    public Boolean getAutoUpdate() {
        return auto_update;
    }

    public void setAutoUpdate(Boolean auto_update) {
        this.auto_update = auto_update;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
