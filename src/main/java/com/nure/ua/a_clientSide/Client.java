package com.nure.ua.a_clientSide.application;

import com.nure.ua.a_clientSide.ServerReader;
import com.nure.ua.controller.Controller;
import com.nure.ua.exchangeData.Response;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Application {
    private PrintWriter writerToServer;
    private FXMLLoader loader;

    private static final String address = "localhost";
    private static final int port = 4004;

    public void receiveData(Response response) {

        while (loader == null || loader.getController() == null) {
            Thread.onSpinWait();
        }

        loader.<Controller>getController().receiveData(response);
    }


    @Override
    public void start(Stage stage) throws IOException {
        stage.initStyle(StageStyle.TRANSPARENT);
        loader = new FXMLLoader(getClass().getResource("/static/auth.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.show();

        Controller controller = loader.getController();
        controller.setWriter(writerToServer);
    }

    @Override
    public void init() throws IOException {
        Socket clientSocket = new Socket(address, port);
        writerToServer = new PrintWriter(clientSocket.getOutputStream(), true);
        Thread serverReader = new ServerReader(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())), this);
        serverReader.start();
    }

    public static void main(String[] args) {
        launch();
    }

}
