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

import static org.fest.assertions.api.Assertions.assertThat;

abstract public class EvalTestBase {

    protected void assertNodeScribe(Node n, String expected) {
        try {
            Env env = new Env();
            assertThat(n.scribe(env)).isEqualTo(expected);
        } catch (Node.ScribeException e) {
            assertThat(false).isEqualTo(true);
        }
    }

    protected void assertBinding(String code, String name, String expected) {
        Node expectedAST = asAST(expected).get(0);

        Queue<Token> tokens;
        try {

            Env e = Env.bindCoreFuns(new Env());

            tokens = Lexer.tokenise(code);
            List<Node> ast = Parser.parse(tokens);
            for (Node node : ast) {
                Interpreter.eval(e, node);
            }

            Node n = e.lookup(name);

            String errorMsg = "binding " + name + " != " + expected;
            assertThat(expectedAST.eq(n)).overridingErrorMessage(errorMsg).isTrue();

        } catch (LangException e) {
            assertThat(true).overridingErrorMessage(e.toString()).isFalse();
        }
    }

    protected void assertEval(String code, String expected) {
        Node n = run(code);

        // assuming that expected evals to a single node
        Node expectedAST = asAST(expected).get(0);

        String errorMsg = "eval: " + code + " != " + expected;
        assertThat(expectedAST.eq(n)).overridingErrorMessage(errorMsg).isTrue();
    }

    // parse the code string into an AST and then scribe it back into a string
    // it's expected to return back to it's original string form
    protected void assertScribe(String code) {
        // assuming that code evals to a single node
        Node codeAST = asAST(code).get(0);

        try {
            Env env = new Env();
            String scribed = codeAST.scribe(env);

            String errorMsg = "code: `" + code + "` incorrectly scribed to: `" + scribed + "`";
            assertThat(scribed).overridingErrorMessage(errorMsg).isEqualTo(code);
        } catch (Node.ScribeException e) {
            assertThat(true).overridingErrorMessage(e.getMessage()).isEqualTo(false);
        }
    }

    protected void assertScribe(AstHolder astHolder, Genotype genotype, String expected) {
        try {
            assertThat(astHolder.scribe(genotype)).isEqualTo(expected);
        } catch (Node.ScribeException e) {
            e.printStackTrace();
            assertThat(true).isEqualTo(false);
        }
    }

    protected void appendAlterable(Genotype genotype, Genotype derived, NodeMutate node) {
        int index = derived.getAlterable().size();
        node.setGenSym((genotype.getAlterable().get(index)).getGenSym());
        derived.add(node);
    }

    protected List<Node> asAST(String code) {
        Token t;
        Queue<Token> tokens;

        try {

            tokens = Lexer.tokenise(code);
            return Parser.parse(tokens);

        } catch (LangException exception) {
            assertThat(true).overridingErrorMessage(exception.toString()).isFalse();
        }

        return null;
    }

    // runs single s-expressions
    protected Node run(String code) {

        try {
            List<Node> ast = asAST(code);
            Env e = Env.bindCoreFuns(new Env());

            Node res = null;

            for (Node node : ast) {
                res = Interpreter.eval(e, node);
            }

            return res;

        } catch (LangException exception) {
            assertThat(true).overridingErrorMessage(exception.toString()).isFalse();
        }

        return null;
    }


    protected Token makeToken(int val) {
        try {
            return new Token(Token.Type.INT, val);
        } catch (Token.TokenException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Token makeToken(String val) {
        return new Token(Token.Type.NAME, val);
    }

    protected Token makeToken(float val) {
        try {
            return new Token(Token.Type.FLOAT, val);
        } catch (Token.TokenException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Token makeToken(Token.Type t) {
        return new Token(t);
    }

    protected Token makeToken(Token.Type t, String val) {
        return new Token(t, val);
    }


}
