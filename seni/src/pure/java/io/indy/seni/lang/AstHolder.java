/*
 * Copyright 2014 Inderjit Gill
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

import java.util.List;
import java.util.Queue;

/*
  seni reserves names beginning with $ for internal use

  ast generates gensym names for alterable nodes

  ast is immutable and shared by multiple instances of astHolder

  astHolder - mAst: ast
              list of alterable nodes and their values for this instance
              sets of valid values for names (based on which family they're in)
              will create the env and populate it with gensym names bound to appropriate values


 */

public class AstHolder {

    private List<Node> mAst;       // immutable and shared by multiple instances of astHolder

    // list of alterable nodes and their values for this instance
    private Genotype mGenotype;

    private int mGenSymStart; // the starting int for SymbolGenerator

    public AstHolder(String script) {
        this(script, 42);
    }

    public AstHolder(String script, int genSymStart) {
        mGenSymStart = genSymStart;
        mAst = buildAst(script);
        // traverse the mAst for alterable nodes, add them into mAlterable
    }

    public AstHolder(AstHolder astHolder) {
        mAst = astHolder.mAst;
        // create a copy of mAlterable
    }

    // scribe all of the nodes in mAst
    public String scribe() throws Node.ScribeException {
        return scribe(mGenotype);
    }

    // scribe all of the nodes in mAst
    public String scribe(Genotype genotype) throws Node.ScribeException {
        Env env = genotype.bind(new Env());

        String res = "";
        for (Node ast : mAst) {
            res += ast.scribe(env);
        }
        return res;
    }

    public List<Node> getAst() {
        return mAst;
    }

    public Genotype getGenotype() {
        return mGenotype;
    }

    public static class UglyCopyException extends Exception {
        public UglyCopyException(String message) {
            super(message);
        }
    }

    /*
      Assign gensym values to the alterable 
      nodes and collect them in a genotype
     */
    private void addGenSyms(Node node, SymbolGenerator sg) {

        if (node.isAlterable()) {
            // only NodeMutate classes can be alterable
            NodeMutate nodeM = (NodeMutate)node;
            nodeM.setGenSym(sg.gen());
            mGenotype.add(nodeM.klone());
        }

        try {
            if (node.getType() == Node.Type.LIST) {
                List<Node> children = Node.asList(node).getChildren();
                ;
                for (Node n : children) {
                    addGenSyms(n, sg);
                }
            }
        } catch (LangException e) {
            e.printStackTrace();
        }
    }

    private List<Node> buildAst(String code) {

        try {
            Queue<Token> tokens = Lexer.tokenise(code);
            List<Node> ast = Parser.parse(tokens);

            // gensym alterable nodes in ast
            SymbolGenerator sg = new SymbolGenerator(mGenSymStart);
            mGenotype = new Genotype();
            for (Node node : ast) {
                addGenSyms(node, sg);
            }

            return ast;
        } catch (LangException e) {
            e.printStackTrace();
        }

        return null;
    }
}
