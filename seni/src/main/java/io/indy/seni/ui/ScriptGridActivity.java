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
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.indy.seni.AppConfig;
import io.indy.seni.R;
import io.indy.seni.SeniApp;
import io.indy.seni.ui.AppContainer;

public class ScriptGridActivity extends Activity {


    private static final String TAG = "ScriptGridActivity";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

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
    }
}
