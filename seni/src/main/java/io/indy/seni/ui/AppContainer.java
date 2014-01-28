package io.indy.seni.ui;

import android.app.Activity;
import android.view.ViewGroup;
import io.indy.seni.SeniApp;

public interface AppContainer {
  /** The root {@link android.view.ViewGroup} into which the activity should place its contents. */
  ViewGroup get(Activity activity, SeniApp app);

  /** An {@link AppContainer} which returns the normal activity content view. */
  AppContainer DEFAULT = new AppContainer() {
    @Override public ViewGroup get(Activity activity, SeniApp app) {
        return (ViewGroup)(activity.findViewById(android.R.id.content));
    }
  };
}
