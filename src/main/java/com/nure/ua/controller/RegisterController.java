package com.nure.ua.controller;

import com.nure.ua.util.Attribute;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class RegisterController extends Controller {
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField passwordTextField;

    @FXML
    private void onSignInClicked(MouseEvent mouseEvent) {
        try {
            switchCurrentFxml("/static/auth.fxml", getStageFromEvent(mouseEvent));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            exit();
        }
    }

    @Override
    public void receiveData(String string) {
        System.out.println("RegisterController received " + string);

    }

    @FXML
    private void onSignUpAction() {
        String name = nameTextField.getText();
        String login = loginTextField.getText();
        String password = passwordTextField.getText();

        sendToServer(Attribute.composeAttribute("TYPE", "SIGNUP") +
                Attribute.composeAttribute("NAME", name) +
                Attribute.composeAttribute("LOGIN", login) +
                Attribute.composeAttribute("PASSWORD", password));
    }
}
