package io.indy.seni;

import dagger.Module;
import io.indy.seni.ui.DebugUiModule;
import io.indy.seni.dummy.DebugDummyModule;


@Module(
    addsTo = SeniModule.class,
    includes = {
        DebugDummyModule.class,
        DebugUiModule.class
        //        DebugDataModule.class
    },
    overrides = true
)
public final class DebugSeniModule {
}
