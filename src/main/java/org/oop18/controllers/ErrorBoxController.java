package org.oop18.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The error box controller is used to notify the user that
 * some operations are illegal and thus fail.
 *
 * @author - Haribo
 */
public class ErrorBoxController implements Initializable {
    private  String errorMessage;

    @FXML
    private Label errorMessageLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMessageLabel.setText(errorMessage);
    }

    public ErrorBoxController(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
