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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class MainController extends Controller {
    @FXML
    private Label errorLabel;
    @FXML
    private Label loginInfoLabel;
    @FXML
    private Label nameInfoLabel;
    @FXML
    private VBox chatsVBox;
    @FXML
    private TextField addUserTextField;
    @FXML
    private ImageView addUserButton;
    @FXML
    private ImageView sendMessageButton;
    @FXML
    private TextField messageTextField;
    @FXML
    private ScrollPane chatsScrollPane;
    @FXML
    private ScrollPane messagesScrollPane;
    @FXML
    private GridPane messagesGridPane;

    private final List<ChatPane> chatsList;

    private ChatPane activeChat = null;

    void resetActiveChat() {
        if (activeChat != null) {
            activeChat.setActive(false);
            activeChat = null;
        }
    }

    @Override
    public void showError(String str) {
        super.showError(str);
        errorLabel.setVisible(true);
        errorLabel.setText(str);
    }

    @Override
    public void receiveData(Response response) {
        DataPack dp = response.getData();

        if (dp.has("user")) {

            Platform.runLater(() -> {
                ChatPane chat = null;


                User user = dp.get("user", User.class);
                boolean foundChat = false;
                for (ChatPane pane : chatsList) {
                    if (pane.getUser().getId() == user.getId()) {
                        foundChat = true;
                        chat = pane;
                        break;
                    }
                }

                if (!foundChat) {
                    chat = new ChatPane(user, "");

                    chatsList.add(chat);
                    chatsVBox.getChildren().add(chat);
                    chat.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMouseClicked);
                }
                setActiveChat(chat);

            });

        }

        if (dp.has("message")) {
            Platform.runLater(() -> {
                Message msg = dp.get("message", Message.class);
                User otherUser = msg.getOtherUser(application.getSession().getUser());

                boolean isFound = false;
                for (ChatPane pane : chatsList) {
                    if (pane.getUser().getId() == otherUser.getId()) {
                        Platform.runLater(() -> pane.setLastMessage(msg.getContent()));
                        isFound = true;
                    }
                }

                if (!isFound) {
                    ChatPane newChat = new ChatPane(otherUser, msg.getContent());

                    chatsList.add(newChat);
                    chatsVBox.getChildren().add(newChat);

                    newChat.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMouseClicked);
                }

                if (activeChat != null && activeChat.getUser().getId() == otherUser.getId()) {
                    Platform.runLater(() -> setActiveChat(activeChat));
                }
            });
        }
    }

    public MainController() {
        this.chatsList = new ArrayList<>();
    }


    @Override
    public void updateAllChats() {
        chatsList.clear();
        chatsVBox.getChildren().clear();

        activeChat = null;

        for (Entry<User, List<Message>> elem : application.getAllMessages().entrySet()) {
            Message lastMessage = elem.getValue().get(elem.getValue().size() - 1);
            ChatPane dp = new ChatPane(elem.getKey(), lastMessage.getContent());

            chatsList.add(dp);
            chatsVBox.getChildren().add(dp);

            dp.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMouseClicked);
        }
    }

    @FXML
    private void onMouseClicked(MouseEvent mouseEvent) {
        ChatPane source = (ChatPane) mouseEvent.getSource();
        if (source == null || source == activeChat) {
            return;
        }
        setActiveChat(source);
    }

    void setActiveChat(ChatPane chat) {
        if (activeChat != null) {
            activeChat.setActive(false);
        }
        chat.setActive(true);

        activeChat = chat;

        User activeUser = activeChat.getUser();
        loginInfoLabel.setText(activeUser.getLogin());
        nameInfoLabel.setText(activeUser.getName());
        List<Message> list = application.getAllMessages().get(activeUser);

        messagesGridPane.getChildren().clear();
        messagesGridPane.getRowConstraints().clear();

        for (Message mes : list) {
            Pos alignment;

            if (application.getSession().getUser().getId() == mes.getSender().getId()) {
                alignment = Pos.CENTER_RIGHT;
            } else {
                alignment = Pos.CENTER_LEFT;
            }

            MessageBox left = new MessageBox(mes, Pos.CENTER_LEFT);
            GridPane.setColumnIndex(left, 0);
            GridPane.setHalignment(left, HPos.LEFT);

            MessageBox right = new MessageBox(mes, Pos.CENTER_RIGHT);

            GridPane.setColumnIndex(right, 1);
            GridPane.setHalignment(right, HPos.RIGHT);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.ALWAYS);

            messagesGridPane.addRow(messagesGridPane.getRowCount(), left, right);

            if (alignment == Pos.CENTER_RIGHT) {
                left.setVisible(false);
            } else {
                right.setVisible(false);
            }

            messagesGridPane.getRowConstraints().add(rowConstraints);
        }
    }

    @Override
    public void initialize() {
        super.initialize();

        Platform.runLater(() -> {
            messagesGridPane.heightProperty().addListener(observable -> messagesScrollPane.setVvalue(1D));
            messageTextField.addEventHandler(ActionEvent.ACTION, this::onAttemptToSendMessage);
            sendMessageButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onAttemptToSendMessage);

            addUserTextField.addEventHandler(ActionEvent.ACTION, this::onAttemptToAddUser);
            addUserButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onAttemptToAddUser);

            errorLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, ev->errorLabel.setVisible(false));
            addUserTextField.setOnKeyPressed(keyEvent -> errorLabel.setVisible(false));
        });
    }

    @FXML
    private void onAttemptToSendMessage(Event ev) {
        String content = messageTextField.getText();
        if (activeChat == null || content.isBlank()) {
            return;
        }

        messageTextField.clear();
        User sender = application.getSession().getUser();
        User receiver = activeChat.getUser();

        DataPackImpl dp = new DataPackImpl();
        dp.setCommand("send_message");

        dp.put("sender", sender.getId());
        dp.put("receiver", receiver.getId());
        dp.put("quote", null);
        dp.put("content", content);

        sendToServer(new Request(dp));
    }

    @FXML
    private void onAttemptToAddUser(Event ev) {
        String content = addUserTextField.getText();
        if (content.isBlank()) {
            return;
        }
        addUserTextField.clear();

        DataPackImpl dp = new DataPackImpl();
        dp.setCommand("find_user");

        dp.put("login", content);

        sendToServer(new Request(dp));
    }
}