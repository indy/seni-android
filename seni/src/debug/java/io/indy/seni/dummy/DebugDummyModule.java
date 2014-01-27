package io.indy.seni.dummy;

import dagger.Module;
import dagger.Provides;
import io.indy.seni.SeniApp;

import javax.inject.Singleton;

@Module(
    injects = SeniApp.class,
    complete = false,
    library = true,
    overrides = true
)
public class DebugDummyModule {

    @Provides Monkey provideMonkey() {
        return new DebugMonkey();
    }
/*
  @Provides @Singleton AppContainer provideAppContainer(DebugAppContainer debugAppContainer) {
    return debugAppContainer;
  }

  @Provides @Singleton ActivityHierarchyServer provideActivityHierarchyServer() {
    return new SocketActivityHierarchyServer();
    }
*/
}
