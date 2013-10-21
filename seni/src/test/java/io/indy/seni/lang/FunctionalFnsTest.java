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

public class FunctionalFnsTest extends EvalTestBase {

    @Test
    public void testFunctional() {
        assertEval("(apply + (quote (4 5 6)))", "15");
        assertEval("(map (lambda (x) (+ x x)) (quote (1 2 3)))", "(2 4 6)");

        assertEval("(reduce + (quote (4 5 6)))", "15");
        assertEval("(reduce + 4 (quote (5 6)))", "15");
    }

    @Test
    public void testFilter() {
        assertEval("(filter (lambda (x) (< x 10)) '(12 1 43 9 4))", "(1 9 4)");
    }
}
