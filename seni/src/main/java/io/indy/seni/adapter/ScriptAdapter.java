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

import io.indy.seni.AppConfig;
import io.indy.seni.EvolveActivity;
import io.indy.seni.EvolveGridFragment;
import io.indy.seni.R;
import io.indy.seni.dummy.Art1352;
import io.indy.seni.dummy.Art1402;
import io.indy.seni.dummy.Art1403b;
import io.indy.seni.dummy.Art1405;
import io.indy.seni.dummy.Art1405b;
import io.indy.seni.lang.AstHolder;
import io.indy.seni.lang.Node;
import io.indy.seni.view.SeniView;

public class ScriptAdapter extends BaseAdapter {

    private static final String TAG = "ScriptAdapter";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    private static final String[] items = {"lorem", "ipsum", "dolor", "foo", "bar"};
    private AstHolder[] mAstHolder;

    private LayoutInflater mInflater;
    private Context mContext;

    public ScriptAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mAstHolder = new AstHolder[5];
        mAstHolder[0] = new AstHolder(Art1352.script());
        mAstHolder[1] = new AstHolder(Art1402.script());
        mAstHolder[2] = new AstHolder(Art1403b.script());
        mAstHolder[3] = new AstHolder(Art1405.script());
        mAstHolder[4] = new AstHolder(Art1405b.script());
    }

    @Override
    public int getCount() {
        return mAstHolder.length;
    }

    @Override
    public Object getItem(int position) {
        return mAstHolder[position];
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
        TextView text;

        int numColumns = mContext.getResources().getInteger(R.integer.num_columns);

        if (convertView == null) {
            if (numColumns > 1) {
                view = mInflater.inflate(R.layout.cell_templist, parent, false);
            } else {
                view = mInflater.inflate(R.layout.row_templist, parent, false);
            }
        } else {
            view = convertView;
        }

        view.setTag(R.string.tag_position, position);

        SeniView seniView = (SeniView) view.findViewById(R.id.seniView);
        seniView.setAstHolder(mAstHolder[position]);

        if (numColumns > 1) {
            text = (TextView) view.findViewById(R.id.bar);
            text.setText("shabba " + position);
        } else {
            text = (TextView) view.findViewById(R.id.foo);
            text.setText("" + position);
        }

        view.setOnClickListener(mOnClickListener);

        return view;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ifd("clicked");

            int position = (int) v.getTag(R.string.tag_position);
            Intent intent = new Intent(mContext, EvolveActivity.class);

            String script = "";

            try {
                script = mAstHolder[position].scribe();
            } catch (Node.ScribeException e) {
                e.printStackTrace();
            }

            intent.putExtra(EvolveGridFragment.GENESIS_SCRIPT, script);

            mContext.startActivity(intent);
        }
    };
}
