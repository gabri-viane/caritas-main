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
package theopenhand.installer.data.xml.connection;

import ttt.utils.engines.enums.FieldType;
import ttt.utils.engines.interfaces.EngineField;
import ttt.utils.xml.document.XMLElement;
import ttt.utils.xml.engine.annotations.Element;
import ttt.utils.xml.engine.annotations.Tag;

/**
 *
 * @author gabri
 */
@Element(Name = "address", IgnoreSubElementsOnWrite = true)
public class AddressElement extends XMLElement {

    @EngineField(FieldType = FieldType.READ_AND_WRITE)
    @Tag(Name = "ip", ValueType = String.class)
    private String address;

    @EngineField(FieldType = FieldType.READ_AND_WRITE)
    @Tag(Name = "pt", ValueType = String.class)
    private String port;

    @EngineField(FieldType = FieldType.READ_AND_WRITE)
    @Tag(Name = "name", ValueType = String.class)
    private String name;

    public AddressElement() {
        super("address");
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return name;
    }

    public void setDatabaseName(String name) {
        this.name = name;
    }

}
