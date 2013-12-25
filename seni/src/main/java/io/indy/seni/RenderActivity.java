package io.indy.seni;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import io.indy.seni.view.SeniView;

public class RenderActivity extends Activity {


    private static final String TAG = "RenderActivity";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }


    /** A handle to the thread that's actually running the animation. */
    private SeniView.SeniViewThread mSeniViewThread;

    /** A handle to the View in which the game is running. */
    private SeniView mSeniView;

    /**
     * Invoked during init to give the Activity a chance to set up its Menu.
     *
     * @param menu the Menu to which entries may be added
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    /**
     * Invoked when the user selects an item from the Menu.
     *
     * @param item the Menu entry which was selected
     * @return true if the Menu item was legit (and we consumed it), false
     *         otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    /**
     * Invoked when the Activity is created.
     *
     * @param savedInstanceState a Bundle containing state saved from a previous
     *        execution, or null if this is a new execution
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // tell system to use the layout defined in our XML file
        setContentView(R.layout.activity_render);

        // get handles to the SeniView from XML, and its SeniViewThread
        mSeniView = (SeniView) findViewById(R.id.lunar);
        mSeniViewThread = mSeniView.getThread();

        // give the SeniView a handle to the TextView used for messages
//        mSeniView.setTextView((TextView) findViewById(R.id.text));

        if (savedInstanceState == null) {
            // we were just launched: set up a new game
//            mSeniViewThread.setState(SeniView.SeniViewThread.STATE_READY);
        } else {
            // we are being restored: resume a previous game
            mSeniViewThread.restoreState(savedInstanceState);
        }
    }

    /**
     * Invoked when the Activity loses user focus.
     */
    @Override
    protected void onPause() {
 //       mSeniView.getThread().pause(); // pause game when Activity pauses
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Notification that something is about to happen, to give the Activity a
     * chance to save state.
     *
     * @param outState a Bundle into which this Activity should save its state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // just have the View's thread save its state into our Bundle
        super.onSaveInstanceState(outState);
    }

}
