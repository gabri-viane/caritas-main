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

module Installer {
    requires Commons;
    requires Utils;
    requires java.logging;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    exports theopenhand.installer;
    exports theopenhand.installer.data;
    exports theopenhand.installer.online.update;
    exports theopenhand.installer.online.store;
    exports theopenhand.installer.utils;
    exports theopenhand.installer.interfaces;
    opens theopenhand.installer.utils to javafx.graphics;
    opens theopenhand.installer.graphics to javafx.graphics, javafx.fxml;
    opens theopenhand.installer.data.xml.settings to Utils;
    opens theopenhand.installer.data.xml.connection to Utils;
}
