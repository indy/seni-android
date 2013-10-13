package io.indy.seni.lang;

import org.junit.Test;
import org.junit.Before;
import static org.fest.assertions.api.Assertions.assertThat;


public class InterpreterTest {

    // private Interpreter mInterpreter;
    private Env mEnv;

    @Before
    public void initObjects() {
        // mInterpreter = new Interpreter();
        mEnv = new Env();
    }

    @Test
    public void testBasicEval() {

        try {
            NodeInt nInt = new NodeInt(42);
            assertThat(Interpreter.eval(null, nInt)).isEqualTo(nInt);        

            NodeFloat nFloat = new NodeFloat(3.14f);
            assertThat(Interpreter.eval(null, nFloat)).isEqualTo(nFloat);        

            NodeString nString = new NodeString("pish");
            assertThat(Interpreter.eval(null, nString)).isEqualTo(nString);        

            NodeBoolean nBoolean = new NodeBoolean(true);
            assertThat(Interpreter.eval(null, nBoolean)).isEqualTo(nBoolean);
        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void testLookupEval() {
        try {
            String var = "foo";
            NodeInt nInt = new NodeInt(42);

            mEnv.addBinding(var, nInt);
            assertThat(Interpreter.eval(mEnv, new NodeName(var))).isEqualTo(nInt);
        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void testSpecialFormQuote() {
        try {        
            // (quote foo) => foo
            NodeList nList = new NodeList();

            nList.addChild(new NodeName("quote"));

            NodeName nName = new NodeName("foo");
            nList.addChild(nName);

            assertThat(Interpreter.eval(null, nList)).isEqualTo(nName);
        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }


    @Test
    public void testSpecialFormIf() {
        try {
            NodeInt two = new NodeInt(2);
            NodeInt four = new NodeInt(4);

            // (if true 2 4) => 2
            NodeList nList = new NodeList();
            nList.addChild(new NodeName("if"));
            nList.addChild(new NodeBoolean(true));
            nList.addChild(two);
            nList.addChild(four);

            assertThat(Interpreter.eval(null, nList)).isEqualTo(two);


            // (if false 2 4) => 4
            nList = new NodeList();
            nList.addChild(new NodeName("if"));
            nList.addChild(new NodeBoolean(false));
            nList.addChild(two);
            nList.addChild(four);

            assertThat(Interpreter.eval(null, nList)).isEqualTo(four);
        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void testSpecialFormSet() {
        try {
            // set! requires an existing binding
            mEnv.addBinding("foo", new NodeString("an old value"));

            // (set! foo 45)
            NodeList nList = new NodeList();
            nList.addChild(new NodeName("set!"));
            nList.addChild(new NodeName("foo"));
            nList.addChild(new NodeInt(45));

            Interpreter.eval(mEnv, nList);

            Node n = mEnv.lookup("foo");
            assertThat(n.getType()).isEqualTo(Node.Type.INT);
            assertThat(((NodeInt)n).getInt()).isEqualTo(45);
        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void testSpecialFormDefine() {
        try {
            // (define bar 99)
            NodeList nList = new NodeList();
            nList.addChild(new NodeName("define"));
            nList.addChild(new NodeName("bar"));
            nList.addChild(new NodeInt(99));

            Interpreter.eval(mEnv, nList);

            Node n = mEnv.lookup("bar");
            assertThat(n.getType()).isEqualTo(Node.Type.INT);
            assertThat(((NodeInt)n).getInt()).isEqualTo(99);
        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void testSpecialFormBegin() {
        try {
            // (begin (set! bar 99) (define foo 11) (set! bar 42))

            mEnv.addBinding("bar", new NodeString("previous"));

            NodeList nListA = new NodeList();
            nListA.addChild(new NodeName("set!"));
            nListA.addChild(new NodeName("bar"));
            nListA.addChild(new NodeInt(99));

            NodeList nListB = new NodeList();
            nListB.addChild(new NodeName("define"));
            nListB.addChild(new NodeName("foo"));
            nListB.addChild(new NodeInt(11));

            NodeList nListC = new NodeList();
            nListC.addChild(new NodeName("set!"));
            nListC.addChild(new NodeName("bar"));
            nListC.addChild(new NodeInt(42));

            NodeList nList = new NodeList();
            nList.addChild(new NodeName("begin"));
            nList.addChild(nListA);
            nList.addChild(nListB);
            nList.addChild(nListC);

            Interpreter.eval(mEnv, nList);

            Node n = mEnv.lookup("bar");
            assertThat(n.getType()).isEqualTo(Node.Type.INT);
            assertThat(((NodeInt)n).getInt()).isEqualTo(42);

            n = mEnv.lookup("foo");
            assertThat(n.getType()).isEqualTo(Node.Type.INT);
            assertThat(((NodeInt)n).getInt()).isEqualTo(11);
        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void testCoreFunPlusInt() {
        try {
            Env e = Env.bindCoreFuns(mEnv);

            // (+ 3 4)
            NodeList nList = new NodeList();
            nList.addChild(new NodeName("+"));
            nList.addChild(new NodeInt(3));
            nList.addChild(new NodeInt(4));

            Node n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.INT);
            assertThat(((NodeInt)n).getInt()).isEqualTo(7);


            // (+ 3 4 5 6)
            nList.addChild(new NodeInt(5));
            nList.addChild(new NodeInt(6));

            n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.INT);
            assertThat(((NodeInt)n).getInt()).isEqualTo(18);
        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void testCoreFunPlusFloat() {
        try {
            Env e = Env.bindCoreFuns(mEnv);

            // (+ 3.0 4.0)
            NodeList nList = new NodeList();
            nList.addChild(new NodeName("+"));
            nList.addChild(new NodeFloat(3.0f));
            nList.addChild(new NodeFloat(4.0f));

            Node n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.FLOAT);
            assertThat(((NodeFloat)n).getFloat()).isEqualTo(7.0f);


            // (+ 3.0 4.0 5.0 6.0)
            nList.addChild(new NodeFloat(5.0f));
            nList.addChild(new NodeFloat(6.0f));

            n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.FLOAT);
            assertThat(((NodeFloat)n).getFloat()).isEqualTo(18.0f);
        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void testCoreFunMinusInt() {
        try {
            Env e = Env.bindCoreFuns(mEnv);

            // (- 3 4)
            NodeList nList = new NodeList();
            nList.addChild(new NodeName("-"));
            nList.addChild(new NodeInt(3));
            nList.addChild(new NodeInt(4));

            Node n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.INT);
            assertThat(((NodeInt)n).getInt()).isEqualTo(-1);

            // (- 3 4 5 6)
            nList.addChild(new NodeInt(5));
            nList.addChild(new NodeInt(6));

            n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.INT);
            assertThat(((NodeInt)n).getInt()).isEqualTo(-12);
        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void testCoreFunMinusFloat() {
        try {
            Env e = Env.bindCoreFuns(mEnv);

            // (- 3.0 4.0)
            NodeList nList = new NodeList();
            nList.addChild(new NodeName("-"));
            nList.addChild(new NodeFloat(3.0f));
            nList.addChild(new NodeFloat(4.0f));

            Node n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.FLOAT);
            assertThat(((NodeFloat)n).getFloat()).isEqualTo(-1.0f);

            // (- 3.0 4.0 5.0 6.0)
            nList.addChild(new NodeFloat(5.0f));
            nList.addChild(new NodeFloat(6.0f));

            n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.FLOAT);
            assertThat(((NodeFloat)n).getFloat()).isEqualTo(-12.0f);
        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void testCoreFunDivideInt() {
        try {
            Env e = Env.bindCoreFuns(mEnv);

            // (/ 24 2)
            NodeList nList = new NodeList();
            nList.addChild(new NodeName("/"));
            nList.addChild(new NodeInt(24));
            nList.addChild(new NodeInt(2));

            Node n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.INT);
            assertThat(((NodeInt)n).getInt()).isEqualTo(12);

            // (/ 24 2 2)
            nList.addChild(new NodeInt(2));

            n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.INT);
            assertThat(((NodeInt)n).getInt()).isEqualTo(6);
        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void testCoreFunDivideFloat() {
        try {
            Env e = Env.bindCoreFuns(mEnv);

            // (/ 24.0 2.0)
            NodeList nList = new NodeList();
            nList.addChild(new NodeName("/"));
            nList.addChild(new NodeFloat(24.0f));
            nList.addChild(new NodeFloat(2.0f));

            Node n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.FLOAT);
            assertThat(((NodeFloat)n).getFloat()).isEqualTo(12.0f);

            // (/ 24 2 2)
            nList.addChild(new NodeFloat(2.0f));

            n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.FLOAT);
            assertThat(((NodeFloat)n).getFloat()).isEqualTo(6.0f);
        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void testCoreFunMultiplyInt() {
        try {
            Env e = Env.bindCoreFuns(mEnv);

            // (* 24 2)
            NodeList nList = new NodeList();
            nList.addChild(new NodeName("*"));
            nList.addChild(new NodeInt(24));
            nList.addChild(new NodeInt(2));

            Node n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.INT);
            assertThat(((NodeInt)n).getInt()).isEqualTo(48);

            // (* 24 2 2)
            nList.addChild(new NodeInt(2));

            n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.INT);
            assertThat(((NodeInt)n).getInt()).isEqualTo(96);
        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void testCoreFunMultiplyFloat() {
        try {
            Env e = Env.bindCoreFuns(mEnv);

            // (* 24.0 2.0)
            NodeList nList = new NodeList();
            nList.addChild(new NodeName("*"));
            nList.addChild(new NodeFloat(24.0f));
            nList.addChild(new NodeFloat(2.0f));

            Node n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.FLOAT);
            assertThat(((NodeFloat)n).getFloat()).isEqualTo(48.0f);

            // (* 24 2 2)
            nList.addChild(new NodeFloat(2.0f));

            n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.FLOAT);
            assertThat(((NodeFloat)n).getFloat()).isEqualTo(96.0f);
        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }



    @Test
    public void testCoreFunLessInt() {
        try {
            Env e = Env.bindCoreFuns(mEnv);

            // (< 3 4)
            NodeList nList = new NodeList();
            nList.addChild(new NodeName("<"));
            nList.addChild(new NodeInt(3));
            nList.addChild(new NodeInt(4));

            Node n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.BOOLEAN);
            assertThat(((NodeBoolean)n).getBoolean()).isEqualTo(true);

        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void testCoreFunLessFloat() {
        try {
            Env e = Env.bindCoreFuns(mEnv);

            // (< 3.0 4.0)
            NodeList nList = new NodeList();
            nList.addChild(new NodeName("<"));
            nList.addChild(new NodeFloat(3.0f));
            nList.addChild(new NodeFloat(4.0f));

            Node n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.BOOLEAN);
            assertThat(((NodeBoolean)n).getBoolean()).isEqualTo(true);

        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }


    @Test
    public void testCoreFunMoreInt() {
        try {
            Env e = Env.bindCoreFuns(mEnv);

            // (> 4 3)
            NodeList nList = new NodeList();
            nList.addChild(new NodeName(">"));
            nList.addChild(new NodeInt(4));
            nList.addChild(new NodeInt(3));

            Node n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.BOOLEAN);
            assertThat(((NodeBoolean)n).getBoolean()).isEqualTo(true);

        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void testCoreFunMoreFloat() {
        try {
            Env e = Env.bindCoreFuns(mEnv);

            // (> 4.0 3.0)
            NodeList nList = new NodeList();
            nList.addChild(new NodeName(">"));
            nList.addChild(new NodeFloat(4.0f));
            nList.addChild(new NodeFloat(3.0f));

            Node n = Interpreter.eval(e, nList);
            assertThat(n.getType()).isEqualTo(Node.Type.BOOLEAN);
            assertThat(((NodeBoolean)n).getBoolean()).isEqualTo(true);

        } catch(LangException e) {
            assertThat(true).isEqualTo(false);
        }
    }

}
