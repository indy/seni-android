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

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.HashMap;
import java.util.Map;

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

    private static final String METADATA = "metadata";
    private static final String CONFIGURE = "configure";

    private List<Node> mAst;       // immutable and shared by multiple instances of astHolder

    private Node mMetadataNode; // for scribing
    private Map<String, Node> mMetadata;

    private Node mConfigurationNode; // for scribing
    private Map<String, Node> mConfiguration;

    // list of alterable nodes and their values for this instance
    private Genotype mGenotype;

    private int mGenSymStart; // the starting int for SymbolGenerator

    public AstHolder(String script) {
        this(script, 42);
    }

    public AstHolder(String script, int genSymStart) {
        mGenSymStart = genSymStart;
        mAst = buildAst(script);

        mMetadata = extractPairForm(mAst, METADATA);
        mMetadataNode = mMetadata.get(METADATA);

        mConfiguration = extractPairForm(mAst, CONFIGURE);
        mConfigurationNode = mConfiguration.get(CONFIGURE);
    }

    public AstHolder(AstHolder astHolder) {
        mAst = astHolder.mAst;
        // create a copy of mAlterable
        // create a copu of metadata
        // create a copu of configuration
    }

    public Node getMetadata(String key) {
        return mMetadata.get(key);
    }

    public Node getConfiguration(String key) {
        return mConfiguration.get(key);
    }

    // scribe all of the nodes in mAst
    public String scribe() throws Node.ScribeException {
        return scribe(mGenotype);
    }

    // scribe all of the nodes in mAst
    public String scribe(Genotype genotype) throws Node.ScribeException {
        Env env = genotype.bind(new Env());

        String res = "";

        if(mMetadataNode != null) {
            res += mMetadataNode.scribe(env) + " ";
        }

        if(mConfigurationNode != null) {
            res += mConfigurationNode.scribe(env) + " ";
        }

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

    /*
      Assign gensym values to the alterable 
      nodes and collect them in a genotype
     */
    private void addGenSyms(Node node, SymbolGenerator sg) {

        if (node.isAlterable()) {
            // only NodeMutate classes can be alterable
            NodeMutate nodeM = (NodeMutate) node;
            nodeM.setGenSym(sg.gen());
            mGenotype.add(nodeM.klone());
        }

        try {
            if (node.getType() == Node.Type.LIST) {
                List<Node> children = Node.asList(node).getChildren();
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
            mGenotype = new Genotype(this);
            for (Node node : ast) {
                addGenSyms(node, sg);
            }

            return ast;
        } catch (LangException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * remove list of k,v pairs from ast and build a map with them
     * the returned map will also have a special key named formName
     * which has a value of the original node in the ast
     */
    private Map<String, Node> extractPairForm(List<Node> ast, String formName) {
        Map<String, Node> map = new HashMap<String, Node>();

        Node meta;

        Iterator<Node> iter = ast.iterator();
        while(iter.hasNext()) {
            Node n = iter.next();

            if(n.getType() == Node.Type.LIST) {
                try {
                    NodeList nl = Node.asList(n);
                    Node child = nl.getChild(0);
                    if(child.getType() == Node.Type.NAME) {
                        String name = Node.asNameValue(child);
                        if(name.equals(formName)) {
                            map.put(formName, n);
                            addPairFormsToMap(map, nl);
                            iter.remove();
                        }
                    }
                } catch (LangException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    private void addPairFormsToMap(Map<String, Node> map, NodeList nl) {
        List<Node> children = nl.getChildren();
        
        Iterator<Node> iter = children.iterator();
        if(iter.hasNext()) {
            iter.next();        // NodeName: metadata | configure
        }

        Node k, v;
        while(iter.hasNext()) {
            k = iter.next();
            if(!iter.hasNext()) {
                // throw an exception, no corresponding v for k
            }
            v = iter.next();

            try {
                String s = Node.asNameValue(k);
                map.put(s, v);
            } catch(LangException e) {
                e.printStackTrace();
            }
        }
    }
}
