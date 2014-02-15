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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.indy.seni.AppConfig;
import io.indy.seni.lang.LangException;
import io.indy.seni.ui.EvolveActivity;
import io.indy.seni.ui.EvolveGridFragment;
import io.indy.seni.R;
import io.indy.seni.lang.AstHolder;
import io.indy.seni.lang.Node;
import io.indy.seni.util.ImageGenerator;
import io.indy.seni.view.SeniView;

public class ScriptAdapter extends BaseAdapter {

    private static final String TAG = "ScriptAdapter";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    private List<AstHolder> mAstHolders;

    private LayoutInflater mInflater;
    private Context mContext;

    private int mNumColumns = 0;

    private int mActionBarHeight = 0;

    private ImageGenerator mImageGenerator;


    public ScriptAdapter(Context context, ImageGenerator imageGenerator) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        mAstHolders = parseScripts(context, "scripts");

        mImageGenerator = imageGenerator;

        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(
                android.R.attr.actionBarSize, tv, true)) {
            mActionBarHeight = TypedValue.complexToDimensionPixelSize(
                    tv.data, context.getResources().getDisplayMetrics());
            ifd("mActionBarHeight = " + mActionBarHeight);
        }
    }

    @Override
    public int getCount() {
        if (getNumColumns() == 0) {
            return 0;
        }

        ifd("getCount = " + (mAstHolders.size() + mNumColumns));

        return mAstHolders.size() + mNumColumns;
    }

    @Override
    public Object getItem(int position) {
        if (position < mNumColumns) {
            ifd("getItem: null");
        } else {
            ifd("getItem: " + mAstHolders.get(position - mNumColumns));
        }
        return position < mNumColumns ? null : mAstHolders.get(position - mNumColumns);
    }

    @Override
    public long getItemId(int position) {
        if(position < mNumColumns) {
            ifd("getItemId: " + 0);
        } else {
            ifd("getItemId: " + (position - mNumColumns));
        }
        return position < mNumColumns ? 0 : position - mNumColumns;
    }

    @Override
    public int getViewTypeCount() {
        ifd("getViewTypeCount");
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        ifd("getItemViewType");
        return (position < mNumColumns) ? 1 : 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if(position < mNumColumns) {
            if (convertView == null) {
                convertView = new View(mContext);
            }
            // Set empty view with height of ActionBar
            convertView.setLayoutParams(new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, mActionBarHeight));
            return convertView;
        }

        int numColumns = mContext.getResources().getInteger(R.integer.num_columns);

        if (convertView == null) {
            if (numColumns > 1) {
                view = mInflater.inflate(R.layout.cell_script, parent, false);
            } else {
                view = mInflater.inflate(R.layout.row_script, parent, false);
            }
        } else {
            view = convertView;
        }

        view.setTag(R.string.tag_position, position - mNumColumns);

        AstHolder astHolder = mAstHolders.get(position - mNumColumns);

        ImageView phenoView = (ImageView) view.findViewById(R.id.pheno_view);
        mImageGenerator.loadImage(astHolder.getGenotype(), phenoView);

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(astHolder.getMetadataString("title", "unknown"));

        TextView description = (TextView) view.findViewById(R.id.description);
        description.setText(astHolder.getMetadataString("description", "no description"));

        view.setOnClickListener(mOnClickListener);

        return view;
    }


    public void setNumColumns(int numColumns) {
        ifd("setNumColumns " + numColumns);
        mNumColumns = numColumns;
    }

    public int getNumColumns() {
        return mNumColumns;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ifd("clicked");

            int position = (int) v.getTag(R.string.tag_position);
            Intent intent = new Intent(mContext, EvolveActivity.class);

            String script = "";

            try {
                script = mAstHolders.get(position).scribe();
            } catch (Node.ScribeException e) {
                e.printStackTrace();
            }

            intent.putExtra(EvolveGridFragment.GENESIS_SCRIPT, script);

            mContext.startActivity(intent);
        }
    };

    private List<AstHolder> parseScripts(Context context, String assetFolder) {
        List<AstHolder> asts = new ArrayList<>();

        try {
            String[] dir = context.getAssets().list(assetFolder);
            for(String s : dir) {
                String path = new File(assetFolder, s).toString();
                String script = readStringFromAsset(context, path);
                AstHolder astHolder = new AstHolder(script); // todo: check if correctly parsed
                asts.add(astHolder);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return asts;
    }

    private String readStringFromAsset(Context context, String path) {
        String res = "";
        try {
            InputStream is = context.getAssets().open(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            res = sb.toString();

            reader.close();
        } catch (IOException e) {
            //log the exception
        }

        return res;
    }
}
