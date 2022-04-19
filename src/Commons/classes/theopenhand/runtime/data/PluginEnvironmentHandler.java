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
package theopenhand.runtime.data;

import java.io.File;
import java.io.Serializable;
import java.util.zip.ZipFile;
import theopenhand.runtime.data.components.IDataElement;

/**
 *
 * @author gabri
 */
public interface PluginEnvironmentHandler {

    public File addFile(String name, File source);
    
    public void addData(IDataElement data);

    public IDataElement addData(String name, ZipFile source);

    public IDataElement addData(String name, Serializable data);

    public IDataElement getData(String name);

    public File getFile(String name);

    public void removeData(String name);

    public void loadDataElements();

}
