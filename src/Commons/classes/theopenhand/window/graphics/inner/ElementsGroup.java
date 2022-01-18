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
package theopenhand.window.graphics.inner;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author gabri
 */
public class ElementsGroup extends AnchorPane {

    @FXML
    private HBox groupControlHB;

    @FXML
    private Separator lineLeftSP;

    @FXML
    private VBox mainContainerVB;

    @FXML
    private HBox titleBandHB;

    @FXML
    private VBox titleGroupVB;

    @FXML
    private Label titleLBL;

    @FXML
    private Separator lineTitleUnderlineSP;

    @FXML
    private BorderPane contentBP;

    private Node content;

    /**
     *
     */
    public ElementsGroup() {
        content = null;
        init();
    }

    /**
     *
     * @param content
     */
    public ElementsGroup(Node content) {
        this.content = content;
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/graphics/inner/ElementsGroup.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ElementsGroup.class.getName()).log(Level.SEVERE, null, ex);
        }
        contentBP.setCenter(content);
    }

    /**
     *
     * @param title
     */
    public void setTile(String title) {
        titleLBL.setText(title);
    }

    /**
     *
     * @return
     */
    public VBox getTitleBox() {
        return titleGroupVB;
    }

    /**
     *
     */
    public void hideLeftLine() {
        groupControlHB.getChildren().remove(lineLeftSP);
    }

    /**
     *
     */
    public void hideTitleLine() {
        titleGroupVB.getChildren().remove(lineTitleUnderlineSP);
    }

    /**
     *
     * @param n
     */
    public void addContentToTile(Node n) {
        titleBandHB.getChildren().add(n);
    }

    /**
     *
     * @param n
     */
    public void removeContentToTile(Node n) {
        titleBandHB.getChildren().remove(n);
    }

    /**
     *
     * @return
     */
    public BorderPane getContentView() {
        return contentBP;
    }

    /**
     *
     * @param n
     */
    public void setContent(Node n) {
        content = n;
        contentBP.setCenter(content);
    }

    /**
     *
     * @return
     */
    public Node getContent() {
        return content;
    }

    /**
     *
     * @param n
     */
    public void appendContent(Node n) {
        mainContainerVB.getChildren().add(n);
    }

    /**
     *
     * @param n
     */
    public void removeContent(Node n) {
        mainContainerVB.getChildren().remove(n);
    }

}
