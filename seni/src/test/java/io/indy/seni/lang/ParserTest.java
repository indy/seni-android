package io.indy.seni.lang;

import java.util.List;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class ParserTest {

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
