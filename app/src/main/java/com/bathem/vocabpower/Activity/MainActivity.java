package com.bathem.vocabpower.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bathem.vocabpower.Entity.Category;
import com.bathem.vocabpower.Helper.DataBaseHelper;
import com.bathem.vocabpower.Model.DataModel;
import com.bathem.vocabpower.Entity.Vocab;
import com.bathem.vocabpower.Helper.JSONHelper;
import com.bathem.vocabpower.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, VocabListActivity.class);
                startActivity(intent);
            }
        });

        init();

    }

    void init() {

        //loadJSONFile();

        Vocab v = new Vocab();
        v.setWord("Tempt");

        List list = new ArrayList<String>();
        list.add("to encourage someone to do or want something that is wrong.");
        v.setMeaning(list);

        list = new ArrayList<String>();
        list.add("Dont try to tempt me, i know this pie is delicious");

        v.setExample(list);

        //DataModel.getVocabs().add(v);


        //addVocabInDB(v);
    }

    void addVocabInDB(Vocab vocab) {
        DataBaseHelper db = new DataBaseHelper(getApplicationContext());

        long result = db.addVocab(vocab);

        CharSequence text;

        if(result == -1)
            text = "Failed to add vocab.";
        else
            text = "Vocab added successfully.";

        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();

    }


    void loadJSONFile () {

        try {
            String jsonData = JSONHelper.jsonToStringFromAssetFolder("vocabs.json", MainActivity.this);
            Log.d("json", jsonData);
            List<Vocab> vocabs = JSONHelper.getCollectionFromJSON(jsonData);

            if(vocabs != null)
                DataModel.setVocabs(vocabs);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
