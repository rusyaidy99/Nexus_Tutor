package com.example.nexustutor;

public class tutor_register {

    public String fullname, email, gender, acctype, uid;

    public tutor_register() {
    }

    public tutor_register(String fullname, String email, String gender, String acctype, String uid) {
        this.fullname = fullname;
        this.email = email;
        this.gender = gender;
        this.acctype = acctype;
        this.uid = uid;
    }

    public tutor_register(String fullname, String email, String gender, String acctype) {
        this.fullname = fullname;
        this.email = email;
        this.gender = gender;
        this.acctype = acctype;

    }

}
