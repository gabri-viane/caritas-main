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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import theopenhand.runtime.loader.folder.PluginFolderHandler;
import ttt.utils.engines.enums.FieldType;
import ttt.utils.engines.enums.MethodType;
import ttt.utils.engines.interfaces.EngineField;
import ttt.utils.engines.interfaces.EngineMethod;
import ttt.utils.xml.document.XMLElement;
import ttt.utils.xml.engine.annotations.Element;
import ttt.utils.xml.engine.annotations.Tag;

/**
 * Viene caricato all'avvio del programma e esiste solo quando il plugin è già
 * installato.
 * <p>
 * Contiene i dati relativi a:
 * <ul>
 * <li>Percorso al file .jar</li>
 * <li>Percorso alla classe di caricamento</li>
 * <li>Il nome del plugin</li>
 * <li>L'UUID associato al plugin</li>
 * <li>Lista di UUID di plugins da cui dipende</li>
 * </ul>
 *
 * Viene usato solo al caricamento e alla scrittura del file di
 * {@link SetupInit#PLUGINS_XML} , mentre viene aggiornato ogni volta che viene
 * installato un plugin.
 * <br>
 * Un plugin appena installato avrà il relativo {@link PluginLoaderElement}
 * registrato, solo dopo aver chiamato {@link PluginFolderHandler#addPluginData(java.io.File, java.lang.String, java.lang.String, java.lang.String)
 * } o simile.
 *
 * @author gabri
 */
@Element(Name = "plugin")
public class PluginLoaderElement extends XMLElement {

    private final LinkedList<UUID> deps;
    private final LinkedList<UUID> libs;

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

    private UUID uuid_fl;

    public PluginLoaderElement() {
        super("plugin");
        deps = new LinkedList<>();
        libs = new LinkedList<>();
    }

    public PluginLoaderElement(String file_path, String name, String class_path, String version, String uuid) {
        super("plugin");
        this.file_path = file_path;
        this.class_path = class_path;
        this.uuid = uuid;
        this.plugin_name = name;
        this.version = version;
        deps = new LinkedList<>();
        libs = new LinkedList<>();
    }

    public PluginLoaderElement(String file_path, String name, String class_path, String version, UUID uuid) {
        super("plugin");
        this.file_path = file_path;
        this.class_path = class_path;
        this.uuid = uuid.toString();
        this.plugin_name = name;
        this.version = version;
        deps = new LinkedList<>();
        libs = new LinkedList<>();
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
        return uuid_fl;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid.toString();
        this.uuid_fl = uuid;
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

    @EngineMethod(MethodType = MethodType.CALC)
    public void generateDependencies() {
        uuid_fl = UUID.fromString(uuid);
        getElements().stream().filter((t) -> {
            return t.getName().equals("depends");
        }).forEach(el -> {
            deps.add(UUID.fromString(el.getValue()));
        });
        getElements().stream().filter((t) -> {
            return t.getName().equals("library");
        }).forEach(el -> {
            libs.add(UUID.fromString(el.getValue()));
        });
    }

    public List<UUID> getDependencies() {
        return Collections.unmodifiableList(deps);
    }

    public void addDependencies(UUID el) {
        DependsElement de = new DependsElement();
        de.setValue(el.toString());
        addSubElement(de);
        deps.add(el);
    }

    public void addDependencies(List<UUID> els) {
        els.forEach(el -> {
            DependsElement de = new DependsElement();
            de.setValue(el.toString());
            addSubElement(de);
            deps.add(el);
        });
    }

    public List<UUID> getLibraries() {
        return Collections.unmodifiableList(libs);
    }

    public void addLibraries(UUID el) {
        LibraryElement le = new LibraryElement();
        le.setValue(el.toString());
        addSubElement(le);
        libs.add(el);
    }

    public void addLibraries(List<UUID> els) {
        els.forEach(el -> {
            LibraryElement le = new LibraryElement();
            le.setValue(el.toString());
            addSubElement(le);
            libs.add(el);
        });
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PluginLoaderElement pe) {
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
