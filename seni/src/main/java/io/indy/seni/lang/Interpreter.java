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
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

public class Interpreter {

    private static final String QUOTE = "quote";
    private static final String IF = "if";
    private static final String LET = "let";
    private static final String DEFINE = "define";
    private static final String SET = "set!";
    private static final String LAMBDA = "lambda";
    private static final String BEGIN = "begin";

    private static final NodeNull NODE_NULL = new NodeNull();

    private static HashSet<String> sSpecialFormNames;

    static {
        sSpecialFormNames = new HashSet<String>(8);

        sSpecialFormNames.add(QUOTE);
        sSpecialFormNames.add(IF);
        sSpecialFormNames.add(LET);
        sSpecialFormNames.add(LAMBDA);
        sSpecialFormNames.add(BEGIN);
        sSpecialFormNames.add(DEFINE);
        sSpecialFormNames.add(SET);
    }
    
    public static Node eval(Env env, Node expr) throws LangException {
        
        Node.Type type = expr.getType();

        if(type == Node.Type.INT) {
            return expr;
        }

        if(type == Node.Type.FLOAT) {
            return expr;
        }

        if(type == Node.Type.BOOLEAN) {
            return expr;
        }

        if(type == Node.Type.COLOUR) {
            return expr;
        }

        if(type == Node.Type.STRING) {
            return expr;
        }

        if(type == Node.Type.NULL) {
            return expr;
        }

        if(type == Node.Type.NAME) {
            return env.lookup(((NodeName)expr).getName());
        }

        if(type == Node.Type.LIST) {
            return funApplication(env, (NodeList) expr);
        }
        return null;
    }

    private static Node funApplication(Env env, NodeList listExpr) throws LangException {

        if (isSpecialForm(listExpr)) {
            switch(getCarName(listExpr)) {
            case QUOTE:
                return specialFormQuote(env, listExpr);
            case IF:
                return specialFormIf(env, listExpr);
            case SET:
                return specialFormSet(env, listExpr);
            case DEFINE:
                return specialFormDefine(env, listExpr);
            case BEGIN:
                return specialFormBegin(env, listExpr);
            case LAMBDA:
                return specialFormLambda(env, listExpr);
            default:
                // throw an error
            };
        } 

        // general function application
            
        // (fun args...)
        // ((lambda (x) (+ x x)) 4)

        List<Node> children = Node.asList(listExpr).getChildren();

        Iterator<Node> iter = children.iterator();
            
        Node fun = eval(env, iter.next());

        // fun is either a lambda or a name
        if(fun.getType() == Node.Type.NAME) {
            fun = env.lookup(Node.asNameValue(fun));
        }

        NodeLambda lambda = Node.asLambda(fun);

        List<Node> args = new ArrayList<Node>();
        while(iter.hasNext()) {
            args.add(eval(env, iter.next()));
        }

        // pass the eval'd args to lambda
        return lambda.execute(env, args);
    }

    private static  Node specialFormQuote(Env env, NodeList listExpr) {
        return listExpr.getChildren().get(1);
    }

    private static Node specialFormIf(Env env, NodeList listExpr) throws LangException {

        // TODO: check that there are only 3 or 4 forms in expr

        // eval conditional
        Node conditional = eval(env, listExpr.getChildren().get(1));

        // currently assuming that conditional will be a boolean type
        // (TODO: revise this assumption in the future)
        if (conditional.getType() != Node.Type.BOOLEAN) {
            // throw a not boolean exception
        }

        NodeBoolean cond = (NodeBoolean) conditional;
        if(cond.getBoolean()) {
            return eval(env, listExpr.getChildren().get(2));
        } else {
            if (listExpr.getChildren().size() == 4) {
                return eval(env, listExpr.getChildren().get(3));
            }
        }

        return NODE_NULL;
    }

    private static Node specialFormSet(Env env, NodeList listExpr) throws LangException {
        // (set! foo 42)

        List<Node> children = listExpr.getChildren();
        if(children.size() != 3) {
            // throw an error : set! not in form: set! name value
        }

        NodeName var = Node.asName(children.get(1));
        Node value = eval(env, children.get(2));

        // mutate the current env
        //
        env.addBinding(var.getName(), value);

        return value;
    }

    private static Node specialFormDefine(Env env, NodeList listExpr) throws LangException {
        // (define bar 99)

        List<Node> children = listExpr.getChildren();
        if(children.size() != 3) {
            // throw an error : define not in form: define name value
        }

        NodeName var = Node.asName(children.get(1));

        if (env.hasBinding(var.getName())) {
            // throw an error : var already defined
        }

        Node value = eval(env, children.get(2));
        env.addBinding(var.getName(), value);

        return value;
    }

    private static Node specialFormBegin(Env env, NodeList listExpr) throws LangException {
        // (begin (set! bar 99) (define foo 11) (set! bar 42))

        List<Node> children = listExpr.getChildren();

        Node res = NODE_NULL;
        for (int i=1;i<children.size();i++) {
            res = eval(env, children.get(i));
        }

        return res;
    }

    private static Node specialFormLambda(Env env, NodeList listExpr) throws LangException {
        // (lambda (x y) (+ x y))

        // (lambda (x y) 5)

        List<Node> children = listExpr.getChildren();

        List<String> args = new ArrayList<String>();

        Node argNodes = children.get(1);

        for (Node child : Node.asList(argNodes).getChildren()) {
            args.add(Node.asNameValue(child));
        }

        return new NodeLambda(args, children.get(2));
    }

    private static boolean isSpecialForm(NodeList listExpr) throws LangException {
        List<Node> children = listExpr.getChildren();
        if (children.isEmpty()) {
            // is it even possible to have an empty list?
            //            throw new Exception();
        }

        Node first = children.get(0);

        if(first.getType() == Node.Type.LIST) {
            // e.g. ((lambda (x) (+ x x)) 3)
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
