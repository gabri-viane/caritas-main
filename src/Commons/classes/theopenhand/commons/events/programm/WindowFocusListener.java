/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.commons.events.programm;

import theopenhand.runtime.templates.ReferenceController;

/**
 *
 * @author gabri
 */
public interface WindowFocusListener {

    /**
     *
     * @param focused
     */
    public void onFocusChanged(ReferenceController focused);
}
