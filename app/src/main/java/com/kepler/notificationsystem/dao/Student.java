package com.kepler.notificationsystem.dao;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Amit on 08-04-2017.
 */

public class Student {
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String emailid;
    @Expose
    private String password;
    @Expose
    private String rn;
    @Expose
    private String cn;
    @Expose
    private String batch;
    @Expose
    private String device_id;

    public Student(String name, String emailid, String password, String rn, String cn, String batch) {
        this.name = name;
        this.emailid = emailid;
        this.password = password;
        this.rn = rn;
        this.cn = cn;
        this.batch = batch;
    }

    public Student (){

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmailid() {
        return emailid;
    }

    public String getRn() {
        return rn;
    }

    public String getCn() {
        return cn;
    }

    public String getBatch() {
        return batch;
    }

    public String getDevice_id() {
        return device_id;
    }


}
