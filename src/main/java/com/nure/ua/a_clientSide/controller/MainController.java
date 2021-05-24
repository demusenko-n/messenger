package com.nure.ua.a_clientSide.controller;

import com.nure.ua.a_clientSide.controller.customPane.ChatPane;
import com.nure.ua.a_clientSide.controller.customPane.MessageBox;
import com.nure.ua.a_serverSide.model.entity.Message;
import com.nure.ua.a_serverSide.model.entity.User;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.exchangeData.dataPack.DataPack;
import com.nure.ua.exchangeData.dataPack.DataPackImpl;
import com.nure.ua.exchangeData.response.Response;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

public class MainController extends Controller {
    @FXML
    private TextField messageTextField;

    @Override
    public void receiveData(Response response) {
        application.setSession(response.getSession());

        DataPack dp = response.getData();
        if (dp.isFailState()) {
            Platform.runLater(() -> {
                System.err.println("error occurred");
            });
        } else if (dp.getCommand().equals("newmessage")) {

            Message mes = dp.get("message", Message.class);

            application.addMessage(mes);
            activeChat.ifPresent(chat -> Platform.runLater(() -> setActiveChat(chat.getUser())));
        } else {
            System.err.println("unknown command: " + dp.getCommand());
        }
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

        for (Entry<User, List<Message>> elem : application.getAllMessages().entrySet()) {

            ChatPane dp = new ChatPane(elem.getKey());

            chatsList.add(dp);
            chatsVbox.getChildren().add(dp);

            dp.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMouseClicked);
        }
    }

    @FXML
    private void onMouseClicked(MouseEvent mouseEvent) {
        ChatPane source = (ChatPane) mouseEvent.getSource();

        activeChat.ifPresent((x) -> x.setActive(false));

        activeChat = Optional.ofNullable(source);

        activeChat.ifPresent((x) -> {
            x.setActive(true);
            setActiveChat(x.getUser());
        });
    }

    private void setActiveChat(User user) {
        messagesVbox.getChildren().clear();

        if (user == null) {
            return;
        }

        List<Message> list = application.getAllMessages().get(user);

        for (Message mes : list) {
            Pos alignment;
            if (application.getSession().getUser().getId() == mes.getSender().getId()) {
                alignment = Pos.TOP_RIGHT;
            } else {
                alignment = Pos.TOP_LEFT;
            }
            MessageBox mp = new MessageBox(mes, alignment);

            messagesVbox.getChildren().add(mp);
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        messagesVbox.setSpacing(15.0);
        chatsScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        messagesScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        chatsScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        messagesScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);

        messagesScrollPane.setFitToHeight(false);

        messagesVbox.heightProperty().addListener(observable -> messagesScrollPane.setVvalue(1D));
    }

    @FXML
    private void onMessageTextFieldAction(ActionEvent actionEvent) {

        String content = messageTextField.getText();
        if (activeChat.isEmpty() || content.isBlank()) {
            return;
        }

        messageTextField.clear();
        User sender = application.getSession().getUser();
        User receiver = activeChat.get().getUser();

        DataPackImpl dp = new DataPackImpl();
        dp.setCommand("send_message");

        dp.put("sender", sender.getId());
        dp.put("receiver", receiver.getId());
        dp.put("quote", null);
        dp.put("content", content);

        sendToServer(new Request(dp));
    }
}