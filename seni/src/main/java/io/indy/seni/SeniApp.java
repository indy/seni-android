package io.indy.seni;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import dagger.ObjectGraph;
import io.indy.seni.dummy.Monkey;

import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class SeniApp extends Application {


    private static final String TAG = "SeniApp";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

  private ObjectGraph objectGraph;

  @Override public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
//      Timber.plant(new DebugTree());
    } else {
      // TODO Crashlytics.start(this);
      // TODO Timber.plant(new CrashlyticsTree());
    }

    buildObjectGraphAndInject();

  }

  public void buildObjectGraphAndInject() {
    long start = System.nanoTime();

    objectGraph = ObjectGraph.create(Modules.list(this));
    objectGraph.inject(this);

    long diff = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
    ifd("Global object graph creation took " + diff + "ms");
  }

  public void inject(Object o) {
    objectGraph.inject(o);
  }

  public static SeniApp get(Context context) {
    return (SeniApp) context.getApplicationContext();
  }
}
