package io.indy.seni.runtime;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.List;
import java.util.Queue;

import io.indy.seni.AppConfig;
import io.indy.seni.lang.Env;
import io.indy.seni.lang.Interpreter;
import io.indy.seni.lang.LangException;
import io.indy.seni.lang.Lexer;
import io.indy.seni.lang.Node;
import io.indy.seni.lang.NodeFloat;
import io.indy.seni.lang.Parser;
import io.indy.seni.lang.Token;
import io.indy.seni.runtime.bind.Platform;

public class SeniRuntime {

    private static final String TAG = "SeniRuntime";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }


    public static void render(String script, Canvas canvas, int width, int height) {

        SeniContext sc = new SeniContext(canvas);
        Paint paint = sc.getPaint();
        paint.setARGB(250, 250, 0, 0);

        ifd("width " + width);
        ifd("height " + height);

        SeniRuntime rt = new SeniRuntime();

        Env env = new Env();
        env = rt.bindCoreFunctions(env);
        env = rt.bindPlatformFunctions(env, sc);

        env = env.addBinding("canvas-width", new NodeFloat((float)width));
        env = env.addBinding("canvas-height", new NodeFloat((float)height));

        List<Node> ast = rt.asAst(script);

        try {
            for (Node node : ast) {
                Interpreter.eval(env, node);
            }
        } catch(LangException e) {
            ifd("exception: " + e);
            e.printStackTrace();
        }
    }

    public SeniRuntime() {

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
