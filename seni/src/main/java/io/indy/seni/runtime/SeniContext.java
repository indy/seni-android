package io.indy.seni.runtime;

import android.graphics.Canvas;
import android.util.Log;

import io.indy.seni.AppConfig;

public class SeniContext {

    private static final String TAG = "SeniContext";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    private Canvas mCanvas;

    public SeniContext(Canvas canvas) {
        mCanvas = canvas;
    }



    public void save() {
        mCanvas.save();
    }

    public void restore() {
        mCanvas.restore();
    }

}
