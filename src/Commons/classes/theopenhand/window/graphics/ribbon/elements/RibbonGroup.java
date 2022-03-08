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

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import theopenhand.runtime.templates.RuntimeReference;

/**
 *
 * @author gabri
 */
public class RibbonGroup extends VBox {

    private final HBox main;
    private final Label groupTitle;
    private final RuntimeReference reference;

    protected RibbonGroup(RuntimeReference rr, String title) {
        super();
        reference = rr;
        setPrefHeight(70);
        setPrefWidth(USE_COMPUTED_SIZE);
        setMaxHeight(100);
        setMaxWidth(Double.MAX_VALUE);
        setMinHeight(USE_COMPUTED_SIZE);
        setMinWidth(50);
        setSpacing(5);
        groupTitle = new Label(title);
        main = new HBox();
        main.setSpacing(5);
        VBox.setVgrow(main, Priority.ALWAYS);
        VBox.setVgrow(groupTitle, Priority.NEVER);
        getChildren().addAll(main, groupTitle);
    }

    public RibbonGroup addNode(Node n) {
        main.getChildren().add(n);
        return this;
    }

    public RuntimeReference getRuntimeReference() {
        return reference;
    }

}
