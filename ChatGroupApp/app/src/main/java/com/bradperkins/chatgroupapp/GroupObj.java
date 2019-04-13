package com.bradperkins.chatgroupapp;


public class GroupObj {

    private String title;
    private String chatting;


    public GroupObj(String title, String chatting) {
        this.title = title;
        this.chatting = chatting;
    }

    public GroupObj() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChatting() {
        return chatting;
    }

    public void setChatting(String chatting) {
        this.chatting = chatting;
    }
}
