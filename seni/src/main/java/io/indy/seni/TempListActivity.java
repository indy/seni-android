package io.indy.seni;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import io.indy.seni.adapter.ScriptAdapter;

public class TempListActivity extends Activity {

    private static final String TAG = "TempListActivity";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // tell system to use the layout defined in our XML file
        setContentView(R.layout.activity_templist);


        /*
        GridView gridView = (GridView)findViewById(R.id.grid);
        gridView.setAdapter(new ScriptAdapter(this));
        gridView.setOnItemClickListener(this);
        */

        ListView listView = (ListView)findViewById(R.id.listy);
        listView.setAdapter(new ScriptAdapter(this));
    }


}
