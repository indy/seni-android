package io.indy.seni.lang;

import java.util.Iterator;
import java.util.List;

public class NodeLambda extends Node {

    protected List<String> mArgs;
    protected Node mBody;

    public NodeLambda(List<String> args, Node body) {
        super();

        mType = Node.Type.LAMBDA;
        mArgs = args;
        mBody = body;
    }

    public List<String> getArgs() {
        return mArgs;
    }

    public Node getBody() {
        return mBody;
    }

    public Node execute(Env env, List<Node> params) throws LangException {

        // the params have already been eval'd

        if(mArgs.size() != params.size()) {
            // throw an exception
        }

        // bind params to a new scope
        Env scoped = bindParams(env.newScope(), params);

        // evaluate
        return Interpreter.eval(scoped, mBody);
    }

    protected Env bindParams(Env env, List<Node> params) {
        // bind params to mArgs
        Iterator<String> argIter = mArgs.iterator();
        Iterator<Node> paramIter = params.iterator();
        while(argIter.hasNext()) {
            env.addBinding(argIter.next(), paramIter.next());
        }

        return env;
    }
}
