package com.infowindtech.eguru.StudentPanel.Model;


import com.infowindtech.eguru.TeacherPanel.Models.UserType;

import java.util.ArrayList;

public class ExpandedSubjectData {
    private String name;
    private ArrayList<UserType> childList = new ArrayList<UserType>();
    private String id;
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<UserType> getChildList() {
        return childList;
    }

    public void setChildList(ArrayList<UserType> childList) {
        this.childList = childList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
