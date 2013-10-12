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
    
    public static Node eval(Env env, Node expr) {
        
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

    private static Node funApplication(Env env, NodeList listExpr) {

        if (isSpecialForm(listExpr)) {
            // deal with special forms
            String name = getCarName(listExpr);

            if (name.equals(QUOTE)) { 
                return specialFormQuote(env, listExpr);
            }

            if (name.equals(IF)) {
                return specialFormIf(env, listExpr);
            }

            if (name.equals(SET)) {
                return specialFormSet(env, listExpr);
            }

            if (name.equals(DEFINE)) {
                return specialFormDefine(env, listExpr);
            }

            if (name.equals(BEGIN)) {
                return specialFormBegin(env, listExpr);
            }

            if (name.equals(LAMBDA)) {
                return specialFormLambda(env, listExpr);
            }

            // throw an error

        } 

        // general function application
            
        // (fun args...)
        // ((lambda (x) (+ x x)) 4)

        List<Node> children = asNodeList(listExpr).getChildren();

        Iterator<Node> iter = children.iterator();
            
        Node fun = iter.next();

        // fun is either a lambda or a name
        if(fun.getType() == Node.Type.NAME) {
            fun = env.lookup(asNodeName(fun).getName());
        }

        NodeLambda lambda = asNodeLambda(fun);

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

    private static Node specialFormIf(Env env, NodeList listExpr) {

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

    private static Node specialFormSet(Env env, NodeList listExpr) {
        // (set! foo 42)

        List<Node> children = listExpr.getChildren();
        if(children.size() != 3) {
            // throw an error : set! not in form: set! name value
        }

        NodeName var = asNodeName(children.get(1));
        Node value = eval(env, children.get(2));

        // mutate the current env
        //
        env.addBinding(var.getName(), value);

        return value;
    }

    private static Node specialFormDefine(Env env, NodeList listExpr) {
        // (define bar 99)

        List<Node> children = listExpr.getChildren();
        if(children.size() != 3) {
            // throw an error : define not in form: define name value
        }

        NodeName var = asNodeName(children.get(1));

        if (env.hasBinding(var.getName())) {
            // throw an error : var already defined
        }

        Node value = eval(env, children.get(2));
        env.addBinding(var.getName(), value);

        return value;
    }

    private static Node specialFormBegin(Env env, NodeList listExpr) {
        // (begin (set! bar 99) (define foo 11) (set! bar 42))

        List<Node> children = listExpr.getChildren();

        Node res = NODE_NULL;
        for (int i=1;i<children.size();i++) {
            res = eval(env, children.get(i));
        }

        return res;
    }

    private static Node specialFormLambda(Env env, NodeList listExpr) {
        // (lambda (x y) (+ x y))

        // (lambda (x y) 5)

        List<Node> children = listExpr.getChildren();

        List<String> args = new ArrayList<String>();

        Node argNodes = children.get(1);

        for (Node child : asNodeList(argNodes).getChildren()) {
            args.add(asNodeName(child).getName());
        }

        return new NodeLambda(args, children.get(2));
    }

    private static NodeName asNodeName(Node n) {
        if (n.getType() != Node.Type.NAME) {
            // throw an error
        }

        return (NodeName) n;
    }

    private static NodeList asNodeList(Node n) {
        if (n.getType() != Node.Type.LIST) {
            // throw an error
        }

        return (NodeList) n;
    }

    private static NodeLambda asNodeLambda(Node n) {
        if (n.getType() != Node.Type.LAMBDA) {
            // throw an error
        }

        return (NodeLambda) n;
    }

    private static boolean isSpecialForm(NodeList listExpr) {
        return sSpecialFormNames.contains(getCarName(listExpr));
    }

    // assuming that expr is a list with a name at the beginning,
    //  get the string value of (car expr)
    private static String getCarName(NodeList listExpr) {

        List<Node> children = listExpr.getChildren();
        if (children.isEmpty()) {
            // is it even possible to have an empty list?
            //            throw new Exception();
        }

        Node first = children.get(0);

        // first should be a name node
        if(first.getType() != Node.Type.NAME) {
            //            throw new Exception();
        }

        return ((NodeName) first).getName();
    }


}
