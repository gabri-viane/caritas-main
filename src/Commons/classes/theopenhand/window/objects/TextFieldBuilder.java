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
package theopenhand.window.objects;

import java.util.function.UnaryOperator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

/**
 *
 * @author gabri
 */
public class TextFieldBuilder {

    private TextFieldBuilder() {

    }

    /**
     *
     * @return
     */
    public static TextField buildNumericField() {
        TextField tf = new TextField();
        transformNumericField(tf);
        return tf;
    }

    /**
     *
     * @param tf
     */
    public static void transformNumericField(TextField tf) {
        UnaryOperator<Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        tf.setTextFormatter(textFormatter);
    }
}
