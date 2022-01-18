/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.window.hand;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author gabri
 */
public class MainActivityCTRL implements Initializable {

    @FXML
    private MenuItem aboutMenuBtn;

    @FXML
    private Button collapseLateralMenu;

    @FXML
    private MenuItem connDBMenuBtn;

    @FXML
    private MenuItem connInternetMenuBtn;

    @FXML
    private MenuItem connSiteMenuBtn;

    @FXML
    private MenuItem createBackupMenuBtn;

    @FXML
    private MenuItem customQueryMenuBtn;

    @FXML
    private MenuItem exportCSVMenuBtn;

    @FXML
    private MenuItem exportExcelMenuBtn;

    @FXML
    private MenuItem forceFlushMenuBtn;

    @FXML
    private MenuItem forceRollbackMEnuBtn;

    @FXML
    private VBox lateralMenuVB;

    @FXML
    private BorderPane mainContainerBP;

    @FXML
    private StackPane mainContainerSP;

    @FXML
    private MenuBar mainMenuBar;

    @FXML
    private Accordion panesOptionContainer;

    @FXML
    private Label pluginCounterLB;

    @FXML
    private MenuItem pluginSettingsBtn;

    @FXML
    private MenuItem pluginStoreBtn;

    @FXML
    private MenuItem quitMenuBtn;

    @FXML
    private HBox restartHB;

    @FXML
    private Hyperlink restartHL;

    @FXML
    private Label serverLB;

    @FXML
    private MenuItem showLogsMenuBtn;

    @FXML
    private MenuItem updatePrgMenuBtn;

    @FXML
    private Label userLB;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initListeners();
        initState();
    }

    private void initState() {
        mainContainerBP.setTop(null);
    }

    private void initListeners() {
        collapseLateralMenu.setOnMouseClicked((t) -> {
            collapseLateralHandler();
        });
    }

    private void collapseLateralHandler() {
        boolean visible = lateralMenuVB.isVisible();
        double size = visible ? 0 : 300;
        String text = visible ? ">" : "<";
        collapseLateralMenu.setText(text);
        lateralMenuVB.setVisible(!visible);
        lateralMenuVB.setPrefWidth(size);
        lateralMenuVB.setMaxWidth(size);
        lateralMenuVB.setMinWidth(size);
    }

    /**
     *
     * @return
     */
    public StackPane getMainContainerSP() {
        return mainContainerSP;
    }

    /**
     *
     * @return
     */
    public BorderPane getMainContainerBP() {
        return mainContainerBP;
    }

    /**
     *
     * @return
     */
    public VBox getLateralMenuVB() {
        return lateralMenuVB;
    }

    /**
     *
     * @return
     */
    public Accordion getPanesOptionContainer() {
        return panesOptionContainer;
    }

    /**
     *
     * @return
     */
    public Button getCollapseLateralMenu() {
        return collapseLateralMenu;
    }

    /**
     *
     * @return
     */
    public MenuBar getMainMenuBar() {
        return mainMenuBar;
    }

    /**
     *
     * @return
     */
    public MenuItem getQuitMenuBtn() {
        return quitMenuBtn;
    }

    /**
     *
     * @return
     */
    public MenuItem getCreateBackupMenuBtn() {
        return createBackupMenuBtn;
    }

    /**
     *
     * @return
     */
    public MenuItem getForceFlushMenuBtn() {
        return forceFlushMenuBtn;
    }

    /**
     *
     * @return
     */
    public MenuItem getForceRollbackMEnuBtn() {
        return forceRollbackMEnuBtn;
    }

    /**
     *
     * @return
     */
    public MenuItem getCustomQueryMenuBtn() {
        return customQueryMenuBtn;
    }

    /**
     *
     * @return
     */
    public MenuItem getExportExcelMenuBtn() {
        return exportExcelMenuBtn;
    }

    /**
     *
     * @return
     */
    public MenuItem getExportCSVMenuBtn() {
        return exportCSVMenuBtn;
    }

    /**
     *
     * @return
     */
    public MenuItem getConnInternetMenuBtn() {
        return connInternetMenuBtn;
    }

    /**
     *
     * @return
     */
    public MenuItem getConnDBMenuBtn() {
        return connDBMenuBtn;
    }

    /**
     *
     * @return
     */
    public MenuItem getConnSiteMenuBtn() {
        return connSiteMenuBtn;
    }

    /**
     *
     * @return
     */
    public MenuItem getUpdatePrgMenuBtn() {
        return updatePrgMenuBtn;
    }

    /**
     *
     * @return
     */
    public MenuItem getAboutMenuBtn() {
        return aboutMenuBtn;
    }

    /**
     *
     * @return
     */
    public MenuItem getShowLogsMenuBtn() {
        return showLogsMenuBtn;
    }

    /**
     *
     * @return
     */
    public MenuItem getPluginSettingsBtn() {
        return pluginSettingsBtn;
    }

    /**
     *
     * @return
     */
    public MenuItem getPluginStoreBtn() {
        return pluginStoreBtn;
    }

    public Label getPluginCounterLB() {
        return pluginCounterLB;
    }

    public HBox getRestartHB() {
        return restartHB;
    }

    public Hyperlink getRestartHL() {
        return restartHL;
    }

    public Label getServerLB() {
        return serverLB;
    }

    public Label getUserLB() {
        return userLB;
    }

}
