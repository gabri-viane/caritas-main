/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.runtime.templates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import theopenhand.commons.events.programm.utils.ListEventHandler;
import theopenhand.commons.events.programm.utils.ListEventListener;

/**
 *
 * @author gabri
 */
public abstract class RuntimeReference {

    private final ListEventHandler<ReferenceController> list;
    private final ArrayList<ReferenceController> controllers;
    private final String reference_name;

    /**
     *
     * @param string
     */
    public RuntimeReference(String ref_name) {
        this.controllers = new ArrayList<>();
        this.list = new ListEventHandler<>(this.controllers);
        this.reference_name = ref_name;
    }

    /**
     *
     * @return
     */
    public final List<ReferenceController> getPluginReferenceControllers() {
        return Collections.unmodifiableList(controllers);
    }

    /**
     *
     * @param cl
     */
    public final void addListener(ListEventListener<ReferenceController> cl) {
        list.addListener(cl);
    }

    /**
     *
     * @param cl
     */
    public final void removeListener(ListEventListener<ReferenceController> cl) {
        list.removeListener(cl);
    }

    /**
     *
     * @param rt
     */
    public final void addReferenceController(ReferenceController rt) {
        list.elementAdded(rt);
    }

    /**
     *
     * @param rt
     */
    public final void removeReferenceController(ReferenceController rt) {
        list.elementRemoved(rt);
    }

    /**
     *
     * @return
     */
    public final String getName() {
        return reference_name;
    }
}
