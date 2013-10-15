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
import java.util.Queue;
import org.junit.Test;
import org.junit.Before;
import static org.fest.assertions.api.Assertions.assertThat;


public class IntegrationTest {

    @Test
    public void testMath() {
        assertExpression("(+ 2 1)", 3);
        assertExpression("(+ (+ 2 1) (+ 2 1))", 6);
        assertExpression("(* (* 2 2) (* 3 3))", 36);
        assertExpression("(/ 54 9)", 6);
    }

    @Test
    public void testComparisons() {
        assertExpression("(> 2 1)", true);
        assertExpression("(< 2 1)", false);
        assertExpression("(= 2 1)", false);
        assertExpression("(= 2 2)", true);

        assertExpression("(> 2.0 1.0)", true);
        assertExpression("(< 2.0 1.0)", false);
        assertExpression("(= 2.0 1.0)", false);
        assertExpression("(= 2.0 2.0)", true);
    }

    @Test
    public void testSpecialForms() {
        assertExpression("((lambda (x) (+ x x)) 3)", 6);

        assertExpression("(if (< 12 77) (+ 1 3) (+ 1 7))", 4);
        assertExpression("(if (< 12 77) 4 8)", 4);


        assertExpression("(begin (+ 1 1) (+ 2 2) (+ 3 3))", 6);
    }

    private void assertExpression(String code, int val) {
        Node n = run(code);
        assertThat(n.getType()).isEqualTo(Node.Type.INT);
        assertThat(((NodeInt)n).getInt()).isEqualTo(val);        
    }

    private void assertExpression(String code, boolean val) {
        Node n = run(code);
        assertThat(n.getType()).isEqualTo(Node.Type.BOOLEAN);
        assertThat(((NodeBoolean)n).getBoolean()).isEqualTo(val);
    }

    // runs single s-expressions
    private Node run(String code) {
        Token t;
        Queue<Token> tokens;

        try {

            tokens = Lexer.tokenise(code);

            List<Node> nodes = Parser.parse(tokens);
            assertThat(nodes.size()).isEqualTo(1);
        
            Node ast = nodes.get(0);
            Env e = Env.bindCoreFuns(new Env());

            return Interpreter.eval(e, ast);

        } catch (LangException exception) {
            assertThat(true).isFalse();
        }

        return null;
    }

}
