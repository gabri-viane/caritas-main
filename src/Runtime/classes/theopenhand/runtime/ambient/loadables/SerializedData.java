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
package theopenhand.runtime.ambient.loadables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import theopenhand.runtime.data.DataElement;

/**
 *
 * @author gabri
 */
public class SerializedData {

    private File f;
    private String name;
    private final boolean proceed;
    private DataElement de;

    public SerializedData(File file, String name) {
        proceed = file != null && file.exists() && !file.isDirectory();
        if (proceed) {
            f = file;
            this.name = name;
        }
    }

    public SerializedData(String path, String name) {
        this(new File(path), name);
    }

    public void remove() {
        f.deleteOnExit();
    }

    public DataElement load() {
        if (de != null) {
            return de;
        }
        if (proceed) {
            try {
                try ( FileInputStream fis = new FileInputStream(f);  ObjectInputStream ois = new ObjectInputStream(fis)) {
                    Object readObject = ois.readObject();
                    de = (DataElement) readObject;
                    return de;
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SerializedData.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(SerializedData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public File getF() {
        return f;
    }

}
