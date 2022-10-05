/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.commons.handlers;

import javafx.scene.Node;
import theopenhand.window.hand.MainActivityCTRL;

/**
 *
 * @author gabri
 */
public class MainWindowEventsHandler {

    private final MainActivityCTRL wndw_controller;

    /**
     *
     * @param wndw
     */
    public MainWindowEventsHandler(MainActivityCTRL wndw) {
        wndw_controller = wndw;
    }

    /**
     *
     * @param n
     */
    public void setCenterNode(Node n) {
        wndw_controller.getMainContainerBP().setCenter(n);
    }

}
