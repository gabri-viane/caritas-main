/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package theopenhand.window.resources.ui.settings.plugins;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.runtime.annotations.SettingProperty;

/**
 *
 * @author gabri
 */
public class PluginSettingField extends HBox {
    
    @FXML
    private HBox root;
    
    @FXML
    private Label title;
    
    @FXML
    private HBox container;
    
    @FXML
    private Button apply;
    
    private final Node n;
    private final SettingProperty sp;
    private final ClickListener listener;
    
    /**
     *
     * @param cl
     * @param n
     * @param sp
     */
    public PluginSettingField(ClickListener cl, Node n, SettingProperty sp) {
        listener = cl;
        this.n = n;
        this.sp = sp;
        init();
    }
    
    /**
     *
     */
    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/window/resources/ui/settings/plugins/PluginSettingField.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(PluginSettingField.class.getName()).log(Level.SEVERE, null, ex);
        }
        container.getChildren().add(n);
        title.setText(sp.name());
        apply.setOnAction((a) -> {
            listener.onClick();
        });
    }
    
    /**
     *
     * @return
     */
    public Node getNode() {
        return n;
    }
    
    /**
     *
     * @param b
     */
    public void setButtonVisible(boolean b){
        apply.setVisible(b);
    }
    
}
