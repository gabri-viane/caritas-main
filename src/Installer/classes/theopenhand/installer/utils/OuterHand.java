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
package theopenhand.installer.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import theopenhand.installer.SetupInit;
import theopenhand.statics.StaticReferences;

/**
 *
 * @author gabri
 */
public class OuterHand {

    private static OuterHand instance;

    private final String ss = System.getProperty("user.dir");
    private final String app_folder_path = ss + File.separatorChar + "app";
    private String cfg_name;
    private final Library lib;
    private StringBuilder sb;

    public OuterHand() {
        lib = new Library();
        init();
    }

    private void init() {
        File f = new File(ss);
        if (f.exists() && f.isDirectory()) {
            cfg_name = f.getName() + ".cfg";
        }
        StaticReferences.subscribeOnExit(() -> {
            execute();
        });
    }

    public static OuterHand getInstance() {
        if (instance == null) {
            instance = new OuterHand();
        }
        return instance;
    }

    public void installLibrary(File f) {
        if (f.exists() && f.isDirectory()) {
            findFiles(f.getAbsolutePath()).stream().forEachOrdered(fl -> lib.install(fl));
        } else {
            lib.install(f);
        }
    }

    public void linkLibrary(File f) {
        if (f.exists() && f.isDirectory()) {
            lib.link(f);
        } else {
            lib.link(f);
        }
    }

    public void execute() {
        try {
            sb = new StringBuilder();
            sb.append("cmd /c \"").append(SetupInit.getInstance().getLIBS_FOLDER().getAbsolutePath()).append(File.separatorChar).append("NCDBOuterHands.exe ").append(lib.flush()).append("-p \"").append(app_folder_path)
                    .append("\" -m \"").append(app_folder_path)
                    .append(File.separatorChar).append(cfg_name).append("\"");
            Runtime.getRuntime().exec(sb.toString());
            sb = null;
        } catch (IOException ex) {
            Logger.getLogger(OuterHand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ArrayList<File> findFiles(String path) {
        ArrayList<File> files = new ArrayList<>();
        try {
            Files.list(new File(path).toPath()).filter((t) -> {
                return t.toFile().getName().endsWith(".jar");
            }).forEachOrdered((t) -> {
                files.add(t.toFile());
            });
        } catch (IOException ex) {
            Logger.getLogger(OuterHand.class.getName()).log(Level.SEVERE, null, ex);
        }
        return files;
    }

    private class Library {

        private StringBuilder sb;

        protected Library() {
            sb = new StringBuilder();
        }

        private void addSegment() {
            sb.append("-l ");
        }

        protected void install(File f) {
            if (f.exists() && f.isFile()) {
                addSegment();
                sb.append("-i \"").append(f.getAbsolutePath()).append("\" ");
            }
        }

        protected void link(File f) {
            if (f.exists() && f.isDirectory()) {
                addSegment();
                sb.append("-l \"").append(f.getAbsolutePath()).append("\" ");
            }
        }

        public String flush() {
            String str = sb.toString();
            sb = new StringBuilder();
            return str;
        }
    }

}
