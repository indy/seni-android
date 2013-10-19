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

import java.util.HashSet;
import java.util.Set;
import java.util.Queue;
import java.util.ArrayDeque;

public class Lexer {

    private static class Pair {
        public Token mToken;
        public String mRemaining;
    }

    private static final Set<Character> sWhitespaceSet;
    private static final Set<Character> sDigitSet;
    private static final Set<Character> sAlphaSet;
    private static final Set<Character> sSymbolSet;

    static {
        String whitespaces = " \t\n";
        sWhitespaceSet = new HashSet<Character>();
        for(char c :  whitespaces.toCharArray()) {
            sWhitespaceSet.add(c);
        }

        String digits = "0123456789";
        sDigitSet = new HashSet<Character>();
        for(char c :  digits.toCharArray()) {
            sDigitSet.add(c);
        }

        String alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+-*/<>=";
        sAlphaSet = new HashSet<Character>();
        for(char c :  alpha.toCharArray()) {
            sAlphaSet.add(c);
        }

        String sym = "-!@#$%^&*<>?";
        sSymbolSet = new HashSet<Character>();
        for(char c :  sym.toCharArray()) {
            sSymbolSet.add(c);
        }
    }

    private static final char MINUS = '-';
    private static final char PERIOD = '.';

    public static Queue<Token> tokenise(String input) throws LangException {

        Queue<Token> q = new ArrayDeque<Token>();
        Pair p = null;

        String s = skipWhitespace(input);

        while(s.length() > 0) {
            switch(nextTokenType(s)) {
            case LIST_START :
                p = consumeListStart(s);
                break;
            case LIST_END :
                p = consumeListEnd(s);
                break;
            case STRING :
                p = consumeString(s);
                break;
            case NAME :
                p = consumeName(s);
                break;
            case INT :
                p = consumeInt(s);
                break;
            case FLOAT :
                p = consumeFloat(s);
                break;
            case QUOTE_ABBREVIATION :
                p = consumeQuoteAbbreviation(s);
                break;

            default :
                throw new LangException("unknown token type: " + s.charAt(0));
            }

            q.add(p.mToken);
            s = skipWhitespace(p.mRemaining);
        }

        return q;
    }

    private static String skipWhitespace(String s) {
        int len = s.length();
        for(int i = 0; i < len; i++) {
            if(!isWhitespace(s.charAt(i))) {
                return s.substring(i);
            }
        }
        return "";
    }

    private static Pair consumeInt(String s) {

        int i = 0;
        for(i=0;i<s.length();i++) {
            char c = s.charAt(i);
            if(!isDigit(c) && c != MINUS) {
                break;
            }
            if(!isDigit(c) && !(i == 0 && c == MINUS)) {
                break;
            }
        }
        
        Pair p = new Pair();
        int val = Integer.parseInt(s.substring(0, i));

        try {
            p.mToken = new Token(Token.Type.INT, val);
        } catch(Token.TokenException e) {
            e.printStackTrace();
        }

        p.mRemaining = s.substring(i, s.length());
        return p;

    }

    private static Pair consumeFloat(String s) {
        int i = 0;
        for(i=0;i<s.length();i++) {
            char c = s.charAt(i);
            if(!isDigit(c) && !(i == 0 && c == MINUS) && c != PERIOD) {
                break;
            }
        }
        
        Pair p = new Pair();
        float val = (float) Double.parseDouble(s.substring(0, i));
        try {
            p.mToken = new Token(Token.Type.FLOAT, val);
        } catch(Token.TokenException e) {
            e.printStackTrace();
        }
        p.mRemaining = s.substring(i, s.length());
        return p;
    }

    private static Pair consumeListStart(String s) {
        Pair p = new Pair();
        p.mToken = new Token(Token.Type.LIST_START);
        p.mRemaining = s.substring(1);
        return p;
    }

    private static Pair consumeListEnd(String s) {
        Pair p = new Pair();
        p.mToken = new Token(Token.Type.LIST_END);
        p.mRemaining = s.substring(1);
        return p;
    }

    private static Pair consumeString(String s) {

        String val = s.substring(1); // skip first \"
        int nextQuote = indexOfNext(val, '\"');
        val = val.substring(0, nextQuote);

        Pair p = new Pair();
        p.mToken = new Token(Token.Type.STRING, val);
        p.mRemaining = s.substring(nextQuote + 2);
        return p;
    }

    private static Pair consumeName(String s) {
        int i = 0;
        for(i=0;i<s.length();i++) {
            char c = s.charAt(i);
            if(!isAlpha(c) && !isDigit(c) && !isSymbol(c)) {
                break;
            }
        }
        
        Pair p = new Pair();
        p.mToken = new Token(Token.Type.NAME, s.substring(0, i));
        p.mRemaining = s.substring(i, s.length());
        return p;
    }

    private static Pair consumeQuoteAbbreviation(String s) {
        Pair p = new Pair();
        p.mToken = new Token(Token.Type.QUOTE_ABBREVIATION);
        p.mRemaining = s.substring(1);
        return p;
    }

    private static int indexOfNext(String s, Character c) {
        for(int i=0;i<s.length();i++) {
            if(s.charAt(i) == c) {
                return i;
            }
        }
        return -1; // TODO: raise exception
    }

    private static Token.Type nextTokenType(String s) {
        char c = s.charAt(0);

        if(isQuoteAbbreviation(c)) {
            return Token.Type.QUOTE_ABBREVIATION;
        }

        if(isListStart(c)) {
            return Token.Type.LIST_START;
        }

        if(isListEnd(c)) {
            return Token.Type.LIST_END;
        }
        
        if(isQuotedString(c)) {
            return Token.Type.STRING;
        }

        if(isName(c)) {
            if (c == MINUS && s.length() > 1 && isDigit(s.charAt(1))) {
                // don't treat negative numbers as NAMEs
            } else {
                return Token.Type.NAME;
            }
        }

        if(isDigit(c) || c == MINUS || c == PERIOD) {
            return hasPeriod(s) ? Token.Type.FLOAT : Token.Type.INT;
        }

        return Token.Type.UNKNOWN;
    }

    // is there a period in the stream of characters before we get to whitespace
    private static boolean hasPeriod(String s) {
        for(int i=0;i<s.length();i++) {
            char c = s.charAt(i);
            if(c == PERIOD) {
                return true;
            }
            if(isWhitespace(c)) {
                return false;
            }
        }
        return false;
    }

    private static boolean isWhitespace(Character c) {
        return sWhitespaceSet.contains(c);
    }

    private static boolean isDigit(Character c) {
        return sDigitSet.contains(c);
    }

    private static boolean isAlpha(Character c) {
        return sAlphaSet.contains(c);
    }

    private static boolean isSymbol(Character c) {
        return sSymbolSet.contains(c);
    }

    private static boolean isListStart(Character c) {
        return c == '(';
    }

    private static boolean isListEnd(Character c) {
        return c == ')';
    }

    private static boolean isQuotedString(Character c) {
        return c == '"';
    }

    private static boolean isQuoteAbbreviation(Character c) {
        return c == '\'';
    }

    private static boolean isName(Character c) {
        return isAlpha(c);
    }
}
