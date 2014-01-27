package io.indy.seni;

//import com.jakewharton.u2020.data.DebugDataModule;
//import com.jakewharton.u2020.ui.DebugUiModule;
import io.indy.seni.dummy.DebugDummyModule;
import dagger.Module;


@Module(
    addsTo = SeniModule.class,
    includes = {
        DebugDummyModule.class
        //        DebugUiModule.class,
        //        DebugDataModule.class
    },
    overrides = true
)
public final class DebugSeniModule {
}
