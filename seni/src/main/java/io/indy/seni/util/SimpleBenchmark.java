package io.indy.seni.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import io.indy.seni.AppConfig;

public class SimpleBenchmark {

    private static final String TAG = "SimpleBenchmark";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    private long mStart;
    private long mEnd;

    public SimpleBenchmark() {
    }

    public void go(Canvas canvas) {
        timedDrawLine(canvas);
        timedDrawThickLine(canvas);
        timedDrawLineAlternatingPaint(canvas);
        timedDrawLineNewPaint(canvas);

        timedDrawBox(canvas);
        timedDrawLargeBox(canvas);
        timedDrawModifiedPaintBox(canvas);
        timedDrawThickBox(canvas);

        timedDrawSmallCircle(canvas);
        timedDrawLargeCircle(canvas);
        timedDrawThickCircle(canvas);


        timedDrawMatrixLine(canvas);
        timedDrawLargeAlphaMatrixCircle(canvas);
        timedDrawSmallAlphaMatrixBox(canvas);
        timedDrawLargeAlphaMatrixBox(canvas);
    }

    private void timedDrawLine(Canvas canvas) {
        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setARGB(255, 255, 0, 0);

        int i;
        mStart = android.os.SystemClock.uptimeMillis();
        for(i=0;i<10000;i++) {
            canvas.drawLine(350.0f, 250.0f, 450.0f, 350.0f, linePaint);
        }
        mEnd = android.os.SystemClock.uptimeMillis();
        ifd("timedDrawLine time: " + (mEnd-mStart) + " ms");
    }

    private void timedDrawThickLine(Canvas canvas) {
        int i;

        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(4.0f);
        linePaint.setARGB(255, 255, 0, 0);

        mStart = android.os.SystemClock.uptimeMillis();
        for(i=0;i<10000;i++) {
            canvas.drawLine(350.0f, 250.0f, 450.0f, 350.0f, linePaint);
        }
        mEnd = android.os.SystemClock.uptimeMillis();
        ifd("timedDrawThickLine time: " + (mEnd-mStart) + " ms");
    }

    private void timedDrawLineAlternatingPaint(Canvas canvas) {
        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setARGB(255, 255, 0, 0);
        Paint linePaint2 = new Paint();
        linePaint2.setAntiAlias(true);
        linePaint2.setARGB(255, 0, 0, 255);

        int i;
        mStart = android.os.SystemClock.uptimeMillis();
        for(i=0;i<10000;i++) {
            if((i&1) == 0) {
                canvas.drawLine(350.0f, 250.0f, 450.0f, 350.0f, linePaint);
            } else {
                canvas.drawLine(450.0f, 250.0f, 550.0f, 350.0f, linePaint2);
            }
        }
        mEnd = android.os.SystemClock.uptimeMillis();
        ifd("timedDrawLineAlternatingPaint time: " + (mEnd-mStart) + " ms");
    }

    private void timedDrawLineNewPaint(Canvas canvas) {

        int i;
        mStart = android.os.SystemClock.uptimeMillis();
        for(i=0;i<10000;i++) {
            Paint linePaint = new Paint();
            linePaint.setAntiAlias(true);
            linePaint.setARGB(255, 255, 0, 0);
            canvas.drawLine(350.0f, 250.0f, 450.0f, 350.0f, linePaint);
        }
        mEnd = android.os.SystemClock.uptimeMillis();
        ifd("timedDrawLineNewPaint time: " + (mEnd-mStart) + " ms");
    }

    private void timedDrawBox(Canvas canvas) {

        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setARGB(255, 255, 0, 0);

        int i;
        RectF scratchRect = new RectF(4, 4, 12, 12);

        mStart = android.os.SystemClock.uptimeMillis();
        for(i=0;i<10000;i++) {
            canvas.drawRect(scratchRect, linePaint);
        }
        mEnd = android.os.SystemClock.uptimeMillis();
        ifd("timedDrawBox time: " + (mEnd-mStart) + " ms");
    }


    private void timedDrawLargeBox(Canvas canvas) {

        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setARGB(255, 255, 0, 0);

        int i;
        RectF scratchRect = new RectF(4, 4, 512, 512);

        mStart = android.os.SystemClock.uptimeMillis();
        for(i=0;i<10000;i++) {
            canvas.drawRect(scratchRect, linePaint);
        }
        mEnd = android.os.SystemClock.uptimeMillis();
        ifd("timedDrawLargeBox time: " + (mEnd-mStart) + " ms");
    }

