package com.OlimpiaComarnic.GUI.Utils;

import com.OlimpiaComarnic.GUI.GUIRun;
import com.OlimpiaComarnic.GUI.Popup.UpdatePopup;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Updater {

    public static class UpdaterBuilder {

        private Version _version;
        private String _url;

        public UpdaterBuilder() {
        }

        /**
         * Add a specific version for the current build, if the string provided is wrong it will get the version from maven
         *
         * @param version the version number has to be 1.0.0
         */
        public UpdaterBuilder setVersion(String version) {
            String regex = "^\\d+\\.\\d+\\.\\d+$";
            if (version.matches(regex)) {
                _setVersion(version);
            }
            else {
                setVersion();
            }

            return this;
        }

        /**
         * Get version maven
         * @deprecated this method does not work after maven package because the maven file is moved to manifest/mavem/.../pom.xml until it is fix this method is deprecated
         */
        @Deprecated
        public UpdaterBuilder setVersion() {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            try {
                Model model = reader.read(new FileReader(System.getProperty("user.dir") + "\\pom.xml"));
                _setVersion(model.getVersion());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return this;
        }

        /**
         * Set url where to compare versions and download the update
         *
         * @param url download url
         */
        public UpdaterBuilder setUrl(String url) {
            _url = url;
            return this;
        }


        /**
         * Creates an instance of the builder class
         *
         * @return return the updater
         */
        public Updater build() throws Exception {
            if (_version == null)
                setVersion();
            if (_url == null)
                throw new Exception("You have to provide an url");
            return new Updater(this);
        }

        private UpdaterBuilder _setVersion(String version) {
            _version = new Version(version);
            return this;
        }
    }

    private final Version _version;
    private final String _url;
    private String _downloadUrl;

    private final Path CurrPath = Paths.get("");
    private final String RunningDirectory = CurrPath.toAbsolutePath().toString();

    Updater(UpdaterBuilder builder) {
        _url = builder._url;
        _version = builder._version;
    }

    public void printStats() {
        System.out.println(_version.get() + " " + _url);
    }

    public void Start() {

        CompletableFuture.supplyAsync(this::checkForUpdate)
                .thenAccept(result -> {
                    if(result) {
                        Platform.runLater(() -> {
                            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
                            dialog.setTitle("Updater");
                            dialog.setHeaderText("Actualizare noua.");
                            dialog.setContentText("O noua actualizare este disponibila.\nDoriti sa o descarcati?");
                            dialog.initOwner(GUIRun.currStage);
                            ((Button)dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Da");
                            ((Button)dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Nu");

                            Optional<ButtonType> res = dialog.showAndWait();
                            if(res.isPresent() && res.get() == ButtonType.OK) {
                                beginUpdate();
                            }
                        });
                    }
                });

    }

    private boolean checkForUpdate() {
        try {
            URL url = new URL(_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int status = conn.getResponseCode();
            if(status != 200)
                return false;

            String[] results = getVersionAndUrl(conn);
            _downloadUrl = results[1];
            System.out.println(results[0]);

            conn.disconnect();
            return needUpdate(results[0]);

        } catch (Exception e) {
            System.err.println("Error in checking for update\n");
            e.printStackTrace();
        }

        return false;
    }

    private void beginUpdate() {
        try {
            StartDownload();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void StartDownload() {
        try {
            Platform.runLater(()-> {
                try {
                    new UpdatePopup()
                            .setDownloadUrl(_downloadUrl)
                            .setWorkingDir(RunningDirectory)
                            .start(GUIRun.currStage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }catch (Exception e) {
            System.err.println("Error in downloading new update " + e.getMessage());
        }
    }

    private String[] getVersionAndUrl(HttpURLConnection conn) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        JSONObject json = new JSONObject(content.toString());
        JSONObject asset = json.getJSONArray("assets").getJSONObject(0);


        String latestVersion = json.get("tag_name").toString().replace("v", "");
        String downloadUrl = asset.getString("browser_download_url");

        return new String[] {latestVersion, downloadUrl};
    }

    private boolean needUpdate(String version) {
        System.out.println(_version.compareTo(new Version(version)));
        return _version.compareTo(new Version(version)) < 0;
    }


}
