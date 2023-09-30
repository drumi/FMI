package com;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class RegisterController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnRegister;

    @FXML
    private Label lblConfirmPassword;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblPassword;

    @FXML
    private Label lblPhoneNumber;

    @FXML
    private Label lblUsername;

    @FXML
    private PasswordField pwdConfirmPassword;

    @FXML
    private PasswordField pwdPassword;

    @FXML
    private Tooltip tltConfirmPassword;

    @FXML
    private Tooltip tltEmail;

    @FXML
    private Tooltip tltPassword;

    @FXML
    private Tooltip tltPhone;

    @FXML
    private Tooltip tltUsername;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TextField txtUsername;

    @FXML
    void btnRegisterClicked(ActionEvent event) {
        if (!ValidateInput.validateUsername(txtUsername.getText()))
            lblUsername.setText("Invalid username");

        if (!ValidateInput.validatePhone(txtPhoneNumber.getText()))
            lblPhoneNumber.setText("Invalid phone");

        if (!ValidateInput.validateEmail(txtUsername.getText()))
            lblEmail.setText("Invalid email");

        if (!pwdPassword.getText().equals(pwdConfirmPassword.getText()))
            lblPassword.setText("Passwords do not match");
        else if (pwdPassword.getText().isBlank())
            lblPassword.setText("Password cannot be empty");
    }

    @FXML
    void initialize() {
        assert btnRegister != null : "fx:id=\"btnRegister\" was not injected: check your FXML file 'register.fxml'.";
        assert lblConfirmPassword != null : "fx:id=\"lblConfirmPassword\" was not injected: check your FXML file 'register.fxml'.";
        assert lblEmail != null : "fx:id=\"lblEmail\" was not injected: check your FXML file 'register.fxml'.";
        assert lblPassword != null : "fx:id=\"lblPassword\" was not injected: check your FXML file 'register.fxml'.";
        assert lblPhoneNumber != null : "fx:id=\"lblPhoneNumber\" was not injected: check your FXML file 'register.fxml'.";
        assert lblUsername != null : "fx:id=\"lblUsername\" was not injected: check your FXML file 'register.fxml'.";
        assert pwdConfirmPassword != null : "fx:id=\"pwdConfirmPassword\" was not injected: check your FXML file 'register.fxml'.";
        assert pwdPassword != null : "fx:id=\"pwdPassword\" was not injected: check your FXML file 'register.fxml'.";
        assert tltConfirmPassword != null : "fx:id=\"tltConfirmPassword\" was not injected: check your FXML file 'register.fxml'.";
        assert tltEmail != null : "fx:id=\"tltEmail\" was not injected: check your FXML file 'register.fxml'.";
        assert tltPassword != null : "fx:id=\"tltPassword\" was not injected: check your FXML file 'register.fxml'.";
        assert tltPhone != null : "fx:id=\"tltPhone\" was not injected: check your FXML file 'register.fxml'.";
        assert tltUsername != null : "fx:id=\"tltUsername\" was not injected: check your FXML file 'register.fxml'.";
        assert txtEmail != null : "fx:id=\"txtEmail\" was not injected: check your FXML file 'register.fxml'.";
        assert txtPhoneNumber != null : "fx:id=\"txtPhoneNumber\" was not injected: check your FXML file 'register.fxml'.";
        assert txtUsername != null : "fx:id=\"txtUsername\" was not injected: check your FXML file 'register.fxml'.";

    }

}
