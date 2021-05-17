package com.nure.ua.a_clientSide.controller;

import com.nure.ua.Utility;
import com.nure.ua.a_serverSide.model.entity.Message;
import com.nure.ua.a_serverSide.model.entity.User;
import com.nure.ua.exchangeData.DataPack;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.exchangeData.Response;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AuthController extends Controller {

    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label errorLabel;

    @FXML
    private void onSignupClicked() {
        try {
            application.switchFxml(Utility.FXML_PATH_REG);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            exit();
        }
    }

    @FXML
    private void onSignInAction() {
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
            DataPack dp = new DataPack();
            dp.command = "sign_in";
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
        application.setSession(response.getSession());
        DataPack dp = response.getData();
        if (dp.isFailState()) {
            Platform.runLater(() -> {
                errorLabel.setText((String) dp.getArgs().get("message"));
                errorLabel.setVisible(true);
            });
        } else if (dp.command.equals("auth")) {
            @SuppressWarnings("unchecked")
            Map<User, List<Message>> all_messages = (Map<User, List<Message>>) dp.getArgs().get("all_messages");
            application.setAllMessages(all_messages);
            application.switchFxml(Utility.FXML_PATH_MAIN);
        }
    }
}
