/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package theopenhand.commons.events.programm;

/**
 *
 * @author gabri
 * @param <T>
 */
public interface ValueAcceptListener<T> {

    /**
     *
     * @param value
     */
    public void onAccept(T value);
}
