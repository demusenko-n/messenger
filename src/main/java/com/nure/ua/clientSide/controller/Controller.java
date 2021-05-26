package com.nure.ua.clientSide.controller;

import com.nure.ua.clientSide.Client;
import com.nure.ua.jsonData.GsonCreator;
import com.nure.ua.jsonData.Request;
import com.nure.ua.jsonData.response.Response;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
    private BorderPane mainWindow;

    @FXML
    private AnchorPane toolPanel;

    @FXML
    private ImageView quitButton;

    protected void exit() {
        Platform.exit();
        System.exit(0);
    }

    protected void sendToServer(Request request) {
        application.getWriter().println(GsonCreator.getInstance().toJson(request));
    }

    public void showError(String str) {
        System.err.println("error inside controller: " + str);
    }

    public abstract void receiveData(Response response) throws IOException;

    public void updateAllChats() {
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (toolPanel != null) {
                toolPanel.addEventHandler(MouseEvent.MOUSE_DRAGGED, (e) -> {
                    Window window = application.getStage();
                    window.setX(e.getScreenX() + xOffset);
                    window.setY(e.getScreenY() + yOffset);
                });

                toolPanel.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                    Window window = application.getStage();
                    xOffset = window.getX() - e.getScreenX();
                    yOffset = window.getY() - e.getScreenY();
                });

            }
            if (mainWindow != null) {
                mainWindow.requestFocus();
                mainWindow.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> mainWindow.requestFocus());
                mainWindow.requestFocus();
            }
            if (quitButton != null) {
                quitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> exit());
            }
        });
    }
}
