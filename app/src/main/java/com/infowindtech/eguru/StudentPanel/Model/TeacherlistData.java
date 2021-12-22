package com.infowindtech.eguru.StudentPanel.Model;



public class TeacherlistData {
    private String userid;
    private String tname;
    private String tusername;
    private String temail;
    private String tmobile;
    private String timage;
    private String tfee;
    private String trating;
    private Float rating;
    private String tagline;
    private String teacher_quality;

    public TeacherlistData(String userid, String tusername ,String tname, String temail, String tmobile, String timage, String tfee,String trating,String tagline,String teacher_quality) {
        this.userid = userid;
        this.tusername = tusername;
        this.tname = tname;
        this.temail = temail;
        this.tmobile = tmobile;
        this.timage = timage;
        this.tfee = tfee;
       this.trating=trating;
        this.tagline=tagline;
        this.teacher_quality=teacher_quality;
    }


    public String getTusername() {
        return tusername;
    }

    public void setTusername(String tusername) {
        this.tusername = tusername;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTemail() {
        return temail;
    }

    public void setTemail(String temail) {
        this.temail = temail;
    }

    public String getTmobile() {
        return tmobile;
    }

    public void setTmobile(String tmobile) {
        this.tmobile = tmobile;
    }

    public String getTimage() {
        return timage;
    }

    public void setTimage(String timage) {
        this.timage = timage;
    }

    public String getTfee() {
        return tfee;
    }

    public void setTfee(String tfee) {
        this.tfee = tfee;
    }

    public String getTrating() {
        return trating;
    }

    public void setTrating(String trating) {
        this.trating = trating;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTeacher_quality() {
        return teacher_quality;
    }

    public void setTeacher_quality(String teacher_quality) {
        this.teacher_quality = teacher_quality;
    }
}
