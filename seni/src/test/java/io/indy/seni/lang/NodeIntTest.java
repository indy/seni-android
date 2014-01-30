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

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import static org.fest.assertions.api.Assertions.assertThat;

public class NodeIntTest extends EvalTestBase {

    @Test
    public void testNodeInt() {
        NodeInt n = new NodeInt(42);

        assertThat(n.getType()).isEqualTo(Node.Type.INT);
        assertThat(n.getInt()).isEqualTo(42);
    }

    @Test
    public void testEq() {
        NodeInt n = new NodeInt(42);

        assertThat(n.eq(n)).isTrue();
        assertThat(n.eq(new NodeInt(42))).isTrue();

        assertThat(n.eq(new NodeInt(666))).isFalse();
    }

    @Test
    public void testScribe() {
        assertNodeScribe(new NodeInt(42), "42");
        assertNodeScribe(new NodeInt(42, true), "[42]");

        assertScribe("42");
        assertScribe("[42]");
    }

    @Test
    public void testToString() {
        NodeInt n = new NodeInt(42);
        assertThat(n.toString()).isEqualTo("INT: 42");
    }

    @Test
    public void testMutate() {
        NodeInt n = new NodeInt(12, true);

        int minValue = 30;
        int maxValue = 50;
        Queue<Token> tokens = new ArrayDeque<Token>();
        tokens.add(makeToken(Token.Type.LIST_START));
        tokens.add(makeToken("in-range"));
        tokens.add(makeToken(minValue));
        tokens.add(makeToken(maxValue));
        tokens.add(makeToken(Token.Type.LIST_END));
        List<Node> params = Parser.parse(tokens);

        n.addParameterNode(params.get(0));

        assertThat(n.getType()).isEqualTo(Node.Type.INT);
        assertThat(n.getInt()).isEqualTo(12);

        for(int i=0;i<100;i++) {
            NodeMutate m = n.mutate();
            assertThat(m.getType()).isEqualTo(Node.Type.INT);
            try {
                int f = Node.asIntValue(m);
                assertThat(f >= minValue).isTrue();
                assertThat(f <= maxValue).isTrue();
            } catch (LangException e) {
                assertThat(true).isFalse();
            }
        }
    }
}
