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

package io.indy.seni.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import io.indy.seni.AppConfig;
import io.indy.seni.EvolveActivity;
import io.indy.seni.EvolveFragment;
import io.indy.seni.R;
import io.indy.seni.dummy.Art1352;
import io.indy.seni.dummy.Art1402;
import io.indy.seni.dummy.Art1403;
import io.indy.seni.dummy.Art1403b;
import io.indy.seni.lang.AstHolder;
import io.indy.seni.lang.Genotype;
import io.indy.seni.lang.Node;
import io.indy.seni.lang.NodeMutate;
import io.indy.seni.view.SeniView;

public class EvolveAdapter extends BaseAdapter {

    private static final String TAG = "EvolveAdapter";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    private LayoutInflater mInflater;
    private Context mContext;

    private AstHolder mAstHolder;
    private Genotype[] mGenotypes;
    private int mNumFucks;

    public EvolveAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setScript(String script) {

        ifd("script: " + script);

        mAstHolder = new AstHolder(script);
        mNumFucks = 150;
        mGenotypes = new Genotype[mNumFucks];
        for (int i = 0; i < mNumFucks; i++) {
            mGenotypes[i] = mAstHolder.getGenotype().mutate();
/*
            List<NodeMutate> alterable = mGenotypes[i].getAlterable();
            for(NodeMutate n : alterable) {
                ifd("i: " + i + " " + n.toString());
            }
            */
        }
    }

    @Override
    public int getCount() {
        return mGenotypes.length;
    }

    @Override
    public Object getItem(int position) {
        return mGenotypes[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        int numColumns = mContext.getResources().getInteger(R.integer.num_evolve_columns);

        ifd("numColumns " + numColumns);

        if (convertView == null) {
            view = mInflater.inflate(R.layout.cell_evolve, parent, false);
        } else {
            view = convertView;
        }

        view.setTag(R.string.tag_position, position);

        SeniView seniView = (SeniView) view.findViewById(R.id.seniView);
        seniView.setAstHolder(mAstHolder);
        seniView.setGenotype(mGenotypes[position % mNumFucks]);

        view.setOnClickListener(mOnClickListener);

        return view;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ifd("clicked");
        }
    };
}
