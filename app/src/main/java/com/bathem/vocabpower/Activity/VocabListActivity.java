package com.bathem.vocabpower.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        List<Vocab> vocabs = DataModel.getVocabs();

        if(vocabs == null)
            return;

        List<String> list = new ArrayList<String>();
        for (Vocab v: vocabs) {
           list.add( v.word );
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_vocab_listview, list);
        ListView listView = (ListView)findViewById(R.id.vocab_list);
        listView.setAdapter(adapter);
    }


}
