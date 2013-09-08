package io.indy.seni.lang;

import java.util.List;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class NodeListTest {

    @Test
    public void testNodeList() {
        NodeList n = new NodeList(null);
        NodeInt nodeInt = new NodeInt(null, 42);
        n.addChild(nodeInt);

        assertThat(n.getType()).isEqualTo(Node.Type.LIST);
        
        List<Node> children = n.getChildren();
        assertThat(children.size()).isEqualTo(1);

        Node child = children.get(0);
        assertThat(child.getType()).isEqualTo(Node.Type.INT);
    }
}
