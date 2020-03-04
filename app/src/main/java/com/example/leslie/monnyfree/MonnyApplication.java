package com.example.leslie.monnyfree;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.stetho.Stetho;

/**
 * Created by Leslie on 3/23/2018.
 */

public class MonnyApplication extends Application {
    public static final String TAG = MonnyApplication.class.getSimpleName();
    private static float mTextSize = 16;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
        MonnyApplication.context = getApplicationContext();
    }

    public static Context getContext() {
        return MonnyApplication.context;
    }

    public static void uninstallApp() {
        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec("adb uninstall com.example.leslie.monnyfree");
            pr.waitFor();
        } catch (Exception e) {
            Log.e(TAG,"uninstalling app");
        }
    }

    public static float getTextSize() {
        return MonnyApplication.mTextSize;
    }
}
