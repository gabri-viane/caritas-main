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
package theopenhand.runtime.loader.folder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import theopenhand.installer.SetupInit;
import theopenhand.installer.online.store.PluginDownloadData;

/**
 *
 * @author gabri
 */
public class Installer {

    private final File to_install;

    private Installer(File f) {
        this.to_install = f;
    }

    /**
     *
     * @param f
     * @return
     */
    public static Installer generate(File f) {
        if (f != null && f.exists() && f.isFile() && f.canRead()) {
            return new Installer(f);
        }
        return null;
    }

    /**
     * Installa un plugin:
     * <ol>
     * <li>Apre il file zip (pasato all'istanza tramite {@link #generate(java.io.File)
     * })</li>
     * <li>Controlla che contenga il file .jar</li>
     * <li>Usa i dati scaricati dal sito tramite {@link PluginDownloadData}</li>
     * <li>Tramite {@link PluginFolderHandler#addPluginData(java.io.File, java.lang.String, java.lang.String, java.lang.String, java.util.UUID, java.util.ArrayList)
     * } registra il plugin per il prossimo caricamento.</li>
     * </ol>
     * Questo metodo suppone che il {@link PluginDownloadData} passato
     * corrisponda al file con cui è stata generata questa istanza.
     *
     * @param pd I dati del plugin
     * @return {@code true} se l'installazione avviene con successo.
     */
    public boolean installFrom(PluginDownloadData pd) {
        try {
            ZipFile file = new ZipFile(to_install);
            Iterator<? extends ZipEntry> asIterator = file.entries().asIterator();
            boolean jar_found = false;
            ZipEntry to_extract = null;
            while (asIterator.hasNext() && !jar_found) {
                ZipEntry entry = asIterator.next();
                if (!entry.isDirectory()) {
                    var name = entry.getName();
                    if (name.endsWith(".jar")) {
                        to_extract = entry;
                        jar_found = true;
                    }
                }
            }
            if (to_extract != null) {
                Path newPath = zipSlipProtect(to_extract, SetupInit.getInstance().getPLUGINS_FOLDER().toPath());
                Files.copy(file.getInputStream(to_extract), newPath, StandardCopyOption.REPLACE_EXISTING);
                if (pd != null) {
                    PluginFolderHandler.getInstance().addPluginData(newPath.toFile(), pd.getName(), pd.getInj_point(), pd.getVersion().toString(), pd.getUuid(), pd.getRequires());
                    file.close();
                    return true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Installer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }

    /**
     * A differenza di {@link #installFrom(theopenhand.installer.online.store.PluginDownloadData)
     * }, questo metodo estra i dati del plugin direttamente dal file
     * {@code .properties} teoricamente contenuto nel file zip del plugin.
     * <br>
     * Pre tutto il resto si comporta esattamente come l'altro metodo.
     *
     * @return {@code true} se l'installazione avviene con successo.
     */
    public boolean installZipComplete() {
        try {
            ZipFile file = new ZipFile(to_install);
            Iterator<? extends ZipEntry> asIterator = file.entries().asIterator();
            Properties p = new Properties();
            boolean prop_found = false, jar_found = false;
            ZipEntry to_extract = null;
            while (asIterator.hasNext() && !(prop_found && jar_found)) {
                ZipEntry entry = asIterator.next();
                if (!entry.isDirectory()) {
                    var name = entry.getName();
                    if (name.endsWith(".properties")) {
                        try (InputStream is = file.getInputStream(entry)) {
                            p.load(is);
                        } catch (IOException ex) {
                            Logger.getLogger(Installer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        prop_found = true;
                    } else if (name.endsWith(".jar")) {
                        to_extract = entry;
                        jar_found = true;
                    }
                }
            }
            if (to_extract != null) {
                Path newPath = zipSlipProtect(to_extract, SetupInit.getInstance().getPLUGINS_FOLDER().toPath());
                Files.copy(file.getInputStream(to_extract), newPath, StandardCopyOption.REPLACE_EXISTING);
                String pknm = p.getProperty("pknm");
                String injnm = p.getProperty("injnm");
                String uid = p.getProperty("uuid", null);
                String ver = p.getProperty("ver", "1");
                String req = p.getProperty("req");
                ArrayList<UUID> uids = new ArrayList<>();
                if (req != null) {
                    String[] reqs = req.split(",");
                    for (String s : reqs) {
                        uids.add(UUID.fromString(s));
                    }
                }
                if (pknm != null && injnm != null && !pknm.isBlank() && !injnm.isBlank()) {
                    if (uid != null && !uid.isBlank()) {
                        PluginFolderHandler.getInstance().addPluginData(newPath.toFile(), pknm, injnm, ver, UUID.fromString(uid), uids);
                    } else {
                        PluginFolderHandler.getInstance().addPluginData(newPath.toFile(), pknm, injnm, ver);
                    }
                    file.close();
                    return true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Installer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }

    private static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir) throws IOException {
        Path targetDirResolved = targetDir.resolve(zipEntry.getName());
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }
        return normalizePath;
    }

}
