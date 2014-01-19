package io.indy.seni.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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

        SeniRuntime.render(mCanvas, mAstHolder, mGenotype);
    }

    public void setAstHolder(AstHolder astHolder) {
        mAstHolder = astHolder;
    }
    public void setGenotype(Genotype genotype) {
        mGenotype = genotype;
    }
}
