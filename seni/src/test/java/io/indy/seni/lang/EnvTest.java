package io.indy.seni.lang;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class EnvTest {

    @Test
    public void testLocalLookup() {
        Env env = new Env(null);
        env.addBinding("foo", new NodeInt(42));

        intLookup(env, "foo", 42);
    }    

    @Test
    public void testParentLookup() {
        Env env = new Env(null);
        env.addBinding("bar", new NodeInt(31));

        Env envChild = env.newScope();
        envChild.addBinding("local", new NodeInt(99));

        intLookup(envChild, "bar", 31);
        intLookup(envChild, "local", 99);
    }    

    @Test
    public void testShadowing() {
        Env env = new Env(null);
        env.addBinding("bar", new NodeInt(31));

        Env envChild = env.newScope();
        envChild.addBinding("bar", new NodeInt(99));

        intLookup(envChild, "bar", 99);
    }    

    private void intLookup(Env e, String key, int expected) {
        Node n = e.lookup(key);
        assertThat(n.getType()).isEqualTo(Node.Type.INT);
        NodeInt ni = (NodeInt)n;
        assertThat(ni.getInt()).isEqualTo(expected);
    }
}
