package io.indy.seni.lang.bind;

import java.util.ArrayList;
import java.util.List;

import io.indy.seni.lang.Binder;
import io.indy.seni.lang.Env;
import io.indy.seni.lang.Interpreter;
import io.indy.seni.lang.LangException;
import io.indy.seni.lang.Node;
import io.indy.seni.lang.NodeBoolean;
import io.indy.seni.lang.NodeLambda;
import io.indy.seni.lang.NodeList;
import io.indy.seni.lang.NodeName;
import io.indy.seni.lang.NodeSpecial;

public class Special extends Binder {

    public static Env bind(Env e) {

        e.addBinding(new NodeSpecial("quote") {
            public Node executeSpecial(Env env, NodeList listExpr)
                    throws LangException {
                return listExpr.getChildren().get(1);
            }
        });

        // if
        e.addBinding(new NodeSpecial("if") {
            public Node executeSpecial(Env env, NodeList listExpr)
                    throws LangException {

                // TODO: check that there are only 3 or 4 forms in expr

                // eval conditional
                Node conditional = Interpreter.eval(env, listExpr.getChildren().get(1));

                // currently assuming that conditional will be a boolean type
                // (TODO: revise this assumption in the future)
                if (conditional.getType() != Node.Type.BOOLEAN) {
                    // throw a not boolean exception
                }

                NodeBoolean cond = (NodeBoolean) conditional;
                if (cond.getBoolean()) {
                    return Interpreter.eval(env, listExpr.getChildren().get(2));
                } else {
                    if (listExpr.getChildren().size() == 4) {
                        return Interpreter.eval(env, listExpr.getChildren().get(3));
                    }
                }

                return Interpreter.NODE_NULL;
            }
        });

        // let
        e.addBinding(new NodeSpecial("let") {
            public Node executeSpecial(Env env, NodeList listExpr)
                    throws LangException {
                // (let ((x 2) (y 3)) (+ x y) .... )

                int i = 0;
                List<Node> children = listExpr.getChildren();

                if (children.size() < 2) {
                    // throw an error: malformed let
                }

                Env scopedEnv = env.newScope();

                NodeList newVars = Node.asList(children.get(1));  // ((x 2) (y 3))
                for (i = 0; i < newVars.getChildren().size(); i++) {
                    NodeList nl = Node.asList(newVars.getChild(i));
                    if (nl.getChildren().size() != 2) {
                        // throw an error: let binding not part of a pair
                    }

                    scopedEnv.addBinding(Node.asName(nl.getChild(0)).getName(),
                            Interpreter.eval(scopedEnv, nl.getChild(1)));
                }

                Node res = Interpreter.NODE_NULL;
                for (i = 2; i < children.size(); i++) {
                    res = Interpreter.eval(scopedEnv, children.get(i));
                }

                return res;
            }
        });


        // define
        e.addBinding(new NodeSpecial("define") {
            public Node executeSpecial(Env env, NodeList listExpr)
                    throws LangException {
                // (define bar 99)

                List<Node> children = listExpr.getChildren();
                if (children.size() != 3) {
                    // throw an error : define not in form: define name value
                }

                NodeName var = Node.asName(children.get(1));

                if (env.hasBinding(var.getName())) {
                    // throw an error : var already defined
                }

                Node value = Interpreter.eval(env, children.get(2));
                env.addBinding(var.getName(), value);

                return value;
            }
        });


        // set!
        e.addBinding(new NodeSpecial("set!") {
            public Node executeSpecial(Env env, NodeList listExpr)
                    throws LangException {
                // (set! foo 42)

                List<Node> children = listExpr.getChildren();
                if (children.size() != 3) {
                    // throw an error : set! not in form: set! name value
                }

                NodeName var = Node.asName(children.get(1));
                Node value = Interpreter.eval(env, children.get(2));

                // mutate the current env
                //
                env.addBinding(var.getName(), value);

                return value;
            }
        });


        // lambda
        e.addBinding(new NodeSpecial("lambda") {
            public Node executeSpecial(Env env, NodeList listExpr)
                    throws LangException {
                // (lambda (x y) (+ x y))

                // (lambda (x y) 5)

                List<Node> children = listExpr.getChildren();

                List<String> args = new ArrayList<>();

                Node argNodes = children.get(1);

                for (Node child : Node.asList(argNodes).getChildren()) {
                    args.add(Node.asNameValue(child));
                }

                return new NodeLambda(args, children.get(2));
            }
        });


        e.addBinding(new NodeSpecial("begin") {
            public Node executeSpecial(Env env, NodeList listExpr)
                    throws LangException {

                List<Node> children = listExpr.getChildren();
                int childrenSize = children.size();

                Node res = Interpreter.NODE_NULL;
                // todo: replace with the more efficient iterator
                for (int i = 1; i < childrenSize; i++) {
                    res = Interpreter.eval(env, children.get(i));
                }
                return res;
            }
        });

        return e;
    }
}
