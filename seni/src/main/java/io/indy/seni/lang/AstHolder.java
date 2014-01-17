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

import java.util.ArrayList;
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
    private List<Alterable> mAlterable; // list of alterable nodes and their values for this instance

    // todo: build an env


    public AstHolder(String code) {
        mAst = buildAst(code);
        // traverse the mAst for alterable nodes, add them into mAlterable
    }

    public AstHolder(AstHolder astHolder) {
        mAst = astHolder.mAst;
        // create a copy of mAlterable
    }

    // todo: rename alterable to something more genetically accurate
    public Env addAlterableBindings(Env env) {
        Env e = env.newScope();

        // for each node in mAlterable
        //   addBinding gensym -> value in mAlterable[node]
        //e.addBinding("", );

        return e;
    }

    public static List<Node> buildAlterableList(List<Node> ast) {
        return null;
    }

    private void addGenSyms(Node node, SymbolGenerator sg) {

        if(node.isAlterable()) {
            String genSym = sg.gen();
            node.setGenSym(genSym);
            mAlterable.add(Alterable.fromNode(node, genSym));
        }
        
        try {
            if(node.getType() == Node.Type.LIST) {
                List<Node> children = Node.asList(node).getChildren();;
                for(Node n : children) {
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
            SymbolGenerator sg = new SymbolGenerator();
            mAlterable = new ArrayList<Alterable>();
            for(Node node : ast) {
                addGenSyms(node, sg);
            }
            
            return ast;
        } catch (LangException e) {
            e.printStackTrace();
        }

        return null;
    }
}
