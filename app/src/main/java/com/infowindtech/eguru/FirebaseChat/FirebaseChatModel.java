package com.infowindtech.eguru.FirebaseChat;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class FirebaseChatModel {
    private String receiver_id ,sender_name, sender_id ,receiver_name, message, date, time, status;

    public Map<String, Boolean> stars = new HashMap<>();

    public FirebaseChatModel() {

    }

    public FirebaseChatModel(String receiver_id , String receiver_name, String msg) {

        this.receiver_id = receiver_id;

        this.receiver_name = receiver_name;
        message = msg;


    }

    public FirebaseChatModel(String receiver_id ,String receiver_name, String sender_name, String sender_id, String msg, String date, String time ,String status) {

        this.receiver_id = receiver_id;
        this.sender_name = sender_name;
        this.receiver_name = receiver_name;
        this.sender_id = sender_id;
        message = msg;
        this.date = date;
        this.time = time;
        this.status = status;

    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }



    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("receiver_id", receiver_id);
        result.put("receiver_name", receiver_name);
        result.put("sender_id", sender_id);
        result.put("sender_name", sender_name);
        result.put("message", message);
        result.put("date", date);
        result.put("time", time);
        result.put("status",status);


        return result;
    }

}