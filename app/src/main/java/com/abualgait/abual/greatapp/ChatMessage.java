package com.abualgait.abual.greatapp;

import android.net.Uri;

import java.util.Date;

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private String imgurl;
    private long messageTime;

    public ChatMessage(String messageText, String messageUser, String imgurl) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.imgurl = imgurl;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage() {

    }

    public String getMessageImgurl() {
        return imgurl;
    }

    public void setMessageImgurl(String imgurl) {
        this.imgurl = imgurl;
    }


    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}