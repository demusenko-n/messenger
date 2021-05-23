package com.nure.ua.a_clientSide.controller;

import com.nure.ua.a_clientSide.Client;
import com.nure.ua.exchangeData.GsonCreator;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.exchangeData.response.Response;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

import java.io.IOException;

public abstract class Controller {
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
        application.getWriter().println(GsonCreator.getInstance().toJson(request));
    }

    public abstract void receiveData(Response response) throws IOException;

    public void updateAllChats() {
    }

    @FXML
    public void initialize() {
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
