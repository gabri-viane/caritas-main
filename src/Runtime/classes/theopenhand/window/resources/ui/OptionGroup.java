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
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author gabri
 */
public final class OptionGroup extends TitledPane {

    private UUID id;

    @FXML
    private VBox buttonContainer;

    private final ArrayList<PluginOptionButton> buttons;

    /**
     *
     */
    public OptionGroup() {
        buttons = new ArrayList<>();
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/resources/ui/OptionGroup.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(OptionGroup.class.getName()).log(Level.SEVERE, null, ex);
        }
        buttonContainer.setFillWidth(true);
    }


    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        super.setText(title);
    }

    /**
     *
     * @param id
     */
    public void setID(UUID id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public UUID getID() {
        return id;
    }

    /**
     *
     * @param b
     * @return
     */
    public int addButton(PluginOptionButton b) {
        buttons.add(b);
        this.buttonContainer.getChildren().add(b);
        VBox.setVgrow(b, Priority.NEVER);
        return buttons.size() - 1;
    }

    /**
     *
     * @param b
     */
    public void removeButton(PluginOptionButton b) {
        buttons.remove(b);
        this.buttonContainer.getChildren().remove(b);
        if(buttons.isEmpty()){
            //StaticReferences.getMainWindowReference().removeLateralTitledPane(this);
        }
    }

    /**
     *
     * @param index
     */
    public void removeButton(int index) {
        this.buttonContainer.getChildren().remove(buttons.remove(index));
        if(buttons.isEmpty()){
            //StaticReferences.getMainWindowReference().removeLateralTitledPane(this);
        }
    }

}
