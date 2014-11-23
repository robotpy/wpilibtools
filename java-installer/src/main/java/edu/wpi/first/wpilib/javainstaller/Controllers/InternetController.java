package edu.wpi.first.wpilib.javainstaller.Controllers;

import edu.wpi.first.wpilib.javainstaller.MainApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Handles ensuring that the internet is up and running on the system before proceeding
 */
public class InternetController extends AbstractController {

    @FXML
    private BorderPane mainView;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Button nextButton;

    public InternetController() {
        super("/fxml/intro_screen.fxml");
    }

    @FXML
    public void handleNext() {
        // Handle the connection test on a background thread to avoid blocking the ui. If the user clicks next and can't
        // get to the internet, they have the ability to stop the program without waiting for a timeout
        nextButton.setDisable(true);
        nextButton.setText("Checking Internet");
        new Thread(() -> {
            try {
                // Test for connection to the oracle site. If no connection, show an error
                HttpURLConnection connection = (HttpURLConnection) DownloadController.JRE_URL.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // We have a connection, load the next page
                    Platform.runLater(() -> {
                        FXMLLoader loader = new FXMLLoader();
                        Parent root = null;
                        try {
                            root = loader.load(getClass().getResource("/fxml/download.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mainView.getScene().setRoot(root);
                    });
                } else {
                    Platform.runLater(() -> {
                        try {
                            MainApp.showErrorPopup("Could not connect to the JRE website at: " + DownloadController.JRE_URL_STRING + ", error code is " + connection.getResponseCode());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        nextButton.setDisable(false);
                        nextButton.setText("Retry >");
                    });
                }
            } catch (java.io.IOException e) {
                Platform.runLater(() -> {
                    MainApp.showErrorPopup(
                            "Unknown error when attempting to connect to the Oracle website: "
                                    + System.lineSeparator()
                                    + e);
                    nextButton.setDisable(false);
                    nextButton.setText("Retry >");
                });
            }
        }).start();
    }

    @FXML
    public void handleDownloaded(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Oracle JRE");
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Compressed JRE", "*.tar.gz"));
        File jre = chooser.showOpenDialog(mainView.getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/untar.fxml"));
        try {
            Parent root = loader.load();
            UntarController controller = loader.getController();
            controller.initialize(jre.getAbsolutePath());
            mainView.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}