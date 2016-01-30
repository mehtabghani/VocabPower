package com.bathem.vocabpower.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.bathem.vocabpower.Entity.Vocab;
import com.bathem.vocabpower.Helper.DataBaseHelper;
import com.bathem.vocabpower.R;

public class AddVocabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vocab);
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
}
