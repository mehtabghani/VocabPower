package com.bathem.vocabpower.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bathem.vocabpower.Entity.Word;
import com.bathem.vocabpower.Helper.DataBaseHelper;
import com.bathem.vocabpower.Helper.StringUtil;
import com.bathem.vocabpower.Model.DataModel;
import com.bathem.vocabpower.Entity.Vocab;
import com.bathem.vocabpower.Helper.JSONHelper;
import com.bathem.vocabpower.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VocabListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prepareListView();
    }

    void prepareListView() {

       List<Word> words = DataModel.getWords();

        if(words ==  null) {
            DataBaseHelper db = new DataBaseHelper(getApplicationContext());
            words = db.getWordList();//DataModel.getVocabs();
            DataModel.setWords(words);
        }

        List<String> list = new ArrayList<String>();

        for (Word v: words) {
           list.add( v.getWord() );
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_vocab_listview, list);
        ListView listView = (ListView)findViewById(R.id.vocab_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Word word = DataModel.getWords().get(position);
                Log.d("List", word.getWord());

                if(word != null)
                    showDetailActivity(word.getId());
            }
        });
    }

    private void showDetailActivity(int vocabId) {

        Intent intent = new Intent(VocabListActivity.this, DetailActivity.class);
        intent.putExtra("vocab_id", vocabId);
        startActivity(intent);
    }

}
