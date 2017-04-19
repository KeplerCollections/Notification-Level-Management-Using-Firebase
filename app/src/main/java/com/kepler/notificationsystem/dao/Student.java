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
    private String course;
    @Expose
    private int year;
    @Expose
    private String batch;
    @Expose
    private String device_id;
    @Expose
    private String img;
    @Expose
    private String reg_id;

    public Student(String name, String emailid, String password, String rn, String cn,String course,int year,String img,String batch) {
        this.name = name;
        this.emailid = emailid;
        this.password = password;
        this.rn = rn;
        this.cn = cn;
        this.course = course;
        this.year = year;
        this.img = img;
        this.batch = batch;
    }
    public Student(String name, String emailid, String course, String batch) {
        this.name = name;
        this.emailid = emailid;
        this.course = course;
        this.batch = batch;
    }
    public Student() {

    }



    protected Student(Parcel in) {
        id = in.readString();
        name = in.readString();
        emailid = in.readString();
        rn = in.readString();
        cn = in.readString();
        course = in.readString();
        year = in.readInt();
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

    public String getCourse() {
        return course;
    }

    public int getYear() {
        return year;
    }

    public String getBatch() {
        return batch;
    }

    public String getImg() {
        return img;
    }

    public String getReg_id() {
        return reg_id;
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
        parcel.writeString(rn);
        parcel.writeString(cn);
        parcel.writeString(course);
        parcel.writeInt(year);
        parcel.writeString(img);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }
}
