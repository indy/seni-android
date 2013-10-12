package io.indy.seni.lang;

import java.util.HashSet;
import java.util.List;

public class Interpreter {

    private static final String QUOTE = "quote";
    private static final String IF = "if";
    private static final String LET = "let";
    private static final String DEFINE = "define";
    private static final String SET = "set!";
    private static final String LAMBDA = "lambda";
    private static final String BEGIN = "begin";

    private static final NodeNull NODE_NULL = new NodeNull();

    HashSet<String> mSpecialFormNames;

    public Interpreter() {
        setupSpecialFormNames();
    }
    
    public Node eval(Env env, Node expr) {
        
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

    private Node funApplication(Env env, NodeList listExpr) {

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

        } else {
            // general function application
        }

        return null;
    }

    private Node specialFormQuote(Env env, NodeList listExpr) {
        return listExpr.getChildren().get(1);
    }

    private Node specialFormIf(Env env, NodeList listExpr) {

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

    private Node specialFormSet(Env env, NodeList listExpr) {
        // (set! foo 42)

        List<Node> children = listExpr.getChildren();
        if(children.size() != 3) {
            // throw an error : set! not in form: set! name value
        }

        Node var = children.get(1);
        if (var.getType() != Node.Type.NAME) {
            // throw an error : expected a name node
        }

        // mutate the current env
        //
        String name = ((NodeName)var).getName();
        Node value = children.get(2);
        env.addBinding(name, value);

        return value;
    }

    private boolean isSpecialForm(NodeList listExpr) {
        return mSpecialFormNames.contains(getCarName(listExpr));
    }

    // assuming that expr is a list with a name at the beginning,
    //  get the string value of (car expr)
    private String getCarName(NodeList listExpr) {

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

    private void setupSpecialFormNames() {
        mSpecialFormNames = new HashSet<String>(8);

        mSpecialFormNames.add(QUOTE);
        mSpecialFormNames.add(IF);
        mSpecialFormNames.add(LET);
        mSpecialFormNames.add(LAMBDA);
        mSpecialFormNames.add(BEGIN);
        mSpecialFormNames.add(DEFINE);
        mSpecialFormNames.add(SET);

        //mSpecialFormNames.add("with-colour");
        //mSpecialFormNames.add("--debug-hook");
    }

}
