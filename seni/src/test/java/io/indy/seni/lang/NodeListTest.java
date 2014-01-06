/*
 * Copyright 2013 Inderjit Gill
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.indy.seni.lang;

import java.util.List;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class NodeListTest {

    @Test
    public void testNodeList() {
        NodeList n = new NodeList();
        NodeInt nodeInt = new NodeInt(42);
        n.addChild(nodeInt);

        assertThat(n.getType()).isEqualTo(Node.Type.LIST);
        
        List<Node> children = n.getChildren();
        assertThat(children.size()).isEqualTo(1);

        Node child = children.get(0);
        assertThat(child.getType()).isEqualTo(Node.Type.INT);
    }

    @Test
    public void testEq() {

        // (set! n (12 (82.0 38.0) 98))
        NodeList m = new NodeList();
        m.addChild(new NodeFloat(82.0f));
        m.addChild(new NodeFloat(38.0f));

        assertThat(m.eq(m)).isTrue();

        NodeList q = new NodeList();
        q.addChild(new NodeFloat(82.0f));
        q.addChild(new NodeFloat(38.0f));

        assertThat(m.eq(q)).isTrue();

        NodeList r = new NodeList();
        r.addChild(new NodeFloat(82.0f));
        r.addChild(new NodeFloat(8.0f));

        assertThat(m.eq(r)).isFalse();

        NodeList n = new NodeList();
        n.addChild(new NodeInt(12));
        n.addChild(m);
        n.addChild(new NodeInt(98));

        assertThat(n.eq(n)).isTrue();

        NodeList m2 = new NodeList();
        m2.addChild(new NodeFloat(812.0f));
        m2.addChild(new NodeFloat(318.0f));

        NodeList n2 = new NodeList();
        n2.addChild(new NodeInt(12));
        n2.addChild(m2);
        n2.addChild(new NodeInt(98));

        assertThat(n.eq(n2)).isFalse();
    }

    @Test
    public void testScribe() {

        try {
            NodeList m = new NodeList();
            m.addChild(new NodeFloat(82.0f));
            m.addChild(new NodeFloat(38.0f));

            assertThat(m.scribe()).isEqualTo("(82.0 38.0)");

            NodeList n = new NodeList();
            n.addChild(new NodeFloat(23.0f));
            n.addChild(new NodeFloat(73.0f));

            n.addChild(m);
            assertThat(n.scribe()).isEqualTo("(23.0 73.0 (82.0 38.0))");

        } catch (Node.ScribeException e) {
            assertThat(false).isEqualTo(true);
        }

    }

    @Test
    public void testToString() {
        NodeList m = new NodeList();
        m.addChild(new NodeFloat(82.0f));
        m.addChild(new NodeFloat(38.0f));

        assertThat(m.toString()).isEqualTo("LIST: (82.0 38.0)");
    }

}
