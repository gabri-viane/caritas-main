/*
 * Copyright 2021 gabri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package theopenhand.commons.interfaces.graphics;

import javafx.scene.Parent;
import theopenhand.commons.events.graphics.ClickListener;

/**
 *
 * @author gabri
 */
public interface DialogComponent {

    /**
     *
     * @return
     */
    public double getDialogWidth();

    /**
     *
     * @return
     */
    public double getDialogHeight();

    /**
     *
     * @return
     */
    public String getTitle();

    /**
     *
     * @return
     */
    public Parent getParentNode();

    /**
     *
     * @param cl
     */
    public void onExitPressed(ClickListener cl);

    /**
     *
     * @param cl
     */
    public void onAcceptPressed(ClickListener cl);
}
