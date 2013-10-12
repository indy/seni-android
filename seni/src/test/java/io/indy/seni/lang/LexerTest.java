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
