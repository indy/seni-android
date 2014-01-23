package io.indy.seni.lang.bind;


import java.util.List;

import io.indy.seni.lang.Binder;
import io.indy.seni.lang.Env;
import io.indy.seni.lang.Interpreter;
import io.indy.seni.lang.LangException;
import io.indy.seni.lang.Node;
import io.indy.seni.lang.NodeInt;
import io.indy.seni.lang.NodeList;
import io.indy.seni.lang.NodeSpecial;

public class Misc extends Binder {
    public static Env bind(Env e) {

        e.addBinding(new NodeSpecial("do-times") {
            public Node executeSpecial(Env env, NodeList listExpr)
                    throws LangException {
                       /*
           (do-times i 10
                     (setq br2 (* br2 shrink-factor))
                     (setq ang (* ang ang-delta))))
         */

                List<Node> children = listExpr.getChildren();
                if(children.size() < 4) {
                    // throw an error : do-times not in form: do-times var count statements+
                }

                String name = Node.asName(children.get(1)).getName();
                int iterations = Node.asInt(children.get(2)).getInt();

                int paramsSize = children.size();

                Env newScope = env.newScope();
                Node res = null;
                for (int i=0;i<iterations;i++) {
                    newScope.addBinding(name, new NodeInt(i));
                    for(int j=3;j<paramsSize;j++) {
                        res = Interpreter.eval(newScope, children.get(j));
                    }
                }

                return res;
            }
        });


        return e;
    }
}
