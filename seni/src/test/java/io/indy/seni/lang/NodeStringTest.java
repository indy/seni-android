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

public class NodeStringTest extends EvalTestBase {

    @Test
    public void testNodeString() {
        NodeString n = new NodeString("hello world");

        assertThat(n.getType()).isEqualTo(Node.Type.STRING);
        assertThat(n.getString()).isEqualTo("hello world");
    }

    @Test
    public void testEq() {
        NodeString n = new NodeString("hello world");

        assertThat(n.eq(n)).isTrue();
        assertThat(n.eq(new NodeString("hello world"))).isTrue();

        assertThat(n.eq(new NodeString("car"))).isFalse();
    }

    @Test
    public void testScribe() {
        assertNodeScribe(new NodeString("hello world"), "\"hello world\"");
    }

    @Test
    public void testToString() {
        NodeString n = new NodeString("hello world");
        assertThat(n.toString()).isEqualTo("STRING: \"hello world\"");
    }

}
