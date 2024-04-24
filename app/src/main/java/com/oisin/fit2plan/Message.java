package com.oisin.fit2plan;

public class Message {
    private String sender;
    private String msgReciever;
    private String content;

    public Message() {

    }

    public Message(String sender, String msgReciever, String content) {
        this.sender = sender;
        this.msgReciever = msgReciever;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsgReciever() {
        return msgReciever;
    }

    public void setMsgReciever(String msgReciever) {
        this.msgReciever = msgReciever;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
