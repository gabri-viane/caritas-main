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
package theopenhand.programm.controls;

import theopenhand.runtime.loader.Loader;

/**
 *
 * @author gabri
 */
public class PluginController {

    private static final PluginController instance = new PluginController();

    private PluginController() {

    }

    public static PluginController getInstance() {
        return instance;
    }

    public void load() {
        Thread t = new Thread() {
            @Override
            public void run() {
                StaticExchange.LOADER = Loader.getInstance();
                StaticExchange.LOADER.activate();
                GUIControl.getInstance().rereshData();
            }
        };
        t.start();
    }
}
