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

    /** The root {@link android.view.ViewGroup} into which the activity should place its contents. */
    @Override
    public ViewGroup get(Activity activity, SeniApp app) {

        activity.setContentView(R.layout.debug_activity_frame);
        return (ViewGroup)(activity.findViewById(R.id.debug_content));
    }
}
