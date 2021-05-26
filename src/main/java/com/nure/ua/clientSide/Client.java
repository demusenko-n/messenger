package com.nure.ua.clientSide;

import com.google.gson.reflect.TypeToken;
import com.nure.ua.Utility;
import com.nure.ua.clientSide.controller.Controller;
import com.nure.ua.entity.Message;
import com.nure.ua.entity.User;
import com.nure.ua.jsonData.dataPack.DataPack;
import com.nure.ua.jsonData.response.Response;
import com.nure.ua.jsonData.session.Session;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Client extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        stage.initStyle(StageStyle.TRANSPARENT);
        loadFxml(Utility.FXML_PATH_AUTH);
        stage.show();
    }

    private PrintWriter writer;
    private volatile Controller controller;
    private Stage stage;
    private Session session;
    private Map<User, List<Message>> allMessages;
    private static final String address = "localhost";
    private static final int port = 4004;

    public void addMessage(Message message) {
        User otherUser;
        if (message.getSender().getId() == session.getUser().getId()) {
            otherUser = message.getReceiver();
        } else {
            otherUser = message.getSender();
        }
        if (allMessages.containsKey(otherUser)) {
            allMessages.get(otherUser).add(message);
        } else {
            List<Message> list = new ArrayList<>();
            list.add(message);
            allMessages.put(otherUser, list);
        }
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

        DataPack dp = response.getData();
        session = response.getSession();

        if (dp.has("all_messages")) {
            TypeToken<Map<User, List<Message>>> token = new TypeToken<>() {
            };
            this.allMessages = dp.get("all_messages", token.getType());
        }
        if (dp.has("message")) {
            Message mes = dp.get("message", Message.class);
            addMessage(mes);
        }
        if (dp.has("user")) {
            User newUser = dp.get("user", User.class);

            if (!allMessages.containsKey(newUser)) {
                allMessages.put(newUser, new ArrayList<>());
            }
        }

        while (controller == null) {
            Thread.onSpinWait();
        }

        if (response.getData().isFailState()) {
            Platform.runLater(() ->
                    controller.showError(response.getData().get("ex_message", String.class))
            );
        } else {
            controller.receiveData(response);
        }
    }

    @Override
    public void init() {
        try {
            Socket clientSocket = new Socket(address, port);
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            Thread serverReader = new ServerReader(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())), this);
            serverReader.start();
        } catch (IOException ex) {
            System.err.println("Server is offline.");
            Platform.exit();
        }

    }

    public void loadFxml(String stringPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(stringPath));
        Scene scene = new Scene(loader.load());
        controller = loader.getController();
        controller.setApplication(this);
        controller.updateAllChats();
        scene.setFill(Color.TRANSPARENT);
        Platform.runLater(() -> stage.setScene(scene));
    }


}
