/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.commons.events.graphics;

import java.util.ArrayList;

/**
 *
 * @author gabri
 */
public class ClickHandler {
    
    private final ArrayList<ClickListener> listeners = new ArrayList<>();

    /**
     *
     */
    public ClickHandler() {
    }
    
    /**
     *
     */
    public void click(){
        listeners.forEach(ClickListener::onClick);
    }
    
    /**
     *
     * @param cl
     */
    public void addListener(ClickListener cl){
        listeners.add(cl);
    }
    
    /**
     *
     * @param cl
     */
    public void removeListener(ClickListener cl){
        listeners.remove(cl);
    }
    
}
