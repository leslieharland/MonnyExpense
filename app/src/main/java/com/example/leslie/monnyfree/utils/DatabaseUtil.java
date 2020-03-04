package com.example.leslie.monnyfree.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Leslie on 3/18/2018.
 */

public class DatabaseUtil {
    private static SparseArray<String> rawHashMap = new SparseArray<>();
    private static final int BUFFER_DIMENSION = 128;

    public static String getRawAsString(Context context, int resId) {
        if (rawHashMap.indexOfKey(resId) >= 0) {
            //Log.d("raw", "getRawAsString: " + rawHashMap.get(resId));
            return rawHashMap.get(resId);
        } else {
            String raw = loadRaw(context, resId);
            if (!TextUtils.isEmpty(raw))
                rawHashMap.put(resId, raw);
            //Log.d("raw", "getRawAsString: " + raw);
            return raw;
        }
    }

    public static String loadRaw(Context context, int resId) {
        String result = null;
        // take input stream
        InputStream is = context.getResources().openRawResource(resId);
        if (is != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_DIMENSION];
            int numRead = 0;
            try {
                while ((numRead = is.read(buffer)) >= 0) {
                    outputStream.write(buffer, 0, numRead);
                }
                // convert to string
                result = new String(outputStream.toByteArray());
            } catch (IOException e) {
                Log.e("loadRaw", e.toString());
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        Log.e("loadRaw", "close byte array" + e.toString());
                    }
                }
            }
        }
        return result;
    }
}
