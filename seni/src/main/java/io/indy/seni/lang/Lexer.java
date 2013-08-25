package io.indy.seni.lang;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    public static List<Token> tokenise(String input) {

        List<Token> l = new ArrayList<Token>();

        l.add(new Token(Token.Type.LIST_START));

        return l;
    }

}
