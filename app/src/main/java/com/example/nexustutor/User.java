package com.example.nexustutor;

public class User {

    public String fullname, email, gender, acctype, phone, image;

    public User(){

    }

    public User(String fullname, String email, String gender, String acctype){
        this.fullname = fullname;
        this.email = email;
        this.gender = gender;
        this.acctype = acctype;

    }

    public User(String fullname, String email, String gender, String acctype, String phone) {
        this.fullname = fullname;
        this.email = email;
        this.gender = gender;
        this.acctype = acctype;
        this.phone = phone;
    }

    public User(String fullname, String email, String gender, String acctype, String phone, String image) {
        this.fullname = fullname;
        this.email = email;
        this.gender = gender;
        this.acctype = acctype;
        this.phone = phone;
        this.image = image;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAcctype() {
        return acctype;
    }

    public void setAcctype(String acctype) {
        this.acctype = acctype;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

