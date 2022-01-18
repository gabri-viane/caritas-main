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
package theopenhand.installer.plugins.store; 

import java.util.UUID;

/**
 *
 * @author gabri
 */
public class PluginData {

    private UUID uuid;
    private String download_path;
    private String zip_path;
    private Integer version;
    private String inj_point;
    private String name;
    private String description;

    public PluginData() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = UUID.fromString(uuid);
    }

    public String getDownload_path() {
        return download_path;
    }

    public void setDownload_path(String download_path) {
        this.download_path = download_path;
    }

    public String getZip_path() {
        return zip_path;
    }

    public void setZip_path(String zip_path) {
        this.zip_path = zip_path;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setVersion(String version) {
        this.version = Integer.parseInt(version);
    }

    public String getInj_point() {
        return inj_point;
    }

    public void setInj_point(String inj_point) {
        this.inj_point = inj_point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
