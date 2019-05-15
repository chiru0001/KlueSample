package com.volive.klueapp.app;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.volive.klueapp.Utils.ConnectivityReceiver;

public class MyApplication extends Application{
    private static MyApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // For View Pager.
        Fresco.initialize(this);
    }
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


}
