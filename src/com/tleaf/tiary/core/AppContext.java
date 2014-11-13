package com.tleaf.tiary.core;

import com.tleaf.tiary.network.TLeafSession;

/**
 * Created by jangyoungjin on 11/6/14.
 */
public class AppContext {
    public static final String TAG = AppContext.class.getSimpleName();

    /**
     * Redirect to UI Thread
     *
     * @param r
     */
    public static void post(Runnable r) {
        CustomApplication.mInstance.mHandler.post(r);
    }

    /**
     * UI Conversion ( DP to PX )
     *
     * @param dp
     * @return
     */
    public static int dp2px(float dp) {
        return (int) (dp * CustomApplication.mInstance.mDensity);
    }

    /**
     * UI Conversion ( DP to PX )
     *
     * @param px
     * @return
     */
    public static float px2dp(int px) {
        return px / CustomApplication.mInstance.mDensity;
    }

    /**
     * Set TLeaf session
     *
     * @param session
     */
    public static void setTlSession(TLeafSession session) {
        CustomApplication.mInstance.setTLeafSession(session);
    }

    /**
     * Get TLeaf session
     */
    public static TLeafSession getTlSession() {
        return CustomApplication.mInstance.getTLeafSession();
    }

    /**
     * GET Device ID
     * @return
     */
    public static String getDeviceId(){
        return CustomApplication.mInstance.mDeviceId;
    }

    /**
     * GET Preference
     */
    public static Preference getPreference() {
        return CustomApplication.mInstance.getmPreference();
    }
}
