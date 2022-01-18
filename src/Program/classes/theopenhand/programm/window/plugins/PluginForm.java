/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package theopenhand.programm.window.plugins;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import theopenhand.runtime.loader.Loader;
import theopenhand.runtime.loader.SettingsLoader;
import theopenhand.window.resources.ui.settings.plugins.PluginSettingPage;

/**
 *
 * @author gabri
 */
public class PluginForm extends AnchorPane {

    @FXML
    private TabPane tabbedpane;

    @FXML
    private ImageView iconGestor;

    public PluginForm(Loader l) {
        init(l);
    }

    private void init(Loader l) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/programm/window/plugins/PluginForm.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(PluginForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        iconGestor.setImage(new Image(this.getClass().getResourceAsStream("/theopenhand/programm/resources/plugin_symbol.png")));
        l.getUUIDS().forEach(u -> {
            SettingsLoader findSettings = l.findSettings(u);
            if (findSettings != null) {
                //findSettings.init();
                PluginSettingPage pspcntrl = new PluginSettingPage(findSettings);
                pspcntrl.setVersion(l.findPluginVersion(u));
                tabbedpane.getTabs().add(new Tab(l.findPluginName(u), pspcntrl));
            }
        });
    }
}
