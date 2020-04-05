package com.androidapp.chatapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by theonetech25 on 8/31/2015.
 */
public class Global {

    Context mContext;

    public Global(Context mCon) {
        this.mContext = mCon;
    }

    public static void hideKeyboard(Activity noticeBoard) {
        View view = noticeBoard.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) noticeBoard.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public synchronized boolean isNetworkAvailable() {
        boolean flag = false;

        if (checkNetworkAvailable()) {
            flag = true;

        } else {
            flag = false;
            //  Toast.makeText(mContext,"No network available!",Toast.LENGTH_SHORT).show();
            Log.d("", "No network available!");
        }
        return flag;
    }

    private boolean checkNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void showToast(String strMessage){
        Toast.makeText(mContext,strMessage, Toast.LENGTH_SHORT).show();
    }
    public void setPrefBoolean(String Tag, Boolean isBool) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Tag, isBool);
        editor.apply();
    }


    public boolean getPrefBoolean(String Tag, Boolean isBool) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getBoolean(Tag, isBool);
    }

    public void setPrefString(String Tag, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Tag, value);
        editor.apply();
    }

    public void setPrefInteger(String Tag, Integer value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Tag, value);
        editor.apply();
    }

    public String getPrefString(String Tag) {
        String value = "";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getString(Tag, value);
    }

    public Integer getPrefInteger(String Tag) {
        int value = 0;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getInt(Tag, value);
    }

    public static Bitmap getSampleBitmapFromFile(String bitmapFilePath, int reqWidth, int reqHeight) throws FileNotFoundException {
        // calculating image size
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(new FileInputStream(new File(bitmapFilePath)), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            int scale = calculateInSampleSize(options, reqWidth, reqHeight);

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;

            return BitmapFactory.decodeStream(new FileInputStream(new File(bitmapFilePath)), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


}
