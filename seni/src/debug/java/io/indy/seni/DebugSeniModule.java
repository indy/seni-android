package io.indy.seni;

import dagger.Module;
import io.indy.seni.dummy.DebugDummyModule;
import io.indy.seni.ui.DebugUiModule;


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
