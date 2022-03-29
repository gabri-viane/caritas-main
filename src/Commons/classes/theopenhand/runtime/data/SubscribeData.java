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
package theopenhand.runtime.data;

import java.io.File;
import java.io.Serializable;
import java.util.zip.ZipFile;
import theopenhand.commons.events.programm.DataRequest;
import theopenhand.runtime.templates.Settings;

/**
 *
 * @author gabri
 */
public class SubscribeData {

    public static DataRequest<File, String, File> file_handler;
    public static DataRequest<DataElement, String, Serializable> data_handler;
    public static DataRequest<Void, ZipFile, String> extraction_handler;

    private SubscribeData() {

    }

    public static void setFCFiles(DataRequest<File, String, File> fc) {
        SubscribeData.file_handler = fc;
    }

    public static void setFCData(DataRequest<DataElement, String, Serializable> fc) {
        SubscribeData.data_handler = fc;
    }

    public static void setFCExtraction(DataRequest<Void, ZipFile, String> fc) {
        SubscribeData.extraction_handler = fc;
    }

    public static File generateFile(Settings st, String name, File f) {
        if (file_handler != null) {
            return file_handler.onSubscribe(st, name, f);
        }
        return null;
    }

    public static DataElement generateElement(Settings st, String name, Serializable s) {
        if (data_handler != null) {
            return data_handler.onSubscribe(st, name, s);
        }
        return null;
    }

    public static void extractFiles(Settings st, ZipFile zf, String name) {
        if (extraction_handler != null) {
            extraction_handler.onSubscribe(st, zf, name);
        }
    }

    public static File requestFile(Settings st, String name) {
        if (file_handler != null) {
            return file_handler.onRequest(st, name);
        }
        return null;
    }

    public static DataElement requestData(Settings st, String name) {
        if (data_handler != null) {
            return data_handler.onRequest(st, name);
        }
        return null;
    }

}
