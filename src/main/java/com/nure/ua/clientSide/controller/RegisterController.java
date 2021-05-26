package com.nure.ua.clientSide.controller;

import com.nure.ua.Utility;
import com.nure.ua.jsonData.Request;
import com.nure.ua.jsonData.dataPack.DataPack;
import com.nure.ua.jsonData.dataPack.DataPackImpl;
import com.nure.ua.jsonData.response.Response;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class RegisterController extends Controller {
    @FXML
    private Label signInLink;
    @FXML
    private Button actionButton;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField passwordTextField;

    @FXML
    private void onSignInClicked(Event ev) {
        try {
            application.loadFxml(Utility.FXML_PATH_AUTH);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            exit();
        }
    }

    @Override
    public void receiveData(Response response) throws IOException {
        DataPack dp = response.getData();

        if (dp.getCommand().equals("auth")) {
            application.loadFxml(Utility.FXML_PATH_MAIN);
        } else {
            System.err.println("unknown command: " + dp.getCommand());
        }
    }

    @FXML
    private void onSignUpAction(Event ev) {
        String name = nameTextField.getText();
        String login = loginTextField.getText();
        String password = passwordTextField.getText();

        boolean isEverythingValid = true;
        String errorMsg = "";
        if (!Utility.matchesLogin(login)) {
            isEverythingValid = false;
            errorMsg += "Login must contain 2-20 characters, only digits, numbers, hyphens and underscores\n";
        }
        if (!Utility.matchesPassword(password)) {
            isEverythingValid = false;
            errorMsg += "Password must contain 8-30 characters, at least one letter and one number\n";
        }
        if (!Utility.matchesName(name)) {
            isEverythingValid = false;
            errorMsg += "Name incorrect input\n";
        }

        if (isEverythingValid) {
            DataPackImpl dp = new DataPackImpl();
            dp.setCommand("sign_up");

            dp.getArgs().put("login", login);
            dp.getArgs().put("password", password);
            dp.getArgs().put("name", name);

            sendToServer(new Request(dp));
        } else {
            errorLabel.setText(errorMsg);
            errorLabel.setVisible(true);
        }
    }

    @Override
    public void showError(String str) {
        super.showError(str);
        errorLabel.setText(str);
        errorLabel.setVisible(true);
    }

    @Override
    public void initialize() {
        super.initialize();
        Platform.runLater(() -> {
            actionButton.addEventHandler(ActionEvent.ACTION, this::onSignUpAction);
            signInLink.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSignInClicked);
            errorLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, (ev)->errorLabel.setVisible(false));
        });
    }
}