package io.indy.seni;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import io.indy.seni.dummy.Monkey;

public class ScriptGridActivity extends Activity {


    private static final String TAG = "ScriptGridActivity";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    @Inject
    Monkey mMonkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_grid);

        SeniApp app = SeniApp.get(this);
        app.inject(this);

        ifd("monkey is: " + mMonkey.getName());
    }
}
