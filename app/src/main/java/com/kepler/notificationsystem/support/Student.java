package com.kepler.notificationsystem.support;

/**
 * Created by Amit on 03-04-2017.
 */

public class Student {
    private String name;
    private String cn;
    private String rn;
    private String username;
    private String password;
    private String batch;

    public Student() {
    }

    public Student(String name, String username, String password, String rn, String cn, String batch) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.rn = rn;
        this.cn = cn;
        this.batch = batch;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getCn() {
        return cn;
    }

    public String getRn() {
        return rn;
    }

    public String getBatch() {
        return batch;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }
}
