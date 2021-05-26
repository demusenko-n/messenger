package com.nure.ua.a_clientSide.controller;

import com.nure.ua.Utility;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.exchangeData.dataPack.DataPack;
import com.nure.ua.exchangeData.dataPack.DataPackImpl;
import com.nure.ua.exchangeData.response.Response;
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

public class AuthController extends Controller {
    @FXML
    private Label signUpLink;
    @FXML
    private Button actionButton;
    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label errorLabel;

    @FXML
    private void onSignupClicked(Event ev) {
        try {
            application.loadFxml(Utility.FXML_PATH_REG);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            exit();
        }
    }

    @FXML
    private void onSignInAction(Event ev) {
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
        if (isEverythingValid) {
            DataPackImpl dp = new DataPackImpl();
            dp.setCommand("sign_in");
            dp.getArgs().put("login", login);
            dp.getArgs().put("password", password);

            sendToServer(new Request(dp));
        } else {
            errorLabel.setText(errorMsg);
            errorLabel.setVisible(true);
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
            actionButton.addEventHandler(ActionEvent.ACTION, this::onSignInAction);
            signUpLink.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSignupClicked);
            errorLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, (ev)->errorLabel.setVisible(false));
        });
    }
}
