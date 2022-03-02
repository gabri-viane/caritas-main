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
package theopenhand.installer.online.update;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import theopenhand.installer.SetupInit;
import theopenhand.installer.data.Version;
import theopenhand.installer.online.WebsiteComplement;
import theopenhand.installer.utils.Installer;
import theopenhand.installer.utils.WebConnection;

/**
 *
 * @author gabri
 */
public final class ProgrammAutoupdate {

    private final WebsiteComplement wc;
    private long online_version;

    public ProgrammAutoupdate() {
        wc = new WebsiteComplement();
        refreshData();
    }

    public void refreshData() {
        online_version = wc.sendVersionRequest();
    }

    public Long getVersion() {
        return Version.serialVersionUID;
    }

    public boolean toUpdate() {
        return (Version.serialVersionUID < online_version);
    }

    public WebConnection.DownloadTask downloadUpdate() {
        return wc.sendDownloadReuest(online_version);
    }

    public File extract(WebConnection.DownloadTask dt) {
        File output = dt.getOutput();
        if (output.exists()) {
            try (ZipFile zf = new ZipFile(output)) {
                ZipEntry update = null;
                int size = zf.size();
                if (size == 2) { //Uno file versione l'altro l'aggiornamento
                    Enumeration<? extends ZipEntry> it = zf.entries();
                    while (it.hasMoreElements()) {
                        ZipEntry nextElement = it.nextElement();
                        if (!nextElement.getName().equals(".version")) {
                            update = nextElement;
                            break;
                        }
                    }
                    if (update != null) {
                        Path newPath = Installer.zipSlipProtect(update, SetupInit.getInstance().getDOWNLOAD_FOLDER().toPath());
                        Files.copy(zf.getInputStream(update), newPath, StandardCopyOption.REPLACE_EXISTING);
                        output = newPath.toFile();
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ProgrammAutoupdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return output;
    }

}
