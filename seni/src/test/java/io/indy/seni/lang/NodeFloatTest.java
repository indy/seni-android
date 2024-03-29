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

public class NodeFloatTest extends EvalTestBase {

    @Test
    public void testNodeFloat() {
        NodeFloat n = new NodeFloat(12.34f);

        assertThat(n.getType()).isEqualTo(Node.Type.FLOAT);
        assertThat(n.getFloat()).isEqualTo(12.34f);
    }

    @Test
    public void testCastingToNodeFloat() {
        NodeInt n = new NodeInt(42);

        try {
            assertThat(Node.asFloatValue(n)).isEqualTo(42.0f);
        } catch (LangException e) {
            assertThat(false).isEqualTo(true);
        }
    }

    @Test
    public void testEq() {
        NodeFloat n = new NodeFloat(12.34f);

        assertThat(n.eq(n)).isTrue();
        assertThat(n.eq(new NodeFloat(12.34f))).isTrue();

        assertThat(n.eq(new NodeFloat(98.76f))).isFalse();
    }

    @Test
    public void testScribe() {
        assertNodeScribe(new NodeFloat(12.34f), "12.34");
        assertNodeScribe(new NodeFloat(12.34f, true), "[12.34]");
    }

    @Test
    public void testToString() {
        NodeFloat n = new NodeFloat(12.34f);
        assertThat(n.toString()).isEqualTo("FLOAT: 12.34");
    }

    @Test
    public void testMutate() {
        NodeFloat n = new NodeFloat(12.34f, true);

        float minValue = 8.0f;
        float maxValue = 24.0f;
        Queue<Token> tokens = new ArrayDeque<Token>();
        tokens.add(makeToken(Token.Type.LIST_START));
        tokens.add(makeToken("in-range"));
        tokens.add(makeToken(minValue));
        tokens.add(makeToken(maxValue));
        tokens.add(makeToken(Token.Type.LIST_END));
        List<Node> params = Parser.parse(tokens);

        n.addParameterNode(params.get(0));

        assertThat(n.getType()).isEqualTo(Node.Type.FLOAT);
        assertThat(n.getFloat()).isEqualTo(12.34f);

        for(int i=0;i<100;i++) {
            NodeMutate m = n.mutate();
            assertThat(m.getType()).isEqualTo(Node.Type.FLOAT);
            try {
                float f = Node.asFloatValue(m);
                assertThat(f >= minValue).isTrue();
                assertThat(f <= maxValue).isTrue();
            } catch (LangException e) {
                assertThat(true).isFalse();
            }
        }
    }


}

