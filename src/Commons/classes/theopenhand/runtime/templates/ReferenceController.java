/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.runtime.templates;

import theopenhand.commons.connection.runtime.interfaces.ResultHolder;

/**
 *
 * @author gabri
 * @param <R>
 */
public abstract class ReferenceController<R extends ResultHolder> {

    private R rs_instance;

    /**
     *
     * @return
     */
    public abstract String getID();

    public final void setRH(ResultHolder rs) {
        this.rs_instance = (R) rs;
    }

    public final R getRH() {
        return rs_instance;
    }

}
