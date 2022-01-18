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
package theopenhand.window.resources.ui;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import theopenhand.commons.events.graphics.ClickHandler;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.runtime.templates.RuntimeReference;

/**
 * FXML Controller class
 *
 * @author gabri
 */
public class PluginOptionButton extends HBox {

    @FXML
    private Button btn;

    private final ClickHandler ch;
    private final RuntimeReference rr;
    private final String name;

    /**
     *
     * @param rr
     * @param name
     */
    public PluginOptionButton(RuntimeReference rr, String name) {
        ch = new ClickHandler();
        this.name = name;
        this.rr = rr;
        init();
    }

    /**
     *
     */
    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/resources/ui/PluginOptionButton.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(OptionGroup.class.getName()).log(Level.SEVERE, null, ex);
        }
        btn.setOnAction((a) -> {
            ch.click();
        });
        btn.setText(name);
    }

    /**
     *
     * @param cl
     */
    public void addListener(ClickListener cl) {
        ch.addListener(cl);
    }

    /**
     *
     * @return
     */
    public RuntimeReference getRuntimeReference() {
        return rr;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

}
