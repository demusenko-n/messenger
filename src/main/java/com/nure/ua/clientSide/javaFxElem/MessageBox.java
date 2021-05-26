package com.nure.ua.clientSide.javaFxElem;


import com.nure.ua.entity.Message;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;

public class MessageBox extends VBox {
    private final Message message;

    public Message getMessage() {
        return message;
    }

    public MessageBox(Message message, Pos alignment) {
        HPos pos;
        int colIndex;
        if (alignment == Pos.CENTER_LEFT) {
            getStyleClass().add("message-h-box-left");
            pos = HPos.LEFT;
            colIndex = 0;
        } else {
            getStyleClass().add("message-h-box-right");
            pos = HPos.RIGHT;
            colIndex = 1;
        }


        setSpacing(10.0);
        setAlignment(alignment);

        GridPane.setHalignment(this, HPos.RIGHT);
        GridPane.setValignment(this, VPos.CENTER);
        GridPane.setHgrow(this, Priority.NEVER);
        GridPane.setVgrow(this, Priority.ALWAYS);
        GridPane.setMargin(this, new Insets(10, 10, 0, 10));
        GridPane.setColumnIndex(this, colIndex);
        GridPane.setColumnSpan(this, 2);
        GridPane.setRowSpan(this, 1);
        setFillWidth(true);

        setMinWidth(USE_COMPUTED_SIZE);
        setMaxWidth(USE_COMPUTED_SIZE);
        setPrefWidth(USE_COMPUTED_SIZE);
        setMinHeight(USE_COMPUTED_SIZE);
        setMaxHeight(USE_COMPUTED_SIZE);
        setPrefHeight(USE_COMPUTED_SIZE);

        setPadding(new Insets(10));
        this.message = message;

        Label labelContent = new Label(message.getContent());
        Label labelDate = new Label(message.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss")));


        labelContent.setWrapText(true);
        labelDate.setWrapText(true);

        getChildren().addAll(labelContent, labelDate);
    }
}
