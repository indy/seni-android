
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

public class MathFnsTest extends EvalTestBase {

    @Test
    public void testMath() {
        assertEval("(+ 2 1)", "3");
        assertEval("(+ (+ 2 1) (+ 2 1))", "6");
        assertEval("(* (* 2 2) (* 3 3))", "36");
        assertEval("(/ 54 9)", "6");
    }

    @Test
    public void testComparisons() {
        assertEval("(> 2 1)", "true");
        assertEval("(< 2 1)", "false");
        assertEval("(= 2 1)", "false");
        assertEval("(= 2 2)", "true");

        assertEval("(> 2.0 1.0)", "true");
        assertEval("(< 2.0 1.0)", "false");
        assertEval("(= 2.0 1.0)", "false");
        assertEval("(= 2.0 2.0)", "true");
    }

    @Test
    public void testScribe() {
        assertScribe(">");
        assertScribe("+");
        assertScribe("-");
        assertScribe("*");
        assertScribe("/");
    }
}
