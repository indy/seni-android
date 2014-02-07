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

package io.indy.seni;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.indy.seni.ui.AppContainer;

public class EvolveActivity extends Activity {

    @Inject
    AppContainer mAppContainer;

    private ViewGroup mContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SeniApp app = SeniApp.get(this);
        app.inject(this);

        mContainer = mAppContainer.get(this, app);
        getLayoutInflater().inflate(R.layout.activity_evolve, mContainer);

        //setContentView(R.layout.activity_evolve);

        // Show the Up button in the action bar.
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            String script = getIntent().getStringExtra(EvolveGridFragment.GENESIS_SCRIPT);

            app.setGenesisScript(script);

            Bundle arguments = new Bundle();
            arguments.putBoolean(EvolveGridFragment.HAS_GENOTYPES, false);
            arguments.putString(EvolveGridFragment.GENESIS_SCRIPT, script);
            EvolveGridFragment fragment = new EvolveGridFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.evolve_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpTo(this, new Intent(this, ScriptGridActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
