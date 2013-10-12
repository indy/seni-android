package io.indy.seni.lang;

import org.junit.Test;
import org.junit.Before;
import static org.fest.assertions.api.Assertions.assertThat;


public class InterpreterTest {

    private Interpreter mInterpreter;
    private Env mEnv;

    @Before
    public void initObjects() {
        mInterpreter = new Interpreter();
        mEnv = new Env();
    }

    @Test
    public void testBasicEval() {

        NodeInt nInt = new NodeInt(42);
        assertThat(mInterpreter.eval(null, nInt)).isEqualTo(nInt);        

        NodeFloat nFloat = new NodeFloat(3.14f);
        assertThat(mInterpreter.eval(null, nFloat)).isEqualTo(nFloat);        

        NodeString nString = new NodeString("pish");
        assertThat(mInterpreter.eval(null, nString)).isEqualTo(nString);        

        NodeBoolean nBoolean = new NodeBoolean(true);
        assertThat(mInterpreter.eval(null, nBoolean)).isEqualTo(nBoolean);
    }

    @Test
    public void testLookupEval() {

        String var = "foo";
        NodeInt nInt = new NodeInt(42);

        mEnv.addBinding(var, nInt);
        assertThat(mInterpreter.eval(mEnv, new NodeName(var))).isEqualTo(nInt);
    }

    @Test
    public void testSpecialFormQuote() {
        
        // (quote foo) => foo
        NodeList nList = new NodeList();

        nList.addChild(new NodeName("quote"));

        NodeName nName = new NodeName("foo");
        nList.addChild(nName);

        assertThat(mInterpreter.eval(null, nList)).isEqualTo(nName);
    }


    @Test
    public void testSpecialFormIf() {

        NodeInt two = new NodeInt(2);
        NodeInt four = new NodeInt(4);

        // (if true 2 4) => 2
        NodeList nList = new NodeList();
        nList.addChild(new NodeName("if"));
        nList.addChild(new NodeBoolean(true));
        nList.addChild(two);
        nList.addChild(four);

        assertThat(mInterpreter.eval(null, nList)).isEqualTo(two);


        // (if false 2 4) => 4
        nList = new NodeList();
        nList.addChild(new NodeName("if"));
        nList.addChild(new NodeBoolean(false));
        nList.addChild(two);
        nList.addChild(four);

        assertThat(mInterpreter.eval(null, nList)).isEqualTo(four);
    }

    @Test
    public void testSpecialFormSet() {

        // set! requires an existing binding
        mEnv.addBinding("foo", new NodeString("an old value"));

        // (set! foo 45)
        NodeList nList = new NodeList();
        nList.addChild(new NodeName("set!"));
        nList.addChild(new NodeName("foo"));
        nList.addChild(new NodeInt(45));

        mInterpreter.eval(mEnv, nList);

        Node n = mEnv.lookup("foo");
        assertThat(n.getType()).isEqualTo(Node.Type.INT);
        assertThat(((NodeInt)n).getInt()).isEqualTo(45);
    }
}
