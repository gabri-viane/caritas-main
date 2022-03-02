/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.commons.handlers;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import theopenhand.commons.events.graphics.ClickHandler;
import theopenhand.commons.events.graphics.ComponentHandler;
import theopenhand.window.hand.MainActivityCTRL;

/**
 *
 * @author gabri
 */
public class MainWindowEventsHandler {

    private final MainActivityCTRL wndw_controller;

    /**
     *
     */
    public final ClickHandler LATERAL_MENU_BUTTON_HANDLER;

    /**
     *
     */
    public final ClickHandler MENU_PLUGIN_SETTINGS_HANDLER;

    /**
     *
     */
    public final ComponentHandler GENERAL_COMPONENT_HANDLER;

    /**
     *
     * @param wndw
     */
    public MainWindowEventsHandler(MainActivityCTRL wndw) {
        wndw_controller = wndw;
        LATERAL_MENU_BUTTON_HANDLER = new ClickHandler();
        MENU_PLUGIN_SETTINGS_HANDLER = new ClickHandler();
        GENERAL_COMPONENT_HANDLER = new ComponentHandler();
        initAll();
    }

    private void initAll() {
        LATERAL_MENU_BUTTON_HANDLER.addListener(() -> wndw_controller.getCollapseLateralMenu().fire());
        MENU_PLUGIN_SETTINGS_HANDLER.addListener(() -> wndw_controller.getPluginSettingsBtn().fire());
    }

    /**
     *
     * @param b
     */
    public void addLateralTitledPane(TitledPane b) {
        Accordion a = wndw_controller.getPanesOptionContainer();
        Platform.runLater(() -> {
            a.getPanes().add(b);
        });
        GENERAL_COMPONENT_HANDLER.addedComponent(a, b);
    }

    /**
     *
     * @param b
     */
    public void removeLateralTitledPane(TitledPane b) {
        Accordion a = wndw_controller.getPanesOptionContainer();
        a.getPanes().remove(b);
        GENERAL_COMPONENT_HANDLER.removedComponent(a, b);
    }

    /**
     *
     * @param n
     */
    public void setCenterNode(Node n) {
        wndw_controller.getMainContainerBP().setCenter(n);
    }

}
