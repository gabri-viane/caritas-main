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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;

/**
 *
 * @author gabri
 */
public class WebConnection {

    private WebConnection() {

    }

    public static Task<Void> download(String title, String file_link_from, File folder_to, String file_name) {
        if (TestConnection.isOnline("https://vnl-eng.net")) {
            Task<Void> task = new DownloadTask(file_link_from, file_name, folder_to);
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
            return task;
        } else {
            return null;
        }
    }

    public static String comunicate(String newtargetURL, String newurlParameters) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(newtargetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(newurlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(newurlParameters);

            if (connection.getResponseCode() == 200) {
                //Get Response  
                InputStream is = connection.getInputStream();
                StringBuffer response;
                try ( BufferedReader rd = new BufferedReader(new InputStreamReader(is))) {
                    response = new StringBuffer();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                }
                return response.toString();
            } else {
                return null;
            }
        } catch (IOException ex) {
            System.err.print(ex);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static class DownloadTask extends Task<Void> {

        private final String url;
        private final String update_file_name;
        private final File output;

        public DownloadTask(String url, String file_name, File dir) {
            this.url = url;
            this.update_file_name = file_name;
            this.output = new File(dir.getAbsolutePath() + File.separatorChar + update_file_name);
        }

        @Override
        protected Void call() throws Exception {
            updateMessage("Inizio download...");
            URLConnection connection = new URL(url).openConnection();
            long file_length = connection.getContentLengthLong();
            try (
                     InputStream is = connection.getInputStream();  FileOutputStream fos = new FileOutputStream(output);) {

                long nread = 0L;
                byte[] buf = new byte[8192];
                int n;
                while ((n = is.read(buf)) > 0) {
                    fos.write(buf, 0, n);
                    nread += n;
                    updateProgress(nread, file_length);
                    updateMessage((int) ((100 * nread) / file_length) + "%");
                }
                updateMessage("Download completato");
                fos.close();
                is.close();
            } catch (Exception e) {
                Logger.getLogger(WebConnection.class.getName()).log(Level.SEVERE, null, e);
            }
            return null;
        }

        public File getOutput() {
            return output;
        }

    }

}
