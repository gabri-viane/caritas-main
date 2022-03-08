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
package theopenhand.window.graphics.ribbon;

import java.util.HashMap;
import java.util.LinkedList;
import theopenhand.runtime.block.KeyUnlock;
import theopenhand.runtime.templates.RuntimeReference;
import theopenhand.window.graphics.ribbon.elements.RibbonBar;
import theopenhand.window.graphics.ribbon.elements.RibbonGroup;
import theopenhand.window.graphics.ribbon.elements.RibbonTab;

/**
 *
 * @author gabri
 */
public class RibbonFactory {

    private static final RibbonFactory instance = new RibbonFactory();
    private final LinkedList<RibbonTab> tabs_ordered = new LinkedList<>();
    private final HashMap<String, RibbonTab> tabs = new HashMap<>();

    protected static RibbonFactory getInstance() {
        return instance;
    }

    public static RibbonTab retriveTab(String title) {
        if (getInstance().tabs.containsKey(title)) {
            return getInstance().tabs.get(title);
        }
        RibbonTab rt = RibbonTab.generate(KeyUnlock.KEY, title);
        getInstance().tabs.put(title, rt);
        getInstance().tabs_ordered.add(rt);
        return rt;
    }

    public static RibbonGroup createGroup(RuntimeReference rr, RibbonTab rt, String title) {
        if (rr != null && rt != null && title != null && !title.isBlank()) {
            RibbonGroup rg = rt.addGroup(rr, title);
            return rg;
        }
        return null;
    }

    public static RibbonGroup createGroup(RuntimeReference rr, String tab_title, String title) {
        return createGroup(rr, retriveTab(tab_title), title);
    }

    public static void load(RibbonBar rb) {
        rb.clear();
        RibbonFactory.getInstance().tabs_ordered.stream().forEachOrdered(t -> {
            rb.addTab(t);
        });
    }
}
