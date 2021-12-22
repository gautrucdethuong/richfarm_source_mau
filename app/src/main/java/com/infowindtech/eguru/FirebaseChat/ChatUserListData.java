package com.infowindtech.eguru.FirebaseChat;


public class ChatUserListData {
    private String userfromid;
    private String name;
    private String profile;
    private String usertoid;
    private int unreadmsg;
    private String username;
    private String chatId ;



    public ChatUserListData() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserfromid() {
        return userfromid;
    }

    public void setUserfromid(String userfromid) {
        this.userfromid = userfromid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUsertoid() {
        return usertoid;
    }

    public void setUsertoid(String usertoid) {
        this.usertoid = usertoid;
    }

    public int getUnreadmsg() {
        return unreadmsg;
    }

    public void setUnreadmsg(int unreadmsg) {
        this.unreadmsg = unreadmsg;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
