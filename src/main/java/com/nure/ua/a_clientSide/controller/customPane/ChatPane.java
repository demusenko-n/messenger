package com.nure.ua.a_clientSide.controller.customPane;

import com.nure.ua.a_serverSide.model.entity.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ChatPane extends AnchorPane {
    static final public int sizeX = 100;
    static final public int sizeY = 50;

    private static final PseudoClass CHOSEN_PSEUDO_CLASS = PseudoClass.getPseudoClass("chosen");

    private final BooleanProperty isActive;
    private final User user;

    public void setActive(boolean active) {
        this.isActive.set(active);
    }

    public User getUser() {
        return user;
    }

    private final Label labelLastMessage;

    public void setLastMessage(String lastMessage) {
        labelLastMessage.setText(lastMessage);
    }

    public ChatPane(User user, String lastMsgContent) {
        isActive = new SimpleBooleanProperty(false);
        isActive.addListener(e -> pseudoClassStateChanged(CHOSEN_PSEUDO_CLASS, isActive.get()));
        getStyleClass().add("chat-box");
        this.user = user;
        ImageView img = new ImageView();
        img.getStyleClass().add("avatar");
        img.setFitHeight(46);
        img.setFitWidth(46);

        HBox.setMargin(img, new Insets(2));

        Label labelUserName = new Label(user.getName());
        labelUserName.setFont(new Font(18));
        labelLastMessage = new Label(lastMsgContent);
        labelLastMessage.setFont(new Font(12));

        VBox vBox = new VBox(labelUserName, labelLastMessage);
        vBox.setPadding(new Insets(0,5,0,0));
        HBox hBox = new HBox(img, vBox);


        AnchorPane.setTopAnchor(hBox, 0d);
        AnchorPane.setLeftAnchor(hBox, 0d);
        AnchorPane.setRightAnchor(hBox, 0d);
        AnchorPane.setBottomAnchor(hBox, 0d);

        getChildren().add(hBox);
    }
}

