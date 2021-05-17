package com.nure.ua.a_clientSide.controller;

import com.google.gson.Gson;
import com.nure.ua.a_clientSide.Client;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.exchangeData.Response;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {
    private double xOffset;
    private double yOffset;
    protected Client application;

    public void setApplication(Client application) {
        this.application = application;
    }

    @FXML
    private void onWindowPressed(MouseEvent mouseEvent) {
        Window window = application.getStage();
        xOffset = window.getX() - mouseEvent.getScreenX();
        yOffset = window.getY() - mouseEvent.getScreenY();
    }

    @FXML
    private void onWindowDragged(MouseEvent mouseEvent) {
        Window window = application.getStage();
        window.setX(mouseEvent.getScreenX() + xOffset);
        window.setY(mouseEvent.getScreenY() + yOffset);
    }

    @FXML
    private AnchorPane mainWindow;

    @FXML
    private AnchorPane toolPanel;

    @FXML
    private void onAnchorPaneClick(MouseEvent mouseEvent) {
        mainWindow.requestFocus();
    }

    @FXML
    private void onQuit() {
        exit();
    }

    protected void exit() {
        Platform.exit();
        System.exit(0);
    }

    protected void sendToServer(Request request) {
        application.getWriter().println(new Gson().toJson(request));
    }

    public abstract void receiveData(Response response) throws IOException;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            if (toolPanel != null) {
                toolPanel.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onWindowDragged);
                toolPanel.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onWindowPressed);
            }
            if (mainWindow != null) {
                mainWindow.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onAnchorPaneClick);
                mainWindow.requestFocus();
            }
        });
    }
}
