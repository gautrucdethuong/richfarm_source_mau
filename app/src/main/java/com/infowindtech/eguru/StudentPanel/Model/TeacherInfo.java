package com.infowindtech.eguru.StudentPanel.Model;


public class TeacherInfo {
    private String attribute;
    private String value;

    public TeacherInfo(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
