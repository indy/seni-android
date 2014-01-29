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

package io.indy.seni;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import io.indy.seni.adapter.EvolveAdapter;
import io.indy.seni.adapter.ScriptAdapter;
import io.indy.seni.dummy.DummyContent;
import io.indy.seni.lang.AstHolder;

public class EvolveFragment extends Fragment {
    /**
     * The original script that all other images are derived from
     */
    public static final String GENESIS_SCRIPT = "genesis_script";

    private EvolveAdapter mEvolveAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EvolveFragment() {
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mEvolveAdapter = new EvolveAdapter(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(GENESIS_SCRIPT)) {
            String script = getArguments().getString(GENESIS_SCRIPT);
            mEvolveAdapter.setScript(script);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_evolve, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridView gridView = (GridView) view.findViewById(R.id.grid);
        gridView.setAdapter(mEvolveAdapter);
    }
}
