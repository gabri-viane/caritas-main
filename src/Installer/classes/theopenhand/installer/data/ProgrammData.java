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
package theopenhand.installer.data;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import theopenhand.installer.SetupInit;
import theopenhand.installer.data.xml.settings.ProgrammElement;
import theopenhand.statics.StaticReferences;
import ttt.utils.xml.document.XMLDocument;
import ttt.utils.xml.engine.XMLEngine;
import ttt.utils.xml.engine.interfaces.IXMLElement;
import ttt.utils.xml.io.XMLWriter;
import ttt.utils.xml.io.base.RootElement;

/**
 *
 * @author gabri
 */
public class ProgrammData {

    private static ProgrammData instance;
    private XMLDocument doc;
    private XMLWriter writer;

    private ProgrammData() {
        init();
    }

    private void init() {
        File f = SetupInit.getInstance().getGENERAL_SETTINGS();
        try {
            writer = new XMLWriter(f);
            doc = new XMLDocument(f);
            XMLEngine eng = new XMLEngine(f, RootElement.class, ProgrammElement.class);
            eng.morph(doc);
            initData();
            StaticReferences.subscribeOnExit((args) -> {
                writer.writeDocument(doc, true);
                return null;
            });
        } catch (IOException ex) {
            Logger.getLogger(ProgrammData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ProgrammData getInstance() {
        if (instance == null) {
            instance = new ProgrammData();
        }
        return instance;
    }

    private void initData() {
        IXMLElement el = doc.getRoot().getFirstElement("programm");
        if (el != null) {
            ProgrammElement pe = (ProgrammElement) el;
            //Ho la versione del programma
            if (!Objects.equals(pe.getVersion(), Version.serialVersionUID)) {
                //Versioni diverse: se questa è maggiore sostituisco
                 pe.setVersion(Version.serialVersionUID);
                 //(procedimento nel caso di programma aggiornato)
            }
        } else {
            //Installazione nuova
            ProgrammElement pe = new ProgrammElement();
            pe.setVersion(Version.serialVersionUID);
            doc.getRoot().addSubElement(pe);
        }
    }

}
