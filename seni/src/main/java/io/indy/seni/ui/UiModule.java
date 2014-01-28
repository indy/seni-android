package io.indy.seni.ui;

import dagger.Module;
import dagger.Provides;
import io.indy.seni.ScriptGridActivity;

import javax.inject.Singleton;

@Module(
    injects = {
        ScriptGridActivity.class
    },
    complete = false,
    library = true
)
public class UiModule {
  @Provides @Singleton AppContainer provideAppContainer() {
    return AppContainer.DEFAULT;
  }
}
