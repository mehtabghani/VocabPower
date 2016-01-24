package com.bathem.vocabpower.Helper;

import android.content.Context;
import android.content.res.AssetManager;

import com.bathem.vocabpower.Activity.MainActivity;
import com.bathem.vocabpower.Entity.Vocab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.io.FileWriter;

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

   /*
    public static void writeJSONToFile(List<Vocab> data, String fileName, Context context) {


        try {


            if(data ==  null)
                return;

            Gson gson = new Gson();
            String json = gson.toJson(data);
            //write converted json data to a file named "CountryGSON.json"

            File path = context.getFilesDir();
            File file = new File(path, fileName);
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(json.getBytes());
            stream.close();


//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(file);
//
//            outputStreamWriter.write(json);
//            outputStreamWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
*/

}
