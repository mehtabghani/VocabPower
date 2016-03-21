package com.bathem.vocabpower.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bathem.vocabpower.Fragment.ListFragment;
import com.bathem.vocabpower.Fragment.RandomVocabFragment;
import com.bathem.vocabpower.Helper.DataBaseHelper;
import com.bathem.vocabpower.Model.DataModel;
import com.bathem.vocabpower.Entity.Vocab;
import com.bathem.vocabpower.Helper.JSONHelper;
import com.bathem.vocabpower.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void init() {

        addRandomVocabFragment();
        initListButton();
        initAddButton();

        //loadJSONFile();
//
//        Vocab v = new Vocab();
//        v.setWord("Tempt");
//
//        List list = new ArrayList<String>();
//        list.add("to encourage someone to do or want something that is wrong.");
//        v.setMeaning(list);
//
//        list = new ArrayList<String>();
//        list.add("Dont try to tempt me, i know this pie is delicious");
//
//        v.setExample(list);

    }

    void addRandomVocabFragment() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout_random_vocab, new RandomVocabFragment());
        ft.commit();
    }

    private void initListButton() {
        Button btnList = (Button) findViewById(R.id.button_list);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, VocabListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initAddButton() {
        Button btnadd = (Button) findViewById(R.id.button_add_vocab);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddVocabActivity.class);
                startActivity(intent);
            }
        });
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
