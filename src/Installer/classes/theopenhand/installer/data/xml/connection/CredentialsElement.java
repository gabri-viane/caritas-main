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
@Element(Name = "credentials")
public class CredentialsElement extends XMLElement {

    @EngineField(FieldType = FieldType.READ_AND_WRITE)
    @Tag(Name = "user", ValueType = String.class)
    private String username;

    @EngineField(FieldType = FieldType.READ_AND_WRITE)
    @Tag(Name = "key", ValueType = String.class)
    private String password;

    public CredentialsElement() {
        super("credentials");
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
