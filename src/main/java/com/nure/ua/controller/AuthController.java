package com.nure.ua.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class AuthController extends Controller {

    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private void onSignupClicked(MouseEvent mouseEvent) {
        try {
            switchCurrentFxml("/static/register.fxml", getStageFromEvent(mouseEvent));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            exit();
        }
    }
    @FXML
    private void onSignInAction() {
        String login = loginTextField.getText();
        String password = passwordTextField.getText();

//        sendToServer(Attribute.composeAttribute("TYPE", "SIGNIN") +
//                Attribute.composeAttribute("LOGIN", login) +
//                Attribute.composeAttribute("PASSWORD", password));
    }
    @Override
    public void receiveData(String string) {
        System.out.println("AuthController received " + string);
    }
}
