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
package theopenhand.programm.window.homepage;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author gabri
 */
public class HomepageScene extends AnchorPane {

    @FXML
    private ImageView iconGestor;
    
    private static HomepageScene instance;

    private HomepageScene() {
        init();
    }

    public static HomepageScene getInstance() {
        if (instance == null) {
            instance = new HomepageScene();
        }
        return instance;
    }
    
    private void init(){
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/programm/window/homepage/HomepageScene.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(HomepageScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        iconGestor.setImage(new Image(this.getClass().getResourceAsStream("/theopenhand/programm/resources/Home.png")));
    }

}
