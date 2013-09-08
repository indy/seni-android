package io.indy.seni.lang;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class ParserTest {
    @Test
    public void testConsumeItemWithInt() {
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(makeToken(42));

        assertThat(tokens.size()).isEqualTo(1);

        Parser parser = new Parser();
        Parser.ParserReturn pr = parser.consumeItem(tokens);

        assertThat(pr.mTokens.size()).isEqualTo(0);
        assertThat(pr.mNode.getType()).isEqualTo(Node.Type.INT);
        NodeInt ni = (NodeInt)(pr.mNode);
        assertThat(ni.getInt()).isEqualTo(42);
    }

    @Test
    public void testConsumeItemWithList() {
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(makeToken(Token.Type.LIST_START));
        tokens.add(makeToken(16));
        tokens.add(makeToken(61));
        tokens.add(makeToken(Token.Type.LIST_END));
        tokens.add(makeToken(99));

        assertThat(tokens.size()).isEqualTo(5);

        Parser parser = new Parser();
        Parser.ParserReturn pr = parser.consumeItem(tokens);

        // consume list, just the '99' left
        assertThat(pr.mTokens.size()).isEqualTo(1);

        assertThat(pr.mNode.getType()).isEqualTo(Node.Type.LIST);
        NodeList nl = (NodeList)(pr.mNode);
        assertThat(nl.getChildren().size()).isEqualTo(2);
    }

    private Token makeToken(int val) {
        try {
            return new Token(Token.Type.INT, val);
        } catch(Token.TokenException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Token makeToken(Token.Type t) {
        return new Token(t);
    }
}

/*

(ns seni.rt.lang.test.parser
  (:use;*CLJSBUILD-REMOVE*;-macros
    [clip-test.core;*CLJSBUILD-REMOVE*;-cljs
     :only [deftest testing is
            ;*CLJSBUILD-REMOVE*;thrown? thrown-with-msg?
            ]])
  (:require [seni.rt.lang.parser :as parser]))

(deftest consume-item
  (is (= [nil '() 23]
         (parser/consume-item '([:number 23]))))
  (is (= [nil '([:number 23]) "foo"]
         (parser/consume-item '([:string "foo"] [:number 23])))))

(deftest consume-list
  (is (= [nil '() []]
         (parser/consume-list '([:list-start] [:list-end]))))

  (is (= ["error: mismatched parentheses" nil nil]
         (parser/consume-list '([:list-start]))))

  (is (= [nil '() [23]]
         (parser/consume-list '([:list-start] [:number 23] [:list-end]))))

  (is (= ["error: not a list" '([:number 23] [:list-end]) nil]
         (parser/consume-list '([:number 23] [:list-end]))))

  (is (= [nil '() [23 "foo"]]
         (parser/consume-list '([:list-start] [:number 23] [:string "foo"] [:list-end]))))

  (is (= [nil '() [23 "foo" [33 "bar"]]]
         (parser/consume-list '([:list-start] [:number 23] [:string "foo"] [:list-start] [:number 33] [:string "bar"] [:list-end] [:list-end]))))

  (is (= ["error: mismatched parentheses" nil nil]
         (parser/consume-list '([:list-start] [:number 23] [:string "foo"] [:list-start] [:number 33] [:string "bar"] [:list-end])))))


(deftest parse
  ;; parser/parse returns [err result] vector
  (is (= [nil [[]]]
         (parser/parse '([:list-start] [:list-end]))))

  (is (= [nil [[23 "foo" [33 "bar"]]]]
         (parser/parse '([:list-start] [:number 23] [:string "foo"] [:list-start] [:number 33] [:string "bar"] [:list-end] [:list-end]))))

  (is (= ["error: mismatched parentheses" nil]
         (parser/parse '([:list-start] [:number 23] [:string "foo"] [:list-start] [:number 33] [:string "bar"] [:list-end]))))

  )

(defn test-ns-hook
  []
  (consume-item)
  (consume-list)
  (parse))


 */
