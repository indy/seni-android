package io.indy.seni.lang;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lexer {

    public class Pair {
        public Token mToken;
        public String mRemaining;
    }

    private Set<Character> mWhitespaceSet;
    private Set<Character> mDigitSet;
    private Set<Character> mAlphaSet;
    private Set<Character> mSymbolSet;

    private char MINUS = '-';
    private char PERIOD = '.';

    public Lexer() {
        setupSets();
    }

    public List<Token> tokenise(String input) {

        List<Token> l = new ArrayList<Token>();

        Token.Type tt;
        Pair p = null;

        String s = skipWhitespace(input);

        while(s.length() > 0) {
            tt = nextTokenType(s);
            switch(tt) {
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
            default :
                // TODO: throw error
                break;
            }

            l.add(p.mToken);
            s = skipWhitespace(p.mRemaining);
        }

        return l;
    }

    public String skipWhitespace(String s) {
        int len = s.length();
        for(int i = 0; i < len; i++) {
            if(!isWhitespace(s.charAt(i))) {
                return s.substring(i);
            }
        }
        return "";
    }

    public Pair consumeInt(String s) {

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

    public Pair consumeFloat(String s) {
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

    public Pair consumeListStart(String s) {
        Pair p = new Pair();
        p.mToken = new Token(Token.Type.LIST_START);
        p.mRemaining = s.substring(1);
        return p;
    }

    public Pair consumeListEnd(String s) {
        Pair p = new Pair();
        p.mToken = new Token(Token.Type.LIST_END);
        p.mRemaining = s.substring(1);
        return p;
    }

    public Pair consumeString(String s) {

        String val = s.substring(1); // skip first \"
        int nextQuote = indexOfNext(val, '\"');
        val = val.substring(0, nextQuote);

        Pair p = new Pair();
        p.mToken = new Token(Token.Type.STRING, val);
        p.mRemaining = s.substring(nextQuote + 2);
        return p;
    }

    public Pair consumeName(String s) {
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

    private int indexOfNext(String s, Character c) {
        for(int i=0;i<s.length();i++) {
            if(s.charAt(i) == c) {
                return i;
            }
        }
        return -1; // TODO: raise exception
    }

    public Token.Type nextTokenType(String s) {
        char c = s.charAt(0);

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
            return Token.Type.NAME;
        }

        if(isDigit(c) || c == MINUS || c == PERIOD) {
            return hasPeriod(s) ? Token.Type.FLOAT : Token.Type.INT;
        }

        return Token.Type.UNKNOWN;
    }

    // is there a period in the stream of characters before we get to whitespace
    public boolean hasPeriod(String s) {
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

    public boolean isWhitespace(Character c) {
        return mWhitespaceSet.contains(c);
    }

    public boolean isDigit(Character c) {
        return mDigitSet.contains(c);
    }

    public boolean isAlpha(Character c) {
        return mAlphaSet.contains(c);
    }

    public boolean isSymbol(Character c) {
        return mSymbolSet.contains(c);
    }

    public boolean isListStart(Character c) {
        return c == '(';
    }

    public boolean isListEnd(Character c) {
        return c == ')';
    }

    public boolean isQuotedString(Character c) {
        return c == '"';
    }

    public boolean isName(Character c) {
        return isAlpha(c);
    }

    private void setupSets() {
        String whitespaces = " \t\n";
        mWhitespaceSet = new HashSet<Character>();
        for(char c :  whitespaces.toCharArray()) {
            mWhitespaceSet.add(c);
        }

        String digits = "0123456789";
        mDigitSet = new HashSet<Character>();
        for(char c :  digits.toCharArray()) {
            mDigitSet.add(c);
        }

        String alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        mAlphaSet = new HashSet<Character>();
        for(char c :  alpha.toCharArray()) {
            mAlphaSet.add(c);
        }

        String sym = "-!@#$%^&*<>?";
        mSymbolSet = new HashSet<Character>();
        for(char c :  sym.toCharArray()) {
            mSymbolSet.add(c);
        }
    }
}
