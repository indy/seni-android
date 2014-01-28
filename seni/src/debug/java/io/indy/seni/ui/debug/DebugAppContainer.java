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

package io.indy.seni.ui.debug;

import android.app.Activity;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.indy.seni.R;
import io.indy.seni.SeniApp;
import io.indy.seni.ui.AppContainer;

//@Singleton
public class DebugAppContainer implements AppContainer {


    @Inject
    public DebugAppContainer() {

    }

    /**
     * The root {@link android.view.ViewGroup} into which the activity should place its contents.
     */
    @Override
    public ViewGroup get(Activity activity, SeniApp app) {

        activity.setContentView(R.layout.debug_activity_frame);
        return (ViewGroup) (activity.findViewById(R.id.debug_content));
    }
}
