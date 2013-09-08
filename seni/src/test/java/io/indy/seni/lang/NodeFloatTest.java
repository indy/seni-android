package io.indy.seni.lang;

import java.util.List;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class NodeFloatTest {

    @Test
    public void testNodeFloat() {
        NodeFloat n = new NodeFloat(12.34f);

        assertThat(n.getType()).isEqualTo(Node.Type.FLOAT);
        assertThat(n.getFloat()).isEqualTo(12.34f);
    }
}

