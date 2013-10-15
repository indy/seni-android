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

import java.util.Queue;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class LexerTest {

    @Test
    public void testTokenise() {
        Token t;
        Queue<Token> q;

        try {
            q = Lexer.tokenise("()");
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.LIST_START);
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.LIST_END);

            q = Lexer.tokenise("( (     )");
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.LIST_START);
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.LIST_START);
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.LIST_END);

            q = Lexer.tokenise("   (\"hello\"  )  ");
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.LIST_START);
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.STRING);
            assertThat(t.getStringValue()).isEqualTo("hello");
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.LIST_END);

            q = Lexer.tokenise("   (hello \"bob\"  )  ");
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.LIST_START);
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.NAME);
            assertThat(t.getStringValue()).isEqualTo("hello");
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.STRING);
            assertThat(t.getStringValue()).isEqualTo("bob");
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.LIST_END);

            q = Lexer.tokenise("(hello 23 -54.3 \"bob\")");
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.LIST_START);
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.NAME);
            assertThat(t.getStringValue()).isEqualTo("hello");
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.INT);
            assertThat(t.getIntValue()).isEqualTo(23);
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.FLOAT);
            assertThat(t.getFloatValue()).isEqualTo(-54.3f);
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.STRING);
            assertThat(t.getStringValue()).isEqualTo("bob");
            t = q.remove();
            assertThat(t.getType()).isEqualTo(Token.Type.LIST_END);
        } catch (LangException exception) {
            assertThat(true).isEqualTo(false);
        }

    }

}
