package com.kepler.notificationsystem.services;

import android.content.Context;

import com.kepler.notificationsystem.notification.Config;
import com.kepler.notificationsystem.support.GenerateHashKey;
import com.kepler.notificationsystem.support.Params;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Amit on 08-04-2017.
 */

public class Student {
    private static final int UPDATE_EMAIL = 1;
    private static final int UPDATE_ALL = 2;
    private static final String UPDATE = "update";
    private static final String SELECT = "select";
    private static final String REGISTER = "register";
    public static final String UPDATE_PIC = "update_pic";
    public static final int OFFSET = 30;

    public static void register(Context context, com.kepler.notificationsystem.dao.Student student, SimpleNetworkHandler simpleNetworkHandler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(Params.NAME, student.getName());
        requestParams.put(Params.EMAILID, student.getEmailid());
        requestParams.put(Params.RN, student.getRn());
        requestParams.put(Params.CN, student.getCn());
        requestParams.put(Params.BATCH, student.getBatch());
//        requestParams.put(Params.PASSWORD, user.getPassword());
        requestParams.put(Params.DEVICE_ID, GenerateHashKey.getHashedDeivceId(context));
        requestParams.put(Params.ACTION, REGISTER);
        load(null, requestParams, simpleNetworkHandler);
    }

//    public static void serviceRequest(@NonNull Context context, String msg, String value, SimpleNetworkHandler simpleNetworkHandler) {
//        RequestParams requestParams = new RequestParams();
//        requestParams.put(Params.VALUE, value);
//        requestParams.put(Params.MESSAGE, msg);
//        requestParams.put(Params.ACTION, "service_request");
//        requestParams.put(Params.DEVICE_ID, GenerateHashKey.getHashedDeivceId(context));
//        load(null, requestParams, simpleNetworkHandler);
//    }

    public static void updateEmailId(Context context, String email_id, String new_email_id, SimpleNetworkHandler simpleNetworkHandler) {
        if (email_id == null)
            return;
        RequestParams requestParams = new RequestParams();
        requestParams.put(Params.EMAILID, email_id);
        requestParams.put(Params.NEW_EMAILID, new_email_id);
        requestParams.put(Params.ACTION_TYPE, String.valueOf(UPDATE_EMAIL));
        requestParams.put(Params.DEVICE_ID, GenerateHashKey.getHashedDeivceId(context));
        requestParams.put(Params.ACTION, UPDATE);
        load(null, requestParams, simpleNetworkHandler);
    }

    public static void update(Context context, com.kepler.notificationsystem.dao.Student student, SimpleNetworkHandler simpleNetworkHandler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(Params.ID, student.getId());
        requestParams.put(Params.NAME, student.getName());
        requestParams.put(Params.RN, student.getRn());
        requestParams.put(Params.CN, student.getCn());
        requestParams.put(Params.BATCH, student.getBatch());
        requestParams.put(Params.ACTION_TYPE, String.valueOf(UPDATE_ALL));
        requestParams.put(Params.DEVICE_ID, GenerateHashKey.getHashedDeivceId(context));
        requestParams.put(Params.ACTION, UPDATE);
        load(null, requestParams, simpleNetworkHandler);
    }

    public static void uploadFile(Context context, File file, String action, String email_id, SimpleNetworkHandler simpleNetworkHandler) {
        if (email_id == null)
            return;
        RequestParams requestParams = new RequestParams();
        try {
            requestParams.put(Params.FILE, file);
            requestParams.put(Params.FILE_NAME, email_id.split("@")[0] + file.getName().substring(file.getName().lastIndexOf(".")));
            requestParams.put(Params.EMAILID, email_id);
            requestParams.put(Params.DEVICE_ID, GenerateHashKey.getHashedDeivceId(context));
            requestParams.put(Params.ACTION, action);
            load(context, requestParams, simpleNetworkHandler);
        } catch (FileNotFoundException e) {
        }
    }

    public static void select(Context context, com.kepler.notificationsystem.dao.Student student, SimpleNetworkHandler simpleNetworkHandler, int page) {
        RequestParams requestParams = new RequestParams();
        if (student.getName() != null) {
            requestParams.put(Params.NAME, student.getName());
        } else if (student.getBatch() != null) {
            requestParams.put(Params.BATCH, student.getBatch());
        } else if (student.getEmailid() != null) {
            requestParams.put(Params.EMAILID, student.getEmailid());
        }
        requestParams.put(Params.OFFSET, String.valueOf(OFFSET));
        requestParams.put(Params.PAGE, String.valueOf(page));
        requestParams.put(Params.DEVICE_ID, GenerateHashKey.getHashedDeivceId(context));
        requestParams.put(Params.ACTION, SELECT);
        load(null, requestParams, simpleNetworkHandler);
    }

    public static void select(Context context, SimpleNetworkHandler simpleNetworkHandler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(Params.DEVICE_ID, GenerateHashKey.getHashedDeivceId(context));
        requestParams.put(Params.ACTION, SELECT);
        load(null, requestParams, simpleNetworkHandler);
    }

    private static void load(Context context, RequestParams params, SimpleNetworkHandler simpleNetworkHandler) {
        SimpleNetworkHandler.callPostRequest(context, SimpleNetworkHandler.BASE_URL, params, simpleNetworkHandler);
    }
}
