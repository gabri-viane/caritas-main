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
package theopenhand.window.graphics.ribbon.elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import theopenhand.commons.events.programm.utils.ListEventListener;
import theopenhand.runtime.SubscriptionHandler;
import theopenhand.runtime.templates.RuntimeReference;

/**
 *
 * @author gabri
 */
public class RibbonBar extends AnchorPane {

    @FXML
    private TabPane mainTP;

    HashMap<String, RibbonTab> tabs = new HashMap<>();

    public RibbonBar() {
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/graphics/ribbon/elements/RibbonBar.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(RibbonBar.class.getName()).log(Level.SEVERE, null, ex);
        }

        SubscriptionHandler.addListener(new ListEventListener<RuntimeReference>() {
            @Override
            public void onElementAdded(RuntimeReference element) {
            }

            @Override
            public void onElementRemoved(RuntimeReference element) {
                removeReference(element);
            }
        });
    }

    public RibbonTab addTab(String title) {
        if (tabs.containsKey(title)) {
            return tabs.get(title);
        } else {
            RibbonTab rt = new RibbonTab(title);
            tabs.put(title, rt);
            mainTP.getTabs().add(rt);
            return rt;
        }
    }

    public void addTab(RibbonTab rt) {
        if (rt != null) {
            tabs.put(rt.getText(), rt);
            mainTP.getTabs().add(rt);
        }
    }

    public void removeReference(RuntimeReference element) {
        if (element != null) {
            tabs.forEach((k, v) -> {
                v.removeGroups(element);
            });
        }
    }

    public void clear() {
        tabs.clear();
    }

}
