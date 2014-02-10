package io.indy.seni.model;

import android.util.Log;

import io.indy.seni.AppConfig;

import android.content.Context;
import android.support.v4.util.AtomicFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicStorage {

    private static final String TAG = "AtomicStorage";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    public static final String FILENAME = "seni-state.json";

    private Context mContext;

    public AtomicStorage(Context context) {
        mContext = context;
    }

    public JSONObject getJSON(String filename) {
        JSONObject jsonObject = new JSONObject();

        try {
            String jsonString = loadFileAsString(filename);
            if (jsonString == null) {
                ifd("getJSON: unable to load contents of " + filename);
                return null;
            }
            //ifd("content of " + filename + " is " + jsonString);
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public void saveJSON(String filename, JSONObject jsonObject) {

        File file = new File(mContext.getFilesDir(), filename);

        AtomicFile atomicFile = new AtomicFile(file);
        FileOutputStream stream = null;
        try {
            stream = atomicFile.startWrite();

            String json = jsonObject.toString();
            byte[] jsonBytes = json.getBytes();

            stream.write(jsonBytes);
            atomicFile.finishWrite(stream);

        } catch (IOException e) {
            e.printStackTrace();
            if (stream != null) {
                atomicFile.failWrite(stream);
            }
        }
    }

    private String loadFileAsString(String filename) {

        File file = new File(mContext.getFilesDir(), filename);
        if (!file.exists()) {
            ifd("loadFileAsString: " + filename + " does not exist");
            return null;
        }

        try {
            AtomicFile atomicFile = new AtomicFile(file);
            byte[] byteArray = atomicFile.readFully();
            return new String(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ifd("loadFileAsString: failed to read " + filename);
        return null;
    }
}
