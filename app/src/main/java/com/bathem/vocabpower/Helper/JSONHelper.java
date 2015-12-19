package com.bathem.vocabpower.Helper;

import android.content.Context;
import android.content.res.AssetManager;

import com.bathem.vocabpower.Activity.MainActivity;
import com.bathem.vocabpower.Entity.Vocab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * Created by mehtab on 12/19/15.
 */
public class JSONHelper {

    public static String jsonToStringFromAssetFolder(String fileName,Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(fileName);

        byte[] data = new byte[file.available()];
        file.read(data);
        file.close();
        return new String(data);
    }


    public static List<Vocab> getCollectionFromJSON(String json) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Vocab>>(){}.getType();
        return gson.fromJson(json, collectionType);
    }
}
