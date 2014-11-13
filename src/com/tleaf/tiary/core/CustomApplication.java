package com.tleaf.tiary.core;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.service.textservice.SpellCheckerService.Session;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.tleaf.tiary.network.TLeafSession;


/**
 * Created by jangyoungjin on 11/6/14.
 */
public class CustomApplication extends Application {
    private static final String TAG = CustomApplication.class.getSimpleName();
    // Singleton Object
    public static CustomApplication mInstance = null;
    // Device Size
    public Point mDeviceSize;
    // Device Id
    public String mDeviceId;
    // Device dpi & ratio
    public int mDeviceDpi, mDeviceRatio;
    // Device Density
    public float mDensity;
    // Main Thread Handler
    public Handler mHandler = new Handler();
    // Preference
    private Preference mPreference;
    // TLeaf session
    private TLeafSession mTLeafSession;

    @Override
    public void onCreate() {
        mInstance = this;
        super.onCreate();
        initDeviceInfo();
        mPreference = Preference.getInstance(this);
    }

    public void initDeviceInfo() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Display diplay = windowManager.getDefaultDisplay();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        mDensity = metrics.density;
        mDeviceDpi = metrics.densityDpi;
        mDeviceSize = new Point();
        diplay.getSize(mDeviceSize);
        mDeviceRatio = mDeviceSize.y / mDeviceSize.x;
        mDeviceId = telephonyManager.getDeviceId();
    }

    public Preference getmPreference() {
        if(mPreference == null) {
            return Preference.getInstance(this);
        }
        return mPreference;
    }

    public TLeafSession getTLeafSession() {
        if(mTLeafSession == null){
            // Setting session from Preference
            // ...
        }
        return mTLeafSession;
    }

    public void setTLeafSession(TLeafSession mTLeafSession) {
        this.mTLeafSession = mTLeafSession;
    }
}
