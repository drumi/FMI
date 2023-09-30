package fxtest;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CipherController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnDecrypt;

    @FXML
    private Button btnEncrypt;

    @FXML
    private Button btnQuit;

    @FXML
    private Label lblCipher;

    @FXML
    private Label lblDecrypt;

    @FXML
    private Label lblEncrypt;

    @FXML
    private TextField txtCipher;

    @FXML
    private TextField txtEncrypt;

    @FXML
    private TextField txtdecrypt;

    @FXML
    void btnDecryptClicked(ActionEvent event) {

    }

    @FXML
    void btnEncryptClicked(ActionEvent event) {

    }

    @FXML
    void btnQuitClicked(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void initialize() {
        assert btnDecrypt != null : "fx:id=\"btnDecrypt\" was not injected: check your FXML file 'cipher.fxml'.";
        assert btnEncrypt != null : "fx:id=\"btnEncrypt\" was not injected: check your FXML file 'cipher.fxml'.";
        assert btnQuit != null : "fx:id=\"btnQuit\" was not injected: check your FXML file 'cipher.fxml'.";
        assert lblCipher != null : "fx:id=\"lblCipher\" was not injected: check your FXML file 'cipher.fxml'.";
        assert lblDecrypt != null : "fx:id=\"lblDecrypt\" was not injected: check your FXML file 'cipher.fxml'.";
        assert lblEncrypt != null : "fx:id=\"lblEncrypt\" was not injected: check your FXML file 'cipher.fxml'.";
        assert txtCipher != null : "fx:id=\"txtCipher\" was not injected: check your FXML file 'cipher.fxml'.";
        assert txtEncrypt != null : "fx:id=\"txtEncrypt\" was not injected: check your FXML file 'cipher.fxml'.";
        assert txtdecrypt != null : "fx:id=\"txtdecrypt\" was not injected: check your FXML file 'cipher.fxml'.";

    }

}
