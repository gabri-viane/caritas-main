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
package theopenhand.window.resources.handler;

import java.util.ArrayList;
import java.util.HashMap;
import theopenhand.statics.StaticReferences;
import theopenhand.runtime.SubscriptionHandler;
import theopenhand.runtime.templates.RuntimeReference;
import theopenhand.window.resources.ui.OptionGroup;
import theopenhand.window.resources.ui.PluginOptionButton;

/**
 *
 * @author gabri
 */
public class PluginBinder {

    private static final HashMap<String, OptionGroup> opts = new HashMap<>();
    private static final HashMap<RuntimeReference, HashMap<String, ArrayList<PluginOptionButton>>> subscribed = new HashMap<>();

    private PluginBinder() {
    }

    /**
     *
     */
    public static void loadAll() {
        SubscriptionHandler.getPluginReferences().forEach(rf -> {
            load(rf);
        });
    }

    /**
     *
     * @param rf
     */
    public static void load(RuntimeReference rf) {
        if (rf != null) {
            HashMap<String, ArrayList<PluginOptionButton>> hm = new HashMap<>();
            rf.getPluginReferenceControllers().forEach(rc -> {
                PluginOptionButton plbtn = new PluginOptionButton(rf, rc.getAssocButtonName());
                plbtn.addListener(rc.getOnAssocButtonClick());
                plbtn.addListener(() -> {
                    StaticReferences.getMainWindowReference().setCenterNode(rc.getNode());
                });
                register(rc.getGroupName(), plbtn);
                if (hm.containsKey(rc.getGroupName())) {
                    hm.get(rc.getGroupName()).add(plbtn);
                } else {
                    ArrayList<PluginOptionButton> arr = new ArrayList<>();
                    arr.add(plbtn);
                    hm.put(rc.getGroupName(), arr);
                }
            });
            subscribed.put(rf, hm);
        }
    }

    private static void register(String name, PluginOptionButton b) {
        OptionGroup ogctrl = opts.get(name);
        if (ogctrl == null) {
            ogctrl = new OptionGroup();
            ogctrl.setTitle(name);
            registerNew(name, ogctrl);
        }
        ogctrl.addButton(b);
    }

    /**
     *
     * @param name
     * @param ogcntrl
     */
    public static void registerNew(String name, OptionGroup ogcntrl) {
        StaticReferences.getMainWindowReference().addLateralTitledPane(ogcntrl);
        opts.put(name, ogcntrl);
    }

    /**
     *
     * @param to_remove
     */
    public static void unregister(RuntimeReference to_remove) {
        HashMap<String, ArrayList<PluginOptionButton>> get = subscribed.get(to_remove);
        if (get != null) {
            get.entrySet().stream().forEach((t) -> {
                OptionGroup get1 = opts.get(t.getKey());
                t.getValue().forEach(b -> get1.removeButton(b));
            });
        }
    }

    /**
     *
     * @param to_remove
     */
    public static void unload(RuntimeReference to_remove) {
        if (to_remove != null) {
            unregister(to_remove);
            subscribed.remove(to_remove);
        }
    }

}
