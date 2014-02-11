/*
 * Copyright 2014 Inderjit Gill
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.indy.seni.runtime;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import io.indy.seni.AppConfig;

public class SeniContext {

    private static final String TAG = "SeniContext";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath;

    public SeniContext(Canvas canvas) {
        mCanvas = canvas;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPath = new Path();
    }

    public Canvas getCanvas() {
        return mCanvas;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public Path getPath() { return mPath; }
}