    private void timedDrawModifiedPaintBox(Canvas canvas) {

        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setARGB(255, 255, 0, 0);

        int i;
        RectF scratchRect = new RectF(4, 4, 12, 12);

        mStart = android.os.SystemClock.uptimeMillis();
        for(i=0;i<10000;i++) {
            linePaint.setARGB(i % 255, 255, 0, 0);
            canvas.drawRect(scratchRect, linePaint);
        }
        mEnd = android.os.SystemClock.uptimeMillis();
        ifd("timedDrawModifiedPaintBox time: " + (mEnd-mStart) + " ms");
    }

    private void timedDrawThickBox(Canvas canvas) {

        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(4.0f);
        linePaint.setARGB(255, 255, 0, 0);

        int i;
        RectF scratchRect = new RectF(304, 304, 312, 312);

        mStart = android.os.SystemClock.uptimeMillis();
        for(i=0;i<10000;i++) {
            canvas.drawRect(scratchRect, linePaint);
        }
        mEnd = android.os.SystemClock.uptimeMillis();
        ifd("timedDrawThickBox time: " + (mEnd-mStart) + " ms");
    }


    private void timedDrawSmallCircle(Canvas canvas) {

        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setARGB(255, 255, 0, 0);

        int i;

        mStart = android.os.SystemClock.uptimeMillis();
        for(i=0;i<10000;i++) {
            canvas.drawCircle(200, 200, 5, linePaint);
        }
        mEnd = android.os.SystemClock.uptimeMillis();
        ifd("timedDrawSmallCircle time: " + (mEnd-mStart) + " ms");
    }


    private void timedDrawLargeCircle(Canvas canvas) {

        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setARGB(255, 255, 0, 0);

        int i;

        mStart = android.os.SystemClock.uptimeMillis();
        for(i=0;i<10000;i++) {
            canvas.drawCircle(200, 200, 50, linePaint);
        }
        mEnd = android.os.SystemClock.uptimeMillis();
        ifd("timedDrawLargeCircle time: " + (mEnd-mStart) + " ms");
    }

    private void timedDrawThickCircle(Canvas canvas) {

        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(4.0f);
        linePaint.setARGB(255, 255, 0, 0);

        int i;

        mStart = android.os.SystemClock.uptimeMillis();
        for(i=0;i<10000;i++) {
            canvas.drawCircle(200, 200, 50, linePaint);
        }
        mEnd = android.os.SystemClock.uptimeMillis();
        ifd("timedDrawThickCircle time: " + (mEnd-mStart) + " ms");
    }



    private void timedDrawMatrixLine(Canvas canvas) {
        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setARGB(255, 255, 0, 0);

        int i;
        mStart = android.os.SystemClock.uptimeMillis();
        for(i=0;i<10000;i++) {
            canvas.save();
            canvas.rotate(34.0f, 60.0f, 350.0f);
            canvas.drawLine(350.0f, 250.0f, 450.0f, 350.0f, linePaint);
            canvas.restore();
        }
        mEnd = android.os.SystemClock.uptimeMillis();
        ifd("timedDrawMatrixLine time: " + (mEnd-mStart) + " ms");
    }



    private void timedDrawLargeAlphaMatrixCircle(Canvas canvas) {

        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setARGB(25, 255, 0, 0);

        int i;

        mStart = android.os.SystemClock.uptimeMillis();
        for(i=0;i<10000;i++) {
            canvas.save();
            canvas.rotate(34.0f, 60.0f, 350.0f);
            canvas.drawCircle(i % 600, i % 600, 50, linePaint);
            canvas.restore();
        }
        mEnd = android.os.SystemClock.uptimeMillis();
        ifd("timedDrawLargeAlphaMatrixCircle time: " + (mEnd-mStart) + " ms");
    }

    private void timedDrawSmallAlphaMatrixBox(Canvas canvas) {

        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setARGB(25, 255, 0, 0);

        int i;

        mStart = android.os.SystemClock.uptimeMillis();
        for(i=0;i<10000;i++) {
            canvas.save();
            canvas.rotate(64.0f, 260.0f, 150.0f);
            canvas.drawRect(i % 600, i % 600, (i % 600) + 8, (i % 600) + 8, linePaint);
            canvas.restore();
        }
        mEnd = android.os.SystemClock.uptimeMillis();
        ifd("timedDrawSmallAlphaMatrixBox time: " + (mEnd-mStart) + " ms");
    }

    private void timedDrawLargeAlphaMatrixBox(Canvas canvas) {

        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setARGB(25, 255, 0, 0);

        int i;

        mStart = android.os.SystemClock.uptimeMillis();
        for(i=0;i<10000;i++) {
            canvas.save();
            canvas.rotate(64.0f, 260.0f, 150.0f);
            canvas.drawRect(i % 600, i % 600, (i % 600) + 300, (i % 600) + 300, linePaint);
            canvas.restore();
        }
        mEnd = android.os.SystemClock.uptimeMillis();
        ifd("timedDrawLargeAlphaMatrixBox time: " + (mEnd-mStart) + " ms");
    }


}
