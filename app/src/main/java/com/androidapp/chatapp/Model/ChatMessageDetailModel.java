package com.androidapp.chatapp.Model;

import java.io.Serializable;

public class ChatMessageDetailModel implements Serializable {


    // Model class has been defined to Chat message list
    private String chatId="";
    private String chatUserName="";
    private String chatMessage="";
    private String chatTime="";
    private String chatMessageType="";
    private String chatUserType="";
    private String chatImageURL="";
    private int chatImageID ;
    private int mRecipientOrSenderStatus;

    public ChatMessageDetailModel(String chatId, String chatUserName, String chatMessage,
                                  String chatTime, String chatMessageType, String chatUserType,
                                  String chatImageURL, int chatImageID) {
        this.chatId = chatId;
        this.chatUserName = chatUserName;
        this.chatMessage = chatMessage;
        this.chatTime = chatTime;
        this.chatMessageType = chatMessageType;
        this.chatUserType = chatUserType;
        this.chatImageURL = chatImageURL;
        this.chatImageID = chatImageID;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatUserName() {
        return chatUserName;
    }

    public void setChatUserName(String chatUserName) {
        this.chatUserName = chatUserName;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }

    public String getChatMessageType() {
        return chatMessageType;
    }

    public void setChatMessageType(String chatMessageType) {
        this.chatMessageType = chatMessageType;
    }

    public String getChatUserType() {
        return chatUserType;
    }

    public void setChatUserType(String chatUserType) {
        this.chatUserType = chatUserType;
    }

    public String getChatImageURL() {
        return chatImageURL;
    }

    public void setChatImageURL(String chatImageURL) {
        this.chatImageURL = chatImageURL;
    }

    public Integer getChatImageID() {
        return chatImageID;
    }

    public void setChatImageID(int chatImageID) {
        this.chatImageID = chatImageID;
    }

    public void setRecipientOrSenderStatus(int recipientOrSenderStatus) {
        this.mRecipientOrSenderStatus = recipientOrSenderStatus;
    }

    public int getRecipientOrSenderStatus() {
        return mRecipientOrSenderStatus;
    }



}
