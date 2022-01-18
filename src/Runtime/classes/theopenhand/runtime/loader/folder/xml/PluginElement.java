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
package theopenhand.runtime.loader.folder.xml;

import java.util.Objects;
import java.util.UUID;
import ttt.utils.engines.enums.FieldType;
import ttt.utils.engines.interfaces.EngineField;
import ttt.utils.xml.document.XMLElement;
import ttt.utils.xml.engine.annotations.Element;
import ttt.utils.xml.engine.annotations.Tag;

/**
 *
 * @author gabri
 */
@Element(Name = "plugin")
public class PluginElement extends XMLElement {

    @EngineField(FieldType = FieldType.READ_AND_WRITE)
    @Tag(Name = "file")
    private String file_path;

    @EngineField(FieldType = FieldType.READ_AND_WRITE)
    @Tag(Name = "class")
    private String class_path;

    @EngineField(FieldType = FieldType.READ_AND_WRITE)
    @Tag(Name = "name")
    private String plugin_name;

    @EngineField(FieldType = FieldType.READ_AND_WRITE)
    @Tag(Name = "uuid")
    private String uuid;

    @EngineField(FieldType = FieldType.READ_AND_WRITE)
    @Tag(Name = "ver")
    private String version;

    public PluginElement() {
        super("plugin");
    }

    public PluginElement(String file_path, String name, String class_path, String version, String uuid) {
        super("plugin");
        this.file_path = file_path;
        this.class_path = class_path;
        this.uuid = uuid;
        this.plugin_name = name;
        this.version =version;
    }

    public PluginElement(String file_path, String name, String class_path, String version, UUID uuid) {
        super("plugin");
        this.file_path = file_path;
        this.class_path = class_path;
        this.uuid = uuid.toString();
        this.plugin_name = name;
        this.version =version;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getClass_path() {
        return class_path;
    }

    public void setClass_path(String class_path) {
        this.class_path = class_path;
    }

    public String getUUID_s() {
        return uuid;
    }

    public UUID getUUID() {
        return UUID.fromString(uuid);
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid.toString();
    }

    public String getPlugin_name() {
        return plugin_name;
    }

    public void setPlugin_name(String plugin_name) {
        this.plugin_name = plugin_name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PluginElement pe) {
            return pe.uuid.equals(uuid);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.class_path);
        hash = 47 * hash + Objects.hashCode(this.uuid);
        return hash;
    }

}
