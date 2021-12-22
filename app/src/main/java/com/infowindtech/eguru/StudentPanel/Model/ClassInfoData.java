package com.infowindtech.eguru.StudentPanel.Model;



public class ClassInfoData {
   private String subject;
   private String username;
    private String date;
    private String time;
    private int image;
    private String id;
    private String user_pic;
    private String appointid;
    private String teacher_id;
    private String status;
    private String ratingstauts;
    private String teacher_quality;
    private String payStatus;
    private String is_online;
    private String room_id;
    private String past_date;

    public ClassInfoData(){
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getAppointid() {
        return appointid;
    }

    public void setAppointid(String appointid) {
        this.appointid = appointid;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRatingstauts() {
        return ratingstauts;
    }

    public void setRatingstauts(String ratingstauts) {
        this.ratingstauts = ratingstauts;
    }

    public String getTeacher_quality() {
        return teacher_quality;
    }

    public void setTeacher_quality(String teacher_quality) {
        this.teacher_quality = teacher_quality;
    }

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getPast_date() {
        return past_date;
    }

    public void setPast_date(String past_date) {
        this.past_date = past_date;
    }
}
