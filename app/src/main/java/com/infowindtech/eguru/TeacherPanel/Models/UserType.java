package com.infowindtech.eguru.TeacherPanel.Models;


public class UserType {
    private String type;
    private String id;
    boolean selected = false;
    private String mainsubj;


    public UserType(String type, String id,boolean selected) {
        super();
        this.type = type;
        this.id = id;
        this.selected = selected;
    }

    public UserType() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getMainsubj() {
        return mainsubj;
    }

    public void setMainsubj(String mainsubj) {
        this.mainsubj = mainsubj;
    }
}
