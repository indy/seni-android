package io.indy.seni;

import android.app.Application;
import io.indy.seni.dummy.DummyModule;
//import com.jakewharton.u2020.data.DataModule;
//import com.jakewharton.u2020.ui.UiModule;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    includes = {
        DummyModule.class
//        UiModule.class,
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
  @Provides @Singleton Application provideApplication() {
    return app;
  }
    */
}
