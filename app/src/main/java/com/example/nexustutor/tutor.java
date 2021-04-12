package com.example.nexustutor;

public class tutor {
    private String fullname;
    private String email;
    private String acctype;
    private String gender;
    private String image;
    /*private int Thumbnail;*/

    public tutor() {
    }

    public tutor(String fullname, String email, String acctype, String gender, String imageUrl) {
        this.fullname = fullname;
        this.email = email;
        this.acctype = acctype;
        this.gender = gender;
        this.image = imageUrl;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getAcctype() {
        return acctype;
    }

    public String getGender() {
        return gender;
    }

    public String getImage() {
        return image;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAcctype(String acctype) {
        this.acctype = acctype;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
