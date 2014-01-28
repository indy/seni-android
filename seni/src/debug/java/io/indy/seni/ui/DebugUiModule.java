package io.indy.seni.ui;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.indy.seni.ui.debug.DebugAppContainer;

@Module(
        injects = DebugAppContainer.class,
        complete = false,
        library = true,
        overrides = true
)
public class DebugUiModule {

    @Provides
    @Singleton
    AppContainer provideAppContainer(DebugAppContainer debugAppContainer) {
        return debugAppContainer;
    }

    /*
  @Provides @Singleton ActivityHierarchyServer provideActivityHierarchyServer() {
    return new SocketActivityHierarchyServer();
  }
*/

}
