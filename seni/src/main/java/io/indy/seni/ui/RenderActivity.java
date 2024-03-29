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

package io.indy.seni.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.indy.seni.AppConfig;
import io.indy.seni.R;
import io.indy.seni.SeniApp;
import io.indy.seni.lang.AstHolder;
import io.indy.seni.lang.Genotype;
import io.indy.seni.view.SeniView;

public class RenderActivity extends Activity {


    private static final String TAG = "RenderActivity";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    @Inject
    AppContainer mAppContainer;

    private ViewGroup mContainer;


    /**
     * A handle to the View in which the game is running.
     */
    private SeniView mSeniView;

    /**
     * Invoked when the Activity is created.
     *
     * @param savedInstanceState a Bundle containing state saved from a previous
     *                           execution, or null if this is a new execution
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SeniApp app = SeniApp.get(this);
        app.inject(this);

        mContainer = mAppContainer.get(this, app);
        getLayoutInflater().inflate(R.layout.activity_render, mContainer);





        // tell system to use the layout defined in our XML file
//        setContentView(R.layout.activity_render);

        // get handles to the SeniView from XML, and its SeniViewThread
        mSeniView = (SeniView) findViewById(R.id.lunar);

        if (savedInstanceState == null) {
            String script = getIntent().getStringExtra(EvolveGridFragment.GENESIS_SCRIPT);
            AstHolder astHolder = new AstHolder(script);
            Genotype genotype = astHolder.getGenotype();
            mSeniView.setAstHolder(astHolder);
            mSeniView.setGenotype(genotype);

        } else {
            // we are being restored: resume a previous game
            //mSeniViewThread.restoreState(savedInstanceState);
        }


    }

    /**
     * Invoked when the Activity loses user focus.
     */
    @Override
    protected void onPause() {
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


    /**
     * Invoked during init to give the Activity a chance to set up its Menu.
     *
     * @param menu the Menu to which entries may be added
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.render, menu);

        super.onCreateOptionsMenu(menu);
        return true;
    }

    /**
     * Invoked when the user selects an item from the Menu.
     *
     * @param item the Menu entry which was selected
     * @return true if the Menu item was legit (and we consumed it), false
     * otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                ifd("settings clicked");
                return true;
            case R.id.action_templist:
                Intent intent = new Intent(this, ScriptGridActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
