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

public class NodeNameTest extends EvalTestBase {

    @Test
    public void testNodeName() {
        NodeName n = new NodeName("cons");

        assertThat(n.getType()).isEqualTo(Node.Type.NAME);
        assertThat(n.getName()).isEqualTo("cons");
    }

    @Test
    public void testEq() {
        NodeName n = new NodeName("cons");

        assertThat(n.eq(n)).isTrue();
        assertThat(n.eq(new NodeName("cons"))).isTrue();

        assertThat(n.eq(new NodeName("car"))).isFalse();
    }

    @Test
    public void testScribe() {
        assertNodeScribe(new NodeName("cons"), "cons");
        assertNodeScribe(new NodeName("[cons]"), "[cons]");
    }

    @Test
    public void testToString() {
        NodeName n = new NodeName("cons");
        assertThat(n.toString()).isEqualTo("NAME: cons");
    }

    @Test
    public void testMutate() {
        NodeName n = new NodeName("z", true);

        Queue<Token> tokens = new ArrayDeque<Token>();
        tokens.add(makeToken(Token.Type.LIST_START));
        tokens.add(makeToken("in-set"));
        tokens.add(makeToken("a"));
        tokens.add(makeToken("b"));
        tokens.add(makeToken("c"));
        tokens.add(makeToken("d"));
        tokens.add(makeToken(Token.Type.LIST_END));
        List<Node> params = Parser.parse(tokens);

        n.addParameterNode(params.get(0));

        assertThat(n.getType()).isEqualTo(Node.Type.NAME);
        assertThat(n.getName()).isEqualTo("z");

        int acount = 0;
        int bcount = 0;
        int ccount = 0;
        int dcount = 0;

        for(int i=0;i<100;i++) {
            NodeMutate m = n.mutate();
            assertThat(m.getType()).isEqualTo(Node.Type.NAME);
            try {
                String f = Node.asNameValue(m);

                if(f.equals("a")) acount++;
                if(f.equals("b")) bcount++;
                if(f.equals("c")) ccount++;
                if(f.equals("d")) dcount++;

                assertThat(f.equals("a") || 
                           f.equals("b") ||
                           f.equals("c") ||
                           f.equals("d")).isTrue();
            } catch (LangException e) {
                assertThat(true).isFalse();
            }
        }

        assertThat(acount > 0).isTrue();
        assertThat(bcount > 0).isTrue();
        assertThat(ccount > 0).isTrue();
        assertThat(dcount > 0).isTrue();
    }
}
