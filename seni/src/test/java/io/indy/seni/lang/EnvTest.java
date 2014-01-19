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
import static org.fest.assertions.api.Assertions.assertThat;

public class EnvTest {

    @Test
    public void testHasBinding() {
        Env env = new Env(null);
        env.addBinding("foo", new NodeInt(42));
        assertThat(env.hasBinding("foo")).isTrue();

        Env scopedEnv = env.newScope();
        assertThat(scopedEnv.hasBinding("foo")).isTrue();
    }    

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

    private void intLookup(Env env, String key, int expected) {

        try {
            Node n = env.lookup(key);
            assertThat(n.getType()).isEqualTo(Node.Type.INT);
            NodeInt ni = (NodeInt)n;
            assertThat(ni.getInt()).isEqualTo(expected);
        } catch (LangException e) {
            assertThat(true).overridingErrorMessage(e.toString()).isFalse();
        }
    }
}
