package com.kepler.notificationsystem.support;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by Ranjan-vaio on 10/5/2015.
 */
public class GenerateHashKey {
    /**
     * @param context
     * @return Key hash for facebook
     * @author Ranjan
     */

//    public static String printFacebookKeyHash(Context context) {
//        PackageInfo packageInfo;
//        String key = null;
//        try {
//            // getting application package name, as defined in manifest
//            String packageName = context.getApplicationContext()
//                    .getPackageName();
//
//            // Retriving package info
//            packageInfo = context.getPackageManager().getPackageInfo(
//                    packageName, PackageManager.GET_SIGNATURES);
//
//            Log.e("Package Name=", context.getApplicationContext()
//                    .getPackageName());
//
//            for (Signature signature : packageInfo.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                key = new String(Base64.encode(md.digest(), 0));
//
//                // String key = new String(Base64.encodeBytes(md.digest()));
//                Log.e("Key Hash=", key);
//            }
//        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e("Name not found", e1.toString());
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("No such an algorithm", e.toString());
//        } catch (Exception e) {
//            Log.e("Exception", e.toString());
//        }
//
//        return key;
//    }

    /**
     * @param context
     * @return Hashed Device ID
     * @author Ranjan
     */
//    public String getHashedDeivceId(Context context) {
//        String imei = "";
//        if ((new MarshMallowPermission(context)).checkPermissionForReadPhoneState()) {
//            try {
//                TelephonyManager telephony = (TelephonyManager) context
//                    .getSystemService("phone");
//            String imeinumber = telephony.getDeviceId();
//            if (!isValidIMEI(imeinumber))
//                imeinumber = Settings.Secure.getString(context.getContentResolver(),
//                        "android_id");
//            MessageDigest mdEnc;
//                mdEnc = MessageDigest.getInstance("MD5");
//                mdEnc.update(imeinumber.getBytes(), 0, imeinumber.length());
//                imei = asHex(mdEnc.digest());
//            } catch (NoSuchAlgorithmException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        return imei;
//    }

    public static String getHashedDeivceId(Context context) {
        String imei = "";
        if (context != null) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                imei = telephonyManager.getDeviceId();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return imei;
    }

//    private static boolean isValidIMEI(String string) {
//        try {
//            long l = Long.valueOf(string);
//            return l != 0;
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }
//
//    private static String asHex(byte[] buf) {
//        char[] HEX_CHARS = "0123456789abcdef".toCharArray();
//        char[] chars = new char[2 * buf.length];
//        for (int i = 0; i < buf.length; ++i) {
//            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
//            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
//        }
//        return new String(chars);
//    }

}