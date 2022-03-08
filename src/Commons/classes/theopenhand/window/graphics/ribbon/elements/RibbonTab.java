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

import java.util.HashMap;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import theopenhand.runtime.block.KeyUnlock;
import theopenhand.runtime.templates.RuntimeReference;

/**
 *
 * @author gabri
 */
public class RibbonTab extends Tab {

    private final HBox hb;
    private final HashMap<RuntimeReference, HashMap<String, RibbonGroup>> groups;

    public static RibbonTab generate(KeyUnlock ku, String title) {
        if (ku != null) {
            return new RibbonTab(title);
        }
        return null;
    }

    protected RibbonTab(String title) {
        super(title);
        groups = new HashMap<>();
        hb = new HBox();
        hb.setMaxWidth(Double.MAX_VALUE);
        hb.setMaxHeight(Double.MAX_VALUE);
        hb.setMinHeight(Region.USE_COMPUTED_SIZE);
        hb.setSpacing(5);
        hb.setPadding(new Insets(5, 5, 5, 5));
        this.setContent(hb);
    }

    public RibbonGroup addGroup(RuntimeReference rr, String title) {
        if (rr != null && title != null && !title.isBlank()) {
            HashMap<String, RibbonGroup> grps;
            if (groups.containsKey(rr)) {
                grps = groups.get(rr);
            } else {
                grps = new HashMap<>();
                groups.put(rr, grps);
            }
            if (grps.containsKey(title)) {
                return grps.get(title);
            } else {
                RibbonGroup rg = new RibbonGroup(rr, title);
                grps.put(title, rg);
                Separator s = new Separator(Orientation.VERTICAL);
                s.setPrefHeight(Region.USE_COMPUTED_SIZE);
                Platform.runLater(() -> {
                    hb.getChildren().addAll(rg, s);
                });
                return rg;
            }
        }
        return null;
    }

    public void removeGroups(RuntimeReference rr) {
        HashMap<String, RibbonGroup> remove = groups.remove(rr);
        if (remove != null) {
            remove.forEach((t, u) -> {
                removeGroup(rr, t);
            });
        }
    }

    public void removeGroup(RuntimeReference rr, String title) {
        if (groups.containsKey(rr)) {
            HashMap<String, RibbonGroup> get = groups.get(rr);
            RibbonGroup remove = get.remove(title);
            Platform.runLater(() -> {
                int indexOf = hb.getChildren().indexOf(remove);
                hb.getChildren().remove(indexOf + 1);
                hb.getChildren().remove(remove);
            });
        }
    }

    public boolean isEmpty() {
        return groups.isEmpty();
    }

}
