package com.example.leslie.monnyfree.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.example.leslie.monnyfree.MonnyApplication;
import com.example.leslie.monnyfree.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtil {
    public static String convertToBase64(Bitmap bitmap) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
        byte[] byteArray = os.toByteArray();
        return Base64.encodeToString(byteArray, 0);
    }

    public static Bitmap convertToBitmap(String base64String) {
        try {
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            Bitmap bitmapResult = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return bitmapResult;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("ResourceType")
    public static Drawable getDrawableResourceFromString(String icon){
        InputStream ims = null;
        try {
            if (icon.equals("add")) {
                ims = MonnyApplication.getContext().getAssets().open("categoryic/" + icon + ".png");
            }else{
                ims = MonnyApplication.getContext().getResources().openRawResource(R.drawable.ic_add);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // load image as Drawable
        Drawable d = Drawable.createFromStream(ims, null);
        return d;
    }
}
