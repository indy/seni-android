package io.indy.seni;

import dagger.Module;
import io.indy.seni.dummy.DummyModule;
import io.indy.seni.ui.UiModule;

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
