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
import io.indy.seni.R;
import io.indy.seni.RenderActivity;
import io.indy.seni.dummy.Art1352;
import io.indy.seni.dummy.Art1402;
import io.indy.seni.dummy.Art1403;
import io.indy.seni.dummy.Art1403b;
import io.indy.seni.lang.AstHolder;
import io.indy.seni.lang.Genotype;
import io.indy.seni.lang.Node;
import io.indy.seni.view.SeniView;

public class ScriptAdapter extends BaseAdapter {

    private static final String TAG = "ScriptAdapter";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    private static final String[] items={"lorem", "ipsum", "dolor",
            "sit", "amet",
            "consectetuer", "adipiscing", "elit", "morbi", "vel",
            "ligula", "vitae", "arcu", "aliquet", "mollis",
            "etiam", "vel", "erat", "placerat", "ante",
            "porttitor", "sodales", "pellentesque", "augue", "purus"};



    // private static final String[] items={"lorem", "ipsum"};

    private LayoutInflater mInflater;
    private Context mContext;

    private AstHolder mAstHolder;
    private Genotype[] mGenotypes;
    private int mNumFucks;

    public ScriptAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mAstHolder = new AstHolder(Art1403b.script());
        mNumFucks = 32;
        mGenotypes = new Genotype[mNumFucks];
        for(int i=0;i<mNumFucks;i++) {
            mGenotypes[i] = mAstHolder.getGenotype().mutate();
        }
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
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
            if(numColumns > 1) {
                view = mInflater.inflate(R.layout.cell_templist, parent, false);
            } else {
                view = mInflater.inflate(R.layout.row_templist, parent, false);
            }
        } else {
            view = convertView;
        }

        view.setTag(R.string.temp_position, position);

        SeniView seniView = (SeniView) view.findViewById(R.id.seniView);
        seniView.setAstHolder(mAstHolder);
        seniView.setGenotype(mGenotypes[position % mNumFucks]);

        if(numColumns > 1) {
            text = (TextView) view.findViewById(R.id.bar);
            text.setText("shabba " + (CharSequence)getItem(position));
        } else {
            text = (TextView) view.findViewById(R.id.foo);
            text.setText((CharSequence)getItem(position));
        }

        view.setOnClickListener(mOnClickListener);

        return view;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ifd("clicked");

            int position = (int)v.getTag(R.string.temp_position);
            Intent intent = new Intent(mContext, RenderActivity.class);

            Genotype genotype = mGenotypes[position % mNumFucks];
            String script = "";

            try {
                script = mAstHolder.scribe(genotype);
            } catch(Node.ScribeException e) {
                e.printStackTrace();
            }

            intent.putExtra(RenderActivity.SCRIPT_NAME, script);

            mContext.startActivity(intent);
        }
    };

    private String getScript(int position) {

        switch (position % 3) {
            case 0: return Art1352.script();
            case 1: return Art1402.script();
            default:return Art1403.script();
        }
    }

}
