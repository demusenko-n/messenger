package com.nure.ua.controller;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {

    private PrintWriter writer;
    private double xOffset;
    private double yOffset;

    @FXML
    private void onWindowPressed(MouseEvent mouseEvent) {
        Window window = getStageFromEvent(mouseEvent);
        xOffset = window.getX() - mouseEvent.getScreenX();
        yOffset = window.getY() - mouseEvent.getScreenY();
    }

    @FXML
    private void onWindowDragged(MouseEvent mouseEvent) {
        Window window = getStageFromEvent(mouseEvent);
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

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    protected void exit() {
        Platform.exit();
        System.exit(0);
    }

    protected Stage getStageFromEvent(Event event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    protected void switchCurrentFxml(String stringPath, Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(stringPath));
        stage.setScene(new Scene(loader.load()));

        Controller controller = loader.getController();
        controller.setWriter(writer);
    }

    protected void sendToServer(String string) {
        writer.println(string);
        System.out.println("send " + string);
    }

    public abstract void receiveData(String string);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            if(toolPanel != null) {
                toolPanel.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onWindowDragged);
                toolPanel.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onWindowPressed);
            }
            if(mainWindow != null) {
                mainWindow.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onAnchorPaneClick);
                mainWindow.requestFocus();
            }
        });
    }
}
