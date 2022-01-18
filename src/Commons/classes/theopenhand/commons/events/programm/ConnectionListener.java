/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.commons.events.programm;

/**
 *
 * @author gabri
 */
public interface ConnectionListener {

    /**
     *
     */
    public void onConnectionRequest();

    /**
     *
     */
    public void onConnectionCompleted();

    /**
     *
     */
    public void onConnectionClosed();
}
