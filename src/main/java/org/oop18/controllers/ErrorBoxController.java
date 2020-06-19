package org.oop18.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
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
