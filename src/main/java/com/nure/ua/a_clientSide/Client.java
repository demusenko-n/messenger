package com.nure.ua.a_clientSide;

import com.nure.ua.Utility;
import com.nure.ua.a_clientSide.controller.Controller;
import com.nure.ua.a_serverSide.model.entity.Message;
import com.nure.ua.a_serverSide.model.entity.User;
import com.nure.ua.exchangeData.response.Response;
import com.nure.ua.exchangeData.session.Session;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class Client extends Application {
    private PrintWriter writer;
    private volatile Controller controller;
    private Stage stage;
    private Session session;
    private Map<User, List<Message>> allMessages;
    private static final String address = "localhost";
    private static final int port = 4004;

    public void setSession(Session session) {
        this.session = session;
    }

    public void setAllMessages(Map<User, List<Message>> allMessages) {
        this.allMessages = allMessages;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public Stage getStage() {
        return stage;
    }

    public Session getSession() {
        return session;
    }

    public Map<User, List<Message>> getAllMessages() {
        return allMessages;
    }

    public void receiveData(Response response) throws IOException {
        while (controller == null) {
            Thread.onSpinWait();
        }
        controller.receiveData(response);
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        stage.initStyle(StageStyle.TRANSPARENT);
        switchFxml(Utility.FXML_PATH_AUTH);
        stage.show();
    }

    @Override
    public void init() throws IOException {
        Socket clientSocket = new Socket(address, port);
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
        Thread serverReader = new ServerReader(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())), this);
        serverReader.start();
    }

    public void switchFxml(String stringPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(stringPath));
        Scene scene = new Scene(loader.load());
        controller = loader.getController();
        controller.setApplication(this);
        controller.updateAllChats();
        scene.setFill(Color.TRANSPARENT);
        Platform.runLater(() -> stage.setScene(scene));
    }

    public static void main(String[] args) {
        launch();
    }
}
