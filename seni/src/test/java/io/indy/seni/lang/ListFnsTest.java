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

public class ListFnsTest extends EvalTestBase {

    @Test
    public void testFirst() {
        assertEval("(first (quote (1 2 3)))", "1");
    }

    @Test
    public void testSecond() {
        assertEval("(second (quote (1 2 3)))", "2");
    }

    @Test
    public void testNth() {
        assertEval("(nth 0 (quote (1 2 3)))", "1");
        assertEval("(nth 1 (quote (1 2 3)))", "2");
        assertEval("(nth 2 (quote (1 2 3)))", "3");
    }
}
