package com.kepler.notificationsystem.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Amit on 08-04-2017.
 */

public class Student implements Parcelable {
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
    @Expose
    private String img;

    public Student(String name, String emailid, String password, String rn, String cn, String batch, String img) {
        this.name = name;
        this.emailid = emailid;
        this.password = password;
        this.rn = rn;
        this.cn = cn;
        this.batch = batch;
        this.img = img;
    }

    public Student() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public void setImg(String img) {
        this.img = img;
    }

    protected Student(Parcel in) {
        id = in.readString();
        name = in.readString();
        emailid = in.readString();
        password = in.readString();
        rn = in.readString();
        cn = in.readString();
        batch = in.readString();
        device_id = in.readString();
        img = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

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

    public String getImg() {
        return img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(emailid);
        parcel.writeString(password);
        parcel.writeString(rn);
        parcel.writeString(cn);
        parcel.writeString(batch);
        parcel.writeString(device_id);
        parcel.writeString(img);
    }
}
