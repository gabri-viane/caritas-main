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
import ttt.utils.engines.enums.MethodType;
import ttt.utils.engines.interfaces.EngineField;
import ttt.utils.engines.interfaces.EngineMethod;
import ttt.utils.xml.document.XMLElement;
import ttt.utils.xml.engine.annotations.Element;
import ttt.utils.xml.engine.annotations.Tag;

/**
 *
 * @author gabri
 */
@Element(Name = "profile")
public class ProfileElement extends XMLElement {

    @Tag(Name = "id", ValueType = Integer.class)
    @EngineField(FieldType = FieldType.READ_AND_WRITE)
    private Integer id;

    private AddressElement address;
    private CredentialsElement credentials;

    public ProfileElement() {
        super("profile");
    }

    @EngineMethod(MethodType = MethodType.CALC)
    public void setup() {
        address = (AddressElement) getFirstElement("address");
        credentials = (CredentialsElement) getFirstElement("credentials");
    }

    public void setArguments(AddressElement address, CredentialsElement credentials) {
        getElements().clear();
        addSubElement(address);
        addSubElement(credentials);
        this.address = address;
        this.credentials = credentials;
    }

    public AddressElement getAddress() {
        return address;
    }

    public CredentialsElement getCredentials() {
        return credentials;
    }

    public Integer getProfileID() {
        return id;
    }

}
