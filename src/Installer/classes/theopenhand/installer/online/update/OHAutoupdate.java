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
import theopenhand.installer.online.WebsiteComplement;
import theopenhand.installer.utils.WebConnection;
import theopenhand.runtime.data.FileUtils;

/**
 *
 * @author gabri
 */
public class OHAutoupdate {

    private final WebsiteComplement wc;

    public OHAutoupdate() {
        this.wc = new WebsiteComplement();
    }

    public boolean toUpdate() {
        String path = SetupInit.getInstance().getLIBS_FOLDER().getAbsolutePath() + File.separatorChar + "NCDB OuterHands.exe";
        File f = new File(path);
        return (!f.exists() || !f.isFile());
    }

    public WebConnection.DownloadTask downloadUpdate() {
        return wc.sendDownloadRequestOuterHand();
    }

    public File extract(WebConnection.DownloadTask dt) {
        File output = dt.getOutput();
        if (output.exists()) {
            try ( ZipFile zf = new ZipFile(output)) {
                ZipEntry update = null;
                int size = zf.size();
                if (size == 2) { //Uno file versione l'altro l'aggiornamento
                    Enumeration<? extends ZipEntry> it = zf.entries();
                    while (it.hasMoreElements()) {
                        ZipEntry nextElement = it.nextElement();
                        if (!nextElement.getName().equals(".properties")) {
                            update = nextElement;
                            break;
                        }
                    }
                    if (update != null) {
                        Path newPath = FileUtils.zipSlipProtect(update, SetupInit.getInstance().getLIBS_FOLDER().toPath());
                        Files.copy(zf.getInputStream(update), newPath, StandardCopyOption.REPLACE_EXISTING);
                        output = newPath.toFile();
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(OHAutoupdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return output;
    }

}
