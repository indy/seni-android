/*
 * Copyright 2014 Inderjit Gill
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Interpreter {

    public static final NodeNull NODE_NULL = new NodeNull();

    private static HashSet<String> sSpecialFormNames;

    static {
        sSpecialFormNames = new HashSet<>(20);
    }

    public static void registerSpecial(String name) {
        sSpecialFormNames.add(name);
    }

    private static Node evalMutableNode(Env env, Node expr) throws LangException {
        if (expr.isAlterable()) {
            return env.lookup(((NodeMutate) expr).getGenSym());
        } else {
            return expr;
        }
    }

    public static Node eval(Env env, Node expr) throws LangException {

        Node.Type type = expr.getType();

        if (type == Node.Type.INT) {
            return evalMutableNode(env, expr);
        }

        if (type == Node.Type.FLOAT) {
            return evalMutableNode(env, expr);
        }

        if (type == Node.Type.BOOLEAN) {
            return evalMutableNode(env, expr);
        }

        if (type == Node.Type.STRING) {
            return evalMutableNode(env, expr);
        }

        if (type == Node.Type.COLOUR) {
            return expr;
        }

        if (type == Node.Type.NULL) {
            return expr;
        }

        if (type == Node.Type.NAME) {
            NodeName nodeName;
            if (expr.isAlterable()) {
                String genSym = ((NodeMutate) expr).getGenSym();
                nodeName = ((NodeName) (env.lookup(genSym)));
            } else {
                nodeName = ((NodeName) expr);
            }

            String name = nodeName.getName();

            return env.lookup(name);
        }

        if (type == Node.Type.LIST) {
            return funApplication(env, (NodeList) expr);
        }
        return null;
    }

    private static Node funApplication(Env env, NodeList listExpr) throws LangException {

        if (isSpecialForm(listExpr)) {
            return specialApplication(env, listExpr);
        }

        // general function application
        return generalApplication(env, listExpr);
    }

    private static Node generalApplication(Env env, NodeList listExpr) throws LangException {
        // (fun args...)
        // ((lambda (x) (+ x x)) 4)

        List<Node> children = Node.asList(listExpr).getChildren();

        Iterator<Node> iter = children.iterator();

        Node fun = eval(env, iter.next());

        // fun is either a lambda or a name
        if (fun.getType() == Node.Type.NAME) {
            fun = env.lookup(Node.asNameValue(fun));
        }

        NodeLambda lambda = Node.asLambda(fun);

        List<Node> args = new ArrayList<Node>();
        while (iter.hasNext()) {
            args.add(eval(env, iter.next()));
        }

        // pass the eval'd args to lambda
        return lambda.execute(env, args);
    }

    private static Node specialApplication(Env env, NodeList listExpr) throws LangException {

        // 0th child is a NAME, eval will perform a lookup and return a SPECIAL
        Node n = eval(env, listExpr.getChildren().get(0));

        return Node.asSpecial(n).executeSpecial(env, listExpr);
    }

    private static boolean isSpecialForm(NodeList listExpr) throws LangException {
        List<Node> children = listExpr.getChildren();
        if (children.isEmpty()) {
            // is it even possible to have an empty list?
            //            throw new Exception();
        }

        Node first = children.get(0);

        if (first.getType() == Node.Type.LIST) {
            return false;
        }

        return sSpecialFormNames.contains(getCarName(listExpr));
    }

    // assuming that expr is a list with a name at the beginning,
    //  get the string value of (car expr)
    private static String getCarName(NodeList listExpr) throws LangException {

        List<Node> children = listExpr.getChildren();
        if (children.isEmpty()) {
            // is it even possible to have an empty list?
            //            throw new Exception();
        }

        Node first = children.get(0);

        return Node.asNameValue(first);
    }
}
