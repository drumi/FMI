package problem1;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AccountController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCredit;

    @FXML
    private Button btnRate;

    @FXML
    private Button btnSaving;

    @FXML
    private Button btnShow;

    @FXML
    private Label lblBalance;

    @FXML
    private VBox lblRate;

    @FXML
    private TextArea txaCredit;

    @FXML
    private TextArea txaSaving;

    @FXML
    private TextField txtBalance;

    @FXML
    private TextField txtRate;

    @FXML
    void btnCreditClicked(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void btnRateClicked(ActionEvent event) {
        txaSaving.setText("test");
    }

    @FXML
    void btnSavingClicked(ActionEvent event) {

    }

    @FXML
    void btnShowClicked(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert btnCredit != null : "fx:id=\"btnCredit\" was not injected: check your FXML file 'sceneAccount.fxml'.";
        assert btnRate != null : "fx:id=\"btnRate\" was not injected: check your FXML file 'sceneAccount.fxml'.";
        assert btnSaving != null : "fx:id=\"btnSaving\" was not injected: check your FXML file 'sceneAccount.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'sceneAccount.fxml'.";
        assert lblBalance != null : "fx:id=\"lblBalance\" was not injected: check your FXML file 'sceneAccount.fxml'.";
        assert lblRate != null : "fx:id=\"lblRate\" was not injected: check your FXML file 'sceneAccount.fxml'.";
        assert txaCredit != null : "fx:id=\"txaCredit\" was not injected: check your FXML file 'sceneAccount.fxml'.";
        assert txaSaving != null : "fx:id=\"txaSaving\" was not injected: check your FXML file 'sceneAccount.fxml'.";
        assert txtBalance != null : "fx:id=\"txtBalance\" was not injected: check your FXML file 'sceneAccount.fxml'.";
        assert txtRate != null : "fx:id=\"txtRate\" was not injected: check your FXML file 'sceneAccount.fxml'.";

    }

}
