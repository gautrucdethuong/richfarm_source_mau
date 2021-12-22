package com.infowindtech.eguru.StudentPanel.Model;

import java.util.ArrayList;

public class DrAppointmenttime {
    private String bgroup;
    private String booktime;
    private ArrayList<String> arrStr;

    public DrAppointmenttime(String bgroup, String booktime, ArrayList<String> arrStr) {
        super();
        this.bgroup = bgroup;
        this.booktime=booktime;
        this.arrStr=arrStr;
    }

    public DrAppointmenttime() {
    }

    public String getBgroup() {
        return bgroup;
    }

    public void setBgroup(String bgroup) {
        this.bgroup = bgroup;
    }

    public String getBooktime() {
        return booktime;
    }

    public void setBooktime(String booktime) {
        this.booktime = booktime;
    }

    public ArrayList<String> getArrStr() {
        return arrStr;
    }

    public void setArrStr(ArrayList<String> arrStr) {
        this.arrStr = arrStr;
    }
}
