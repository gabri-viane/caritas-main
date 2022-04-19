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

import theopenhand.commons.events.programm.FutureRequest;
import theopenhand.runtime.block.KeyUnlock;
import theopenhand.runtime.templates.Settings;

/**
 *
 * @author gabri
 */
public class SubscribeData {

    private static FutureRequest<PluginEnvironmentHandler,Settings> accept;

    private SubscribeData() {

    }

    public static void setAccept(FutureRequest<PluginEnvironmentHandler,Settings> accept, KeyUnlock key) {
        if (key != null) {
            SubscribeData.accept = accept;
        }
    }

    public static PluginEnvironmentHandler requestEnv(Settings s) {
        return accept.requested(s);
    }

}
