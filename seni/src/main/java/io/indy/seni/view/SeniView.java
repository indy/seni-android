package io.indy.seni.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import io.indy.seni.AppConfig;
import io.indy.seni.dummy.Art1352;
import io.indy.seni.util.SimpleBenchmark;

public class SeniView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "SeniView";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    public class SeniViewThread extends Thread {
        /*
         * Member (state) fields
         */
        /**
         * Current height of the surface/canvas.
         *
         * @see #setSurfaceSize
         */
        private int mCanvasHeight = 1;

        /**
         * Current width of the surface/canvas.
         *
         * @see #setSurfaceSize
         */
        private int mCanvasWidth = 1;

        /** Message handler used by mThread to interact with TextView */
        private Handler mHandler;

        /** Scratch rect object. */
        private RectF mScratchRect;

        /** Handle to the surface manager object we interact with */
        private SurfaceHolder mSurfaceHolder;

        public SeniViewThread(SurfaceHolder surfaceHolder, Context context,
                Handler handler) {
            // get handles to some important objects
            mSurfaceHolder = surfaceHolder;
            mHandler = handler;
            mContext = context;

            mScratchRect = new RectF(0, 0, 0, 0);
        }

        /**
         * Restores game state from the indicated Bundle. Typically called when
         * the Activity is being restored after having been previously
         * destroyed.
         *
         * @param savedState Bundle containing the game state
         */
        public synchronized void restoreState(Bundle savedState) {
            synchronized (mSurfaceHolder) {
                // mDifficulty = savedState.getInt(KEY_DIFFICULTY);
            }
        }

        @Override
        public void run() {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                        doDraw(c);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
        }

        /* Callback invoked when the surface dimensions change. */
        public void setSurfaceSize(int width, int height) {
            // synchronized to make sure these all change atomically
            synchronized (mSurfaceHolder) {
                mCanvasWidth = width;
                mCanvasHeight = height;
            }
        }

        /**
         * Draws the ship, fuel/speed bars, and background to the provided
         * Canvas.
         */
        private void doDraw(Canvas canvas) {
//            SimpleBenchmark sb = new SimpleBenchmark();
//            sb.go(canvas);
            Art1352.Draw(canvas, mCanvasWidth, mCanvasHeight);
        }
    }

    /** Handle to the application context, used to e.g. fetch Drawables. */
    private Context mContext;

    /** The mThread that actually draws the animation */
    private SeniViewThread mThread;

    public SeniView(Context context, AttributeSet attrs) {
        super(context, attrs);

        ifd("SeniView constructor surface");

        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        // create thread only; it's started in surfaceCreated()
        mThread = new SeniViewThread(holder, mContext, new Handler() {
            @Override
            public void handleMessage(Message m) {
            }
        });

        setFocusable(true); // make sure we get key events
    }

    /**
     * Fetches the animation thread corresponding to this SeniView.
     *
     * @return the animation thread
     */
    public SeniViewThread getThread() {
        return mThread;
    }

    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        ifd("surfaceChanged");

        mThread.setSurfaceSize(width, height);
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {
        ifd("surfaceCreated");

        // check if surfaceDestroyed has already been called
        // since start() can't be called on the same thread twice
        if (mThread.getState() == Thread.State.TERMINATED) {
            mThread = new SeniViewThread(holder, getContext(), new Handler() {
                @Override
                public void handleMessage(Message m) {
                }
            });
        }

        // start the mThread here so that we don't busy-wait in run()
        // waiting for the surface to be created
        mThread.start();
    }

    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        ifd("surfaceDestroyed");

        // we have to tell mThread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {
                ifd("interruptedException thrown");
            }
        }
    }
}
