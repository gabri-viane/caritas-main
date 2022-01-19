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
 * Associa ad ogni plugin caricato il proprio {@link PluginOptionButton} e lo
 * mette nel gruppo laterale corrispondente, e nel caso non esistesse, crea
 * anche il gruppo.
 *
 * @author gabri
 */
public class PluginBinder {

    private static final HashMap<String, OptionGroup> opts = new HashMap<>();
    private static final HashMap<RuntimeReference, HashMap<String, ArrayList<PluginOptionButton>>> subscribed = new HashMap<>();

    private PluginBinder() {
    }

    /**
     * Carica tutti i plugin iscritti al {@link SubscriptionHandler} e ne crea i
     * relativi pulsanti.
     */
    public static void loadAll() {
        SubscriptionHandler.getPluginReferences().forEach(rf -> {
            load(rf);
        });
    }

    /**
     * Carica una singola {@link RuntimeReference} e ne crea i pulsanti di
     * accesso se non è ancora stata caricta.
     *
     * @param rf La {@link RuntimeReference} da caricare.
     */
    public static void load(RuntimeReference rf) {
        if (rf != null && !subscribed.containsKey(rf)) {
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
     * Registra un nuovo gruppo per i pulsanti di accesso se non ne esiste già
     * uno con lo stesso nome.
     *
     * @param name Nome del nuovo gruppo.
     * @param ogcntrl Gruppo da aggiungere.
     */
    public static void registerNew(String name, OptionGroup ogcntrl) {
        if (!opts.containsKey(name)) {
            StaticReferences.getMainWindowReference().addLateralTitledPane(ogcntrl);
            opts.put(name, ogcntrl);
        }
    }

    /**
     * Rimuove tutti i pulsanti di una {@link RuntimeReference} dai gruppi in
     * cui è stata aggiunta.
     *
     * @param to_remove valore da cercare e rimuovere.
     */
    private static void unregister(RuntimeReference to_remove) {
        HashMap<String, ArrayList<PluginOptionButton>> get = subscribed.get(to_remove);
        if (get != null) {
            get.entrySet().stream().forEach((t) -> {
                OptionGroup get1 = opts.get(t.getKey());
                t.getValue().forEach(b -> get1.removeButton(b));
            });
        }
    }

    /**
     * Rimuove una {@link RuntimeReference} da tutti i pannelli e dalla
     * sottoscrizione di questo binder.
     *
     * @param to_remove {@link RuntimeReference} per cui cercare i pulsanti di
     * accesso da rimuovere.
     */
    public static void unload(RuntimeReference to_remove) {
        if (to_remove != null) {
            unregister(to_remove);
            subscribed.remove(to_remove);
        }
    }

}
