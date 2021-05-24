package com.example.nexustutor;

public class Offer {
    private String oid ,uid, title, description, locationCity, locationState, subject, gender;
    private int Thumbnail;

    public Offer() {
    }

  /*  Offer offer = new Offer(uid, title, description, city, state, subject, gender);*/
    public Offer(String oid, String uid, String title, String description, String locationCity, String locationState, String subject, String gender, int thumbnail) {
        this.oid = oid;
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.locationCity = locationCity;
        this.locationState = locationState;
        this.subject = subject;
        this.gender = gender;
        this.Thumbnail = thumbnail;
    }

    public Offer(String title, String description, String locationCity, String locationState, String subject, String gender, int thumbnail) {
        this.title = title;
        this.description = description;
        this.locationCity = locationCity;
        this.locationState = locationState;
        this.subject = subject;
        this.gender = gender;
        this.Thumbnail = thumbnail;
    }
    public Offer(String oid, String uid, String title, String description, String locationCity, String locationState, String subject, String gender) {
        this.oid = oid;
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.locationCity = locationCity;
        this.locationState = locationState;
        this.subject = subject;
        this.gender = gender;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getLocationState() {
        return locationState;
    }

    public void setLocationState(String locationState) {
        this.locationState = locationState;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}
