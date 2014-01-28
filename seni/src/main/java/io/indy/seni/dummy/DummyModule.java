package io.indy.seni.dummy;

import dagger.Module;
import dagger.Provides;
import io.indy.seni.ScriptGridActivity;
import io.indy.seni.SeniApp;

@Module(
        injects = {SeniApp.class, ScriptGridActivity.class
        },
        complete = false,
        library = true
)
public class DummyModule {

    @Provides
    Monkey provideMonkey() {
        return new Monkey();
    }
/*
    @Provides @Singleton AppContainer provideAppContainer() {
        return AppContainer.DEFAULT;
    }

    @Provides @Singleton ActivityHierarchyServer provideActivityHierarchyServer() {
        return ActivityHierarchyServer.NONE;
    }
*/
}
