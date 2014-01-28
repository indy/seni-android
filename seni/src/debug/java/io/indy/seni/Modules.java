package io.indy.seni;

final class Modules {
    static Object[] list(SeniApp app) {
        return new Object[]{
                new SeniModule(app),
                new DebugSeniModule()
        };
    }

    private Modules() {
        // No instances.
    }
}
