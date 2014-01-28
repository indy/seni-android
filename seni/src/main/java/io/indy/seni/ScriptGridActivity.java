package io.indy.seni;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.indy.seni.dummy.Monkey;
import io.indy.seni.ui.AppContainer;

public class ScriptGridActivity extends Activity {


    private static final String TAG = "ScriptGridActivity";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    @Inject
    Monkey mMonkey;

    @Inject
    AppContainer mAppContainer;

    private ViewGroup mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SeniApp app = SeniApp.get(this);
        app.inject(this);

        mContainer = mAppContainer.get(this, app);

        getLayoutInflater().inflate(R.layout.activity_script_grid, mContainer);

        //setContentView(R.layout.activity_script_grid);


        ifd("monkey is: " + mMonkey.getName());
    }
}
