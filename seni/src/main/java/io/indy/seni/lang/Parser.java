package io.indy.seni.lang;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser {
}

/*
(ns seni.rt.lang.parser)

(declare consume-item)

(defn consume-list
  [lst]
  (if (not= :list-start (ffirst lst))
    ["error: not a list" lst nil]
    (loop [sub-lst (rest lst)
           sub-res []]
      (if (= :list-end (ffirst sub-lst))
        [nil (rest sub-lst) sub-res]
        (if (empty? sub-lst)
          ["error: mismatched parentheses" nil nil]
          (let [[err remaining-lst val] (consume-item sub-lst)]
            (if err
              [err nil nil]
              (recur remaining-lst (conj sub-res val)))))))))

(defn consume-item
  [lst]
  (if (empty? lst)
    [nil '(), '()]
    (if (= :list-start (ffirst lst))
      (consume-list lst)
      [nil (rest lst) (second (first lst))])))

(defn- remove-comments
  [lst]
  (remove #(= :comment (first %)) lst))

(defn parse
  "parse a list of tokens generated by lexing an input stream"
  [lst]
  (loop [[err ls res] (consume-item (remove-comments lst))
         overall-res []]
    (if err
      [err nil]
      (if (empty? ls)
        [nil (conj overall-res res)]
        (recur (consume-item ls)
               (conj overall-res res))))))


 */
