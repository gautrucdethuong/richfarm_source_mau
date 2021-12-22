package com.infowindtech.eguru.StudentPanel.Model;



public class SubjectData {
    private String type;
    private String id;
    private String image;

    public SubjectData(String type, String id, String image) {
        this.type = type;
        this.id = id;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
