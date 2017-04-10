package com.kepler.notificationsystem.support;

import android.content.Context;

import com.kepler.notificationsystem.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Created by 12 on 9/9/2016.
 */
public abstract class SimpleNetworkHandler extends AsyncHttpResponseHandler {

    private static final String TAG = SimpleNetworkHandler.class.getSimpleName();
    private static final int TIMEOUT = 90000;
    private static Context context;
    public static final String BASE_URL = "http://www.tagly.in/wwwork/simi/controller.php";

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            Logger.e(TAG, new String(responseBody));
            onResult(statusCode, headers, new String(responseBody));
        } catch (Exception e) {
        }
    }

    public abstract void onResult(int statusCode, Header[] headers, Object responseBody);

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        if (context != null)
            Utils.toast(context, error.getMessage());
        Logger.e(TAG+"FAILED", error.getMessage());
    }

    public static void callPostRequest(Context context, String url, RequestParams parems, SimpleNetworkHandler asyncHttpResponseHandler) {
        if (isNull(url, asyncHttpResponseHandler))
            return;
        SimpleNetworkHandler.context = context;
        Logger.e(TAG, url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(TIMEOUT);
        if (parems != null)
            Logger.e(TAG, parems.toString());
        client.post(context, url, parems, asyncHttpResponseHandler);
    }


    private static boolean isNull(String url, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        return asyncHttpResponseHandler == null || url == null || url.trim().length() == 0;
    }

}
