package com.example.nexustutor;

public class Session {
    private String sessionID, uid, tid, status, date, time;

    public Session() {
    }

    public Session(String sessionID, String uid, String tid, String status, String date, String time) {
        this.sessionID = sessionID;
        this.uid = uid;
        this.tid = tid;
        this.status = status;
        this.date = date;
        this.time = time;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
