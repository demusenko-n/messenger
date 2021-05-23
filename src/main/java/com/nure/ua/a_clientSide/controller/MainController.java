package com.nure.ua.a_clientSide.controller;

import com.nure.ua.a_clientSide.controller.customPane.ChatPane;
import com.nure.ua.a_serverSide.model.entity.Message;
import com.nure.ua.a_serverSide.model.entity.User;
import com.nure.ua.exchangeData.response.Response;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

public class MainController extends Controller {
    @Override
    public void receiveData(Response response) throws IOException {

    }

    public MainController() {
        this.chatsList = new ArrayList<>();
    }

    @FXML
    private AnchorPane windowNoTool;

    @FXML
    private Line line;

    @FXML
    private ScrollPane chatsScrollPane;

    @FXML
    private VBox chatsVbox;

    @FXML
    private ScrollPane messagesScrollPane;

    @FXML
    private VBox messagesVbox;

    private final List<ChatPane> chatsList;

    Optional<ChatPane> activeChat = Optional.empty();

    @Override
    public void updateAllChats() {
        chatsList.clear();
        chatsVbox.getChildren().clear();
        int sizeY = 40;
        int sizeX = 100;
        int i = 0;

        for (Entry<User, List<Message>> elem : application.getAllMessages().entrySet()) {

            User user = elem.getKey();
            ChatPane dp = new ChatPane(user);

//            dp.setLayoutY(15 + i * sizeY);
//            dp.setLayoutX(15);


            chatsList.add(dp);
            chatsVbox.getChildren().add(dp);

            dp.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMouseClicked);

            i++;
        }
    }

    @FXML
    private void onMouseClicked(MouseEvent mouseEvent) {
        ChatPane source = (ChatPane) mouseEvent.getSource();

        activeChat.ifPresent((x) -> x.setActive(false));

        activeChat = Optional.ofNullable(source);

        activeChat.ifPresent((x) -> {
            x.setActive(false);
            setActiveChat(x.getUser());
        });
    }

    private void setActiveChat(User user) {
        messagesVbox.getChildren().clear();
        if (user == null) {
            return;
        }

        List<Message> list = application.getAllMessages().get(user);

        int i = 0;
        int sizeY = 40;
        int sizeX = 100;

        for (Message mes : list) {

            Label label = new Label();
            AnchorPane ap = new AnchorPane(label);

            label.setMaxWidth(Double.MAX_VALUE);
            AnchorPane.setLeftAnchor(label, 0.0);
            AnchorPane.setRightAnchor(label, 0.0);


            label.setAlignment(Pos.CENTER);

            label.setText(mes.getContent());

            ap.setLayoutY(15 + i * sizeY);
            ap.setLayoutX(15);
            ap.setMinSize(sizeX, sizeY);
            ap.setStyle("-fx-background-color: #ffffff");

            messagesVbox.getChildren().add(ap);

            ap.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMouseClicked);

            i++;
        }

    }


    @Override
    public void initialize() {
        super.initialize();

        chatsScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        messagesScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        chatsScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        messagesScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);

        //chatsScrollPane.setFocusTraversable(false);
        //messagesScrollPane.setFocusTraversable(false);

        //chatsScrollPane.

        //chatsVbox.setSpacing(10);
    }
}