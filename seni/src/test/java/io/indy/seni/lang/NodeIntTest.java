package io.indy.seni.lang;

import java.util.List;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class NodeIntTest {

    @Test
    public void testNodeInt() {
        NodeInt n = new NodeInt(42);

        assertThat(n.getType()).isEqualTo(Node.Type.INT);
        assertThat(n.getInt()).isEqualTo(42);
    }
}
