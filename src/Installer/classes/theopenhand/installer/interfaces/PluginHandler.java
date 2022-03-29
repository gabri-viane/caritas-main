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
package theopenhand.installer.interfaces;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author gabri
 */
public interface PluginHandler {

    public boolean installed(UUID uid);

    public void addPluginData(File f, String name, String ver, String path_to_class);

    public void addPluginData(File f, String name, String path_to_class, String ver, UUID uid);

    public void addPluginData(File f, String name, String path_to_class, String ver, UUID uid, ArrayList<UUID> dependencies);

    public void addPluginData(File f, String name, String path_to_class, String ver, UUID uid, ArrayList<UUID> dependencies, ArrayList<UUID> libraries);

    public void removePluginData(UUID uuid);

}
