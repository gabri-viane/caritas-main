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

module Commons {

    requires Utils;

    requires java.naming;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.base;

    exports theopenhand.statics;

    exports theopenhand.commons;
    exports theopenhand.runtime;

    exports theopenhand.commons.interfaces;
    exports theopenhand.commons.interfaces.graphics;
    exports theopenhand.commons.connection;
    exports theopenhand.commons.connection.runtime;
    exports theopenhand.commons.connection.runtime.annotations;
    exports theopenhand.commons.connection.runtime.interfaces;
    exports theopenhand.commons.connection.runtime.impls;
    exports theopenhand.commons.connection.runtime.custom;
    exports theopenhand.commons.events.graphics;
    exports theopenhand.commons.events.programm;
    exports theopenhand.commons.events.engines;
    exports theopenhand.commons.events.programm.utils;
    exports theopenhand.commons.handlers;

    exports theopenhand.runtime.annotations;
    exports theopenhand.runtime.templates;
    exports theopenhand.runtime.data;
    exports theopenhand.commons.programm.loader;
    exports theopenhand.commons.programm.loader.settings;

    exports theopenhand.window.graphics.inner;
    exports theopenhand.window.graphics.dialogs;
    exports theopenhand.window.graphics.commons;
    exports theopenhand.window.graphics.commons.ordable;

    exports theopenhand.window.objects;
    exports theopenhand.runtime.block to Program, Runtime;
    exports theopenhand.window.hand to Program;
    exports theopenhand.statics.privates to Program, Runtime;
    opens theopenhand.window.hand to javafx.fxml;
    opens theopenhand.window.graphics.inner to javafx.fxml;
    opens theopenhand.window.graphics.commons to javafx.fxml;
    opens theopenhand.window.graphics.commons.ordable to javafx.graphics, javafx.fxml;
    opens theopenhand.window.graphics.commons.ordable.components to javafx.graphics, javafx.fxml;
    //opens theopenhand to javafx.graphics, javafx.fxml;
}
