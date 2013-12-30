package io.indy.seni.runtime;

import android.util.Log;

import java.util.List;
import java.util.Queue;

import io.indy.seni.AppConfig;
import io.indy.seni.lang.Env;
import io.indy.seni.lang.Interpreter;
import io.indy.seni.lang.LangException;
import io.indy.seni.lang.Lexer;
import io.indy.seni.lang.Node;
import io.indy.seni.lang.Parser;
import io.indy.seni.lang.Token;
import io.indy.seni.runtime.bind.Platform;

public class Runtime {

    private static final String TAG = "Runtime";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    public Runtime() {

    }


    public List<Node> asAst(String code) {

        Queue<Token> tokens;

        try {
            tokens = Lexer.tokenise(code);
            return Parser.parse(tokens);
        } catch (LangException e) {
            ifd("exception: asAST " + e);
            e.printStackTrace();
        }

        return null;
    }

    public Env bindCoreFunctions(Env env) {
        return Env.bindCoreFuns(env);
    }

    /**
     * Bind the functions which require a valid SeniContext
     *
     * @return the Env with bound with platform specific vars
     */
    public Env bindPlatformFunctions(Env env, SeniContext sc) {
        return Platform.bind(env, sc);
    }

    // runs single s-expressions
    public Node run(String code) {

        try {
            List<Node> ast = asAst(code);
            Env e = Env.bindCoreFuns(new Env());

            Node res = null;

            for (Node node : ast) {
                res = Interpreter.eval(e, node);
            }

            return res;

        } catch (LangException exception) {
//            assertThat(true).overridingErrorMessage(exception.toString()).isFalse();
        }

        return null;
    }

}
