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
package theopenhand.installer.online.store;

import java.util.UUID;

/**
 *
 * @author gabri
 */
public class LibraryDownloadData {
    
    private UUID uuid;
    private String download_path;
    private String zip_path;
    private Integer version;
    private String link_zip_name;
    private String install_zip_name;
    
    public LibraryDownloadData() {
    }
    
    public UUID getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = UUID.fromString(uuid);
    }
    
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
    
    public String getLink_zip_name() {
        return link_zip_name;
    }
    
    public void setLink_zip_name(String link_zip_name) {
        this.link_zip_name = link_zip_name;
    }
    
    public String getInstall_zip_name() {
        return install_zip_name;
    }
    
    public void setInstall_zip_name(String install_zip_name) {
        this.install_zip_name = install_zip_name;
    }
    
}
