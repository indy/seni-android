package io.indy.seni;

import android.app.Application;
import android.content.Context;
import dagger.ObjectGraph;
import io.indy.seni.dummy.Monkey;

import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class SeniApp extends Application {
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
    //Timber.i("Global object graph creation took %sms", diff);
  }

  public void inject(Object o) {
    objectGraph.inject(o);
  }

  public static SeniApp get(Context context) {
    return (SeniApp) context.getApplicationContext();
  }
}
