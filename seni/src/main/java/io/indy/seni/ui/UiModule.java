package io.indy.seni.ui;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.indy.seni.ScriptGridActivity;

@Module(
        injects = {
                ScriptGridActivity.class
        },
        complete = false,
        library = true
)
public class UiModule {
    @Provides
    @Singleton
    AppContainer provideAppContainer() {
        return AppContainer.DEFAULT;
    }
}
