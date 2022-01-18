/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.runtime.templates;

import javafx.scene.Parent;
import theopenhand.commons.events.graphics.ClickListener;

/**
 *
 * @author gabri
 */
public interface ReferenceController {

    /**
     *
     * @return
     */
    public String getID();

    /**
     *
     * @param <T>
     * @return
     */
    public <T extends Parent> T getNode();

    /**
     *
     * @return
     */
    public ClickListener getOnAssocButtonClick();

    /**
     *
     * @return
     */
    public String getAssocButtonName();

    /**
     *
     * @return
     */
    public String getGroupName();

}
