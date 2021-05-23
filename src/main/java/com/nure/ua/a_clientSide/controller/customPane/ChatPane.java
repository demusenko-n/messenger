package com.nure.ua.a_clientSide.controller.customPane;

import com.nure.ua.a_serverSide.model.entity.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ChatPane extends AnchorPane {
    static final public int sizeX = 100;
    static final public int sizeY = 30;

    private static final PseudoClass CHOSEN_PSEUDO_CLASS = PseudoClass.getPseudoClass("chosen");

    private final BooleanProperty isActive;
    private final User user;

    public void setActive(boolean active) {
        this.isActive.set(active);
    }

    public boolean isActive() {
        return isActive.get();
    }

    public BooleanProperty isActiveProperty() {
        return isActive;
    }

    public User getUser() {
        return user;
    }

    public ChatPane(User user) {
        isActive = new SimpleBooleanProperty(false);
        isActive.addListener(e -> pseudoClassStateChanged(CHOSEN_PSEUDO_CLASS, isActive.get()));
        getStyleClass().add("chatBox");


        this.user = user;

        Label label = new Label();
        this.getChildren().add(label);

        label.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 0.0);
        label.setAlignment(Pos.CENTER);

        label.setText(user.getPassword());

        setMinSize(sizeX, sizeY);

    }
}

