package com.bathem.vocabpower.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bathem.vocabpower.Entity.Vocab;
import com.bathem.vocabpower.ExceptionHandler.ValidationException;
import com.bathem.vocabpower.Helper.DataBaseHelper;
import com.bathem.vocabpower.Helper.StringUtil;
import com.bathem.vocabpower.R;

import java.util.ArrayList;

public class AddVocabActivity extends AppCompatActivity {

    private String word;
    private String meaning;
    private String example;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vocab);
        initAddVocabButton();
    }

    void initAddVocabButton () {
        Button btnAdd = (Button) findViewById(R.id.button_add_vocab);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVocab();
            }
        });
    }

    void addVocab() {

        try {
            validateFields();
            Vocab vocab = new Vocab();
            vocab.setVocab(word, new String[]{meaning}, new String[]{example});
            addVocabInDB(vocab);
        } catch (ValidationException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(getApplicationContext(), e.getErrorMessage(), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

    }

    void validateFields() throws ValidationException{

        EditText editText = (EditText) findViewById(R.id.editText_word);
        word = editText.getText().toString();

        if(!validateField(editText)) {
            ValidationException e = new ValidationException("Please insert word");
            throw e;
        }

        editText = (EditText) findViewById(R.id.editText_meaning);
        meaning = editText.getText().toString();

        if(!validateField(editText)) {

            ValidationException e = new ValidationException("Please insert meaning");
            throw e;
        }

        editText = (EditText) findViewById(R.id.editText_example);
        example = editText.getText().toString();

//        if(!validateField(editText)) {
//            ValidationException e = new ValidationException("Empty field 3");
//            throw e;
//        }

    }

    boolean validateField(EditText field) {

        if(StringUtil.stringEmptyOrNull(field.getText().toString())) {
            return false;
        }

        return true;
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
