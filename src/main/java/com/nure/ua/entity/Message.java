package com.nure.ua.entity;
import java.time.LocalDateTime;

public class Message extends Entity {
    private final Integer quoteMessageId;
    private final User sender;
    private final User receiver;
    private String content;
    private LocalDateTime time;
    private int isUnread;
    private int isEdited;

    public Integer getQuoteMessageId() {
        return quoteMessageId;
    }

    public int isUnread() {
        return isUnread;
    }

    public void setUnread(int unread) {
        isUnread = unread;
    }

    public int isEdited() {
        return isEdited;
    }

    public void setEdited(int edited) {
        isEdited = edited;
    }

    public Message(int id, 
                   Integer quoteMessageId,
                   String content,
                   User sender, 
                   User receiver,
                   int isUnread,
                   int isEdited,
                   LocalDateTime time) {
        super(id);
        this.quoteMessageId = quoteMessageId;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.time = time;
        this.isEdited = isEdited;
        this.isUnread = isUnread;
    }

    public Message(Integer quoteMessageId,
                   String content,
                   User sender,
                   User receiver,
                   int isUnread,
                   int isEdited,
                   LocalDateTime time) {
        this(-1, quoteMessageId, content, sender, receiver,  isUnread, isEdited, time);
    }

    public Message(Integer quoteMessageId,
                   String content,
                   User sender, 
                   User receiver,
                   int isUnread,
                   int isEdited) {
        this(-1, quoteMessageId, content, sender, receiver,  isUnread, isEdited, LocalDateTime.now());
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public User getOtherUser(User user){
        return user.getId() == receiver.getId() ? sender : receiver;
    }

    @Override
    public String toString() {
        return "Message{" +
                "quoteMessageId=" + quoteMessageId +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", isUnread=" + isUnread +
                ", isEdited=" + isEdited +
                '}';
    }


}
