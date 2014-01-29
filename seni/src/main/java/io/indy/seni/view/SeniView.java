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

package io.indy.seni.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import io.indy.seni.AppConfig;
import io.indy.seni.lang.AstHolder;
import io.indy.seni.lang.Genotype;
import io.indy.seni.runtime.SeniRuntime;

public class SeniView extends View {

    private static final String TAG = "SeniView";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    private AstHolder mAstHolder;
    private Genotype mGenotype;

    protected Bitmap mBitmap;
    protected Canvas mCanvas;

    public SeniView(Context context) {
        super(context);
        init();
    }

    public SeniView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SeniView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);

        Paint paint = new Paint();
        paint.setARGB(255, 0, 0, 0);
        mCanvas.drawRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight(), paint);

        SeniRuntime.render(mCanvas, mAstHolder, mGenotype);
    }

    public void setAstHolder(AstHolder astHolder) {
        ifd("setAstHolder");
        mAstHolder = astHolder;
        mGenotype = mAstHolder.getGenotype();
    }

    public void setGenotype(Genotype genotype) {
        mGenotype = genotype;
    }
}
