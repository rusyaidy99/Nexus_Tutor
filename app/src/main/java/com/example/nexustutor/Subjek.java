package com.example.nexustutor;

public class Subjek {
    private String mySubject;
    private String uid;

    public Subjek() {
    }

    public Subjek(String mySubject) {
        this.mySubject = mySubject;
    }

    public Subjek(String mySubject, String uid) {
        this.mySubject = mySubject;
        this.uid = uid;
    }

    public String getMySubject() {
        return mySubject;
    }

    public void setMySubject(String mySubject) {
        this.mySubject = mySubject;
    }

    public String getStatus() {
        return uid;
    }

    public void setStatus(String status) {
        this.uid = status;
    }
}
