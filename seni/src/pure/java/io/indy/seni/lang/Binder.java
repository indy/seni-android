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

public class Binder {

    public static void checkArity(List<Node> params, int expected, String kw)
            throws LangException {
        int size = params.size();
        if (size != expected) {
            String msg = "wrong # of arguments for " + kw;
            msg += " expected " + expected + ", given " + size;
            throw new LangException(msg);
        }
    }

    public static void checkArityAtLeast(List<Node> params, int expected, String kw)
            throws LangException {
        int size = params.size();
        if (size < expected) {
            String msg = "wrong # of arguments for " + kw;
            msg += " expected at least " + expected + ", given " + size;
            throw new LangException(msg);
        }
    }

}
