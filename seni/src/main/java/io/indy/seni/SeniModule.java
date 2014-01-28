package io.indy.seni;

import android.app.Application;

import io.indy.seni.dummy.DummyModule;
import dagger.Module;
import dagger.Provides;
import io.indy.seni.ui.UiModule;

import javax.inject.Singleton;

@Module(
    includes = {
        DummyModule.class,
        UiModule.class
//        DataModule.class
    },
    injects = {
        SeniApp.class
    }
)
public final class SeniModule {
  private final SeniApp app;

  public SeniModule(SeniApp app) {
    this.app = app;
  }
/*
  @Provides @Singleton
  Application provideApplication() {
    return app;
  }*/
}
