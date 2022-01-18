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
package theopenhand.window.graphics.dialogs;

import java.util.ArrayList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import theopenhand.commons.ReferenceQuery;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.events.graphics.ClickListener;
import theopenhand.commons.interfaces.graphics.DialogComponent;
import theopenhand.statics.privates.StaticReferencesPvt;
import theopenhand.window.graphics.commons.PickerDialogCNTRL;

/**
 *
 * @author gabri
 */
public class DialogCreator {

    /**
     *
     * @param p
     * @param width
     * @param height
     * @param title
     * @return
     */
    public static Stage showDialog(Parent p, double width, double height, String title) {
        if (p != null && width > 10 && height > 10) {
            final Stage dialog = new Stage();
            dialog.setTitle(title);
            dialog.getIcons().add(new Image(DialogCreator.class.getResourceAsStream("/theopenhand/window/resources/TheOpenHand-Icona.jpg")));
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(StaticReferencesPvt.primaryStage);
            Scene dialogScene = new Scene(p, width, height);
            dialog.setScene(dialogScene);
            dialog.show();
            return dialog;
        }
        return null;
    }

    /**
     *
     * @param dc
     * @param on_accept
     * @param on_close
     * @return
     */
    public static Stage showDialog(DialogComponent dc, ClickListener on_accept, ClickListener on_close) {
        Stage s = DialogCreator.showDialog(dc.getParentNode(), dc.getDialogWidth(), dc.getDialogWidth(), dc.getTitle());
        ClickListener cl = () -> {
            s.getScene().setRoot(new Region());
            s.close();
        };
        dc.onExitPressed(() -> {
            if (on_close != null) {
                on_close.onClick();
            }
            cl.onClick();
        });
        dc.onAcceptPressed(() -> {
            if (on_accept != null) {
                on_accept.onClick();
            }
            cl.onClick();
        });
        return s;
    }

    /**
     *
     * @param at
     * @param title
     * @param text
     * @param dialog
     * @return
     */
    public static Alert showAlert(Alert.AlertType at, String title, String text, DialogPane dialog) {
        ArrayList<ButtonType> als = new ArrayList<>();
        switch (at) {
            case CONFIRMATION -> {
                als.add(ButtonType.YES);
                als.add(ButtonType.NO);
            }
            case INFORMATION -> als.add(ButtonType.OK);
            case ERROR -> als.add(ButtonType.CLOSE);
            case NONE -> {
                als.add(ButtonType.PREVIOUS);
                als.add(ButtonType.NEXT);
            }
            case WARNING -> {
                als.add(ButtonType.CANCEL);
                als.add(ButtonType.OK);
            }
        }
        ButtonType[] bts = new ButtonType[als.size()];
        for (int i = 0; i < als.size(); i++) {
            bts[i] = als.get(i);
        }
        Alert a = new Alert(at, title, bts);
        a.setHeaderText(title);
        a.setTitle(title);
        if (text != null) {
            a.setContentText(text);
        }
        if (dialog != null) {
            a.setDialogPane(dialog);
        }
        a.setResizable(true);
        return a;
    }

    /**
     *
     * @param <T>
     * @param <X>
     * @param rq
     * @param title
     * @return
     */
    public static <T extends BindableResult, X extends ResultHolder<T>> PickerDialogCNTRL<T, X> createPicker(ReferenceQuery<T, X> rq, String title) {
        PickerDialogCNTRL<T, X> pck = new PickerDialogCNTRL(rq.getRuntime_reference(), rq.getBinded_class(), rq.getResult_holder(), rq.getQuery_id(), title, null, null);
        return pck;
    }

    /**
     *
     * @param <T>
     * @param <X>
     * @param rq
     * @param title
     * @param on_add
     * @return
     */
    public static <T extends BindableResult, X extends ResultHolder<T>> PickerDialogCNTRL<T, X> createPicker(ReferenceQuery<T, X> rq, String title, DialogComponent on_add) {
        PickerDialogCNTRL<T, X> pck = new PickerDialogCNTRL(rq.getRuntime_reference(), rq.getBinded_class(), rq.getResult_holder(), rq.getQuery_id(), title, on_add, null);
        return pck;
    }

    /**
     *
     * @param <T>
     * @param <X>
     * @param rq
     * @param title
     * @param on_add
     * @param on_order
     * @return
     */
    public static <T extends BindableResult, X extends ResultHolder<T>> PickerDialogCNTRL<T, X> createPicker(ReferenceQuery<T, X> rq, String title, DialogComponent on_add, DialogComponent on_order) {
        PickerDialogCNTRL<T, X> pck = new PickerDialogCNTRL(rq.getRuntime_reference(), rq.getBinded_class(), rq.getResult_holder(), rq.getQuery_id(), title, on_add, on_order);
        return pck;
    }
}