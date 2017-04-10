package com.kepler.notificationsystem.dao;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amit on 08-04-2017.
 */

public class StudentParent {
    @Expose
    private boolean status;
    @Expose
    private int code;
    @Expose
    private String message;
    @Expose
    private List<Student> data=new ArrayList<>();

    public boolean isStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<Student> getData() {
        return data;
    }
}
