package io.indy.seni.dummy;

import javax.inject.Inject;

public class DebugMonkey extends Monkey {

    @Inject
    public DebugMonkey() {
    }

    public String getName() {
        return "debug monkey";
    }
}
