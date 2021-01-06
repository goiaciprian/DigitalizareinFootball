package com.OlimpiaComarnic.GUI.Popup;

import com.OlimpiaComarnic.Backend.utils.DBConnection;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdatePopup extends Application {

    public ProgressBar progressBar;

    public AnchorPane pane;

    private String workingDir, _downloadUrl;

    private Stage newStage;

    private Task<Void> task = new Task<Void>() {

        @Override
        protected Void call() throws Exception {
            URL url = new URL(_downloadUrl);
            RandomAccessFile file = null;
            InputStream stream = null;
            int downloaded = 0, size = -1;
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Range", "bytes="+downloaded+"'");
                connection.connect();

                int response = connection.getResponseCode();
                if(response / 100 != 2)
                    throw new Exception("Response error "+ response);

                int length = connection.getContentLength();
                if(length < 1)
                    throw new Exception("Content length less than 1");

                if(size == -1)
                    size=length;

                file = new RandomAccessFile(workingDir + "\\OlimpiaComarnicNew.exe", "rw");
                file.seek(downloaded);

                stream = connection.getInputStream();

                while(downloaded != size) {
                    byte[] buffer = new byte[2048];
                    int read = stream.read(buffer);
                    if(read == -1) break;
                    file.write(buffer, 0, read);
                    downloaded += read;
                    updateProgress(downloaded, size);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    };

    @Override
    public void start(Stage primaryStage) throws IOException {
        createDesign();

        Image img = primaryStage.getIcons().get(0);
        primaryStage.close();

        Scene scene = new Scene(pane);
        newStage = new Stage();

        newStage.setOnCloseRequest(e-> {
            DBConnection.closeConn();
            //This assures that all thread are closed
            primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });

        newStage.getIcons().add(img);
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.setTitle("Updater");
        newStage.centerOnScreen();

        newStage.show();

        final Thread thread = new Thread(task,"task-thread");
        thread.setDaemon(true);
        thread.start();

    }


    public UpdatePopup setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
        return this;
    }

    public UpdatePopup setDownloadUrl(String downloadUrl) {
        this._downloadUrl = downloadUrl;
        return this;
    }

    private void createDesign() {
        pane = new AnchorPane();
        pane.setPrefWidth(432);
        pane.setPrefHeight(140);

        Label downloading = new Label();
        downloading.setText("Downloading...");
        downloading.setPrefHeight(18);
        downloading.setPrefWidth(105);
        downloading.setLayoutX(37);
        downloading.setLayoutY(46);

        progressBar = new ProgressBar();
        progressBar.setLayoutX(37);
        progressBar.setLayoutY(74);
        progressBar.setPrefWidth(358);
        progressBar.progressProperty().bind(
                task.progressProperty()
        );

        task.setOnSucceeded((e)->{
            startNewVersion();
            newStage.fireEvent(new WindowEvent(newStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });

        pane.getChildren().add(progressBar);
        pane.getChildren().add(downloading);
        pane.setStyle(
                "-fx-background-color: radial-gradient(center 50% 50%, radius 100%, #ffffff 30%, #00a3c3)"
        );

    }

    private void startNewVersion() {
        final String command = "powershell -Command \"cd " + workingDir + ";" +
                "rm OlimpiaComarnic.exe;" +
                "mv OlimpiaComarnicNew.exe OlimpiaComarnic.exe;"+
                ".\\OlimpiaComarnic.exe\"";
        Process process;
        try {
            process = Runtime.getRuntime().exec(command);
            process.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
