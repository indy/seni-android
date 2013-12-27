package io.indy.seni.runtime;

import android.graphics.Paint;
import android.util.Log;

import io.indy.seni.AppConfig;
import io.indy.seni.core.Colour;

public class CoreBridge {

    private static final String TAG = "CoreBridge";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    public static Paint setColour(Paint paint, Colour colour) {
        float[] f = colour.as(Colour.Format.RGB).getVals();

        paint.setARGB((int)(f[Colour.ALPHA] * 255),
                (int)(f[Colour.RED] * 255),
                (int)(f[Colour.GREEN] * 255),
                (int)(f[Colour.BLUE] * 255));
        return paint;
    }

}
