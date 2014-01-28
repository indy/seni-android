package io.indy.seni;

final class Modules {
    static Object[] list(SeniApp app) {
        return new Object[]{
                new SeniModule(app)
        };
    }

    private Modules() {
        // No instances.
    }
}
