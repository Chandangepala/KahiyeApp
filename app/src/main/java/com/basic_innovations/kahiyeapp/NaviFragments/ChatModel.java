package com.basic_innovations.kahiyeapp.NaviFragments;

public class ChatModel {
    String chatName;
    String chatLastmsg;
    String chatTime;
    String chatUnread;
    String chatUserID;
    int chatImg;

    public ChatModel() {
    }

    public ChatModel(String chatName, String chatLastmsg, String chatTime, String chatUnread, String chatUserID, int chatImg) {
        this.chatName = chatName;
        this.chatLastmsg = chatLastmsg;
        this.chatTime = chatTime;
        this.chatUnread = chatUnread;
        this.chatUserID = chatUserID;
        this.chatImg = chatImg;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatLastmsg() {
        return chatLastmsg;
    }

    public void setChatLastmsg(String chatLastmsg) {
        this.chatLastmsg = chatLastmsg;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }

    public String getChatUnread() {
        return chatUnread;
    }

    public void setChatUnread(String chatUnread) {
        this.chatUnread = chatUnread;
    }

    public String getChatUserID() {
        return chatUserID;
    }

    public void setChatUserID(String chatUserID) {
        this.chatUserID = chatUserID;
    }

    public int getChatImg() {
        return chatImg;
    }

    public void setChatImg(int chatImg) {
        this.chatImg = chatImg;
    }
}
