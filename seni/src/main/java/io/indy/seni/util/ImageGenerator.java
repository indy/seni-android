/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.indy.seni.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import io.indy.seni.BuildConfig;
import io.indy.seni.R;
import io.indy.seni.dummy.Art1403b;
import io.indy.seni.lang.AstHolder;
import io.indy.seni.lang.Genotype;
import io.indy.seni.runtime.SeniRuntime;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple subclass of {@link ImageWorker} that fetches and resizes images fetched from a URL.
 */
public class ImageGenerator extends ImageWorker {
    private static final String TAG = "ImageGenerator";

    protected int mImageWidth;
    protected int mImageHeight;

    /**
     * Initialize providing a target image width and height for the processing images.
     *
     * @param context
     * @param imageWidth
     * @param imageHeight
     */
    public ImageGenerator(Context context, int imageWidth, int imageHeight) {
        super(context);
        setImageSize(imageWidth, imageHeight);
    }

    /**
     * Initialize providing a single target image size (used for both width and height);
     *
     * @param context
     * @param imageSize
     */
    public ImageGenerator(Context context, int imageSize) {
        super(context);
        setImageSize(imageSize);
    }


    /**
     * Set the target image width and height.
     *
     * @param width
     * @param height
     */
    public void setImageSize(int width, int height) {
        mImageWidth = width;
        mImageHeight = height;
    }

    /**
     * Set the target image size (width and height will be the same).
     *
     * @param size
     */
    public void setImageSize(int size) {
        setImageSize(size, size);
    }

    @Override
    protected void clearCacheInternal() {
        super.clearCacheInternal();
    }

    /**
     * The main process method, which will be called by the ImageWorker in the AsyncTask background
     * thread.
     *
     * @param data The data to load the bitmap, in this case, a regular http URL
     * @return The downloaded and resized bitmap
     */
    private Bitmap processBitmap(String data) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "processBitmap - " + data);
        }

        // could also create arbitrary bitmap and resize it
        Bitmap b = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        Paint paint = new Paint();
        paint.setARGB(255, 200, 0, 0);
        c.drawRect(0, 0, mImageWidth, mImageHeight, paint);

        String script = Art1403b.script();
        AstHolder mAstHolder = new AstHolder(script);
        Genotype mGenotype = mAstHolder.getGenotype();
        SeniRuntime.render(c, mAstHolder, mGenotype);

        return b;
    }

    @Override
    protected Bitmap processBitmap(Object data) {
        return processBitmap(String.valueOf(data));
    }

}
