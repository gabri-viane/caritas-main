/*
 * Copyright 2022 gabri.
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
package theopenhand.window.graphics.dialogs;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import theopenhand.commons.ReferenceQuery;
import theopenhand.commons.SharedReferenceQuery;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.events.programm.FutureCallable;
import theopenhand.commons.interfaces.graphics.Refreshable;
import theopenhand.runtime.Utils;

/**
 *
 * @author gabri
 */
public class ActionCreator {

    private ActionCreator() {

    }

    public static <T extends BindableResult, Q extends ResultHolder<T>> FutureCallable<Void> generateOrderAction(ReferenceQuery<T, Q> rq, Refreshable rr) {
        FutureCallable<Void> fc = (o) -> {
            SharedReferenceQuery.execute(rq, Utils.newInstance(rq.getBinded_class()), SharedReferenceQuery.EXECUTION_REQUEST.CUSTOM_QUERY, (os) -> {
                rr.onRefresh(false);
                return null;
            });
            return null;
        };
        return fc;
    }
    
    public static <T extends BindableResult, Q extends ResultHolder<T>> FutureCallable<Void> generateOrderAction(ReferenceQuery<T, Q> rq, Refreshable rr,T instance) {
        FutureCallable<Void> fc = (o) -> {
            SharedReferenceQuery.execute(rq, instance, SharedReferenceQuery.EXECUTION_REQUEST.CUSTOM_QUERY, (os) -> {
                rr.onRefresh(false);
                return null;
            });
            return null;
        };
        return fc;
    }

    public static void setHyperlinkAction(FutureCallable<?> fc, Hyperlink hl) {
        hl.setOnAction(a -> {
            fc.execute();
            hl.setVisited(false);
        });
    }

    public static void setButtonAction(FutureCallable<?> fc, Button btn) {
        btn.setOnAction(a -> fc.execute());
    }

    public static void setLabelkAction(FutureCallable<?> fc, Label btn) {
        btn.setOnMouseClicked(a -> fc.execute());
    }
}
