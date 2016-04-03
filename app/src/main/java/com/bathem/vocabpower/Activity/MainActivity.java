package com.bathem.vocabpower.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bathem.vocabpower.Activity.Base.BaseActivity;
import com.bathem.vocabpower.Fragment.RandomVocabFragment;
import com.bathem.vocabpower.Model.DataModel;
import com.bathem.vocabpower.Entity.Vocab;
import com.bathem.vocabpower.Helper.JSONHelper;
import com.bathem.vocabpower.R;

import java.io.IOException;
import java.util.List;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addRandomVocabFragment();
    }

    void init() {
        DataModel.getWordList(this);
        addRandomVocabFragment();
        initListButton();
        initAddButton();
    }

    void addRandomVocabFragment() {

        if(getRandomVocab() == null) {
            Log.d("debug", "No random vocab available.");
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout_random_vocab, new RandomVocabFragment());
        ft.commit();
    }

    private Vocab getRandomVocab() {

       return DataModel.getRandomVocab(getApplicationContext());
    }

    private void initListButton() {
        Button btnList = (Button) findViewById(R.id.button_list);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(DataModel.getWordList(MainActivity.this).size() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please add vocab first.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

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
                DataModel.setsVocabs(vocabs);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
