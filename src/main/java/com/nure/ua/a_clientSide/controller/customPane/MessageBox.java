package com.nure.ua.a_clientSide.controller.customPane;


import com.nure.ua.a_serverSide.model.entity.Message;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;

public class MessageBox extends VBox {

    static final public int sizeX = 300;

    private final Message message;

    private final Label labelContent;
    private final Label labelDate;


    private final HBox hBoxContent;
    private final HBox hBoxDate;




    public Message getMessage() {
        return message;
    }

    public MessageBox(Message message, Pos alignment) {
        maxHeight(Double.MAX_VALUE);
        getStyleClass().add("messageBox");
        this.message = message;

        hBoxContent = new HBox();
        hBoxDate = new HBox();

        hBoxContent.maxHeight(Double.MAX_VALUE);
        hBoxDate.maxHeight(Double.MAX_VALUE);




        labelContent = new Label();
        hBoxContent.getChildren().add(labelContent);
        labelContent.setWrapText(true);
        labelContent.setText(message.getContent());
        labelContent.maxHeight(Double.MAX_VALUE);
        hBoxContent.setAlignment(alignment);

        maxWidth(sizeX);
        labelContent.setMaxWidth(sizeX);


        labelDate = new Label();
        hBoxDate.getChildren().add(labelDate);
        labelDate.setText(message.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        hBoxDate.setAlignment(alignment);


        getChildren().add(hBoxContent);
        getChildren().add(hBoxDate);


    }
}
