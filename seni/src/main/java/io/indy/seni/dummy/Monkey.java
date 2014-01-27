package io.indy.seni.dummy;

import javax.inject.Inject;

public class Monkey {

    @Inject
    public Monkey() {
    }

    public String getName() {
        return "normal monkey";
    }
}
