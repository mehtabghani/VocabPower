package com.bathem.vocabpower.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bathem.vocabpower.Entity.Vocab;
import com.bathem.vocabpower.Entity.Word;
import com.bathem.vocabpower.Enum.AddEditMode;
import com.bathem.vocabpower.ExceptionHandler.ValidationException;
import com.bathem.vocabpower.Helper.DataBaseHelper;
import com.bathem.vocabpower.Helper.StringUtil;
import com.bathem.vocabpower.Model.DataModel;
import com.bathem.vocabpower.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */


public class AddEditFragment extends Fragment {

    private String word;
    private List<String> meanings;
    private List<String> examples;
    private static int meaningButtonCount;
    private static int exampleButtonCount;
    private List<EditText> meaningEditTextList = new ArrayList<EditText>();
    private List<EditText> exampleEditTextList = new ArrayList<EditText>();
    private static final int DYNAMIC_FIELD_LIMIT = 2;
    private AddEditMode mode;
    private Vocab mCurrentVocab;

    public AddEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        meaningButtonCount = 0;
        exampleButtonCount = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mode == AddEditMode.add_mode) {
            initAddMeaningFieldButton();
            initAddExampleFieldButton();
        } else {
            hideAddButtons();
            prepareFieldsForEditMode();
        }
    }

    public void setMode(AddEditMode _mode) {
        this.mode = _mode;
    }

    void initAddMeaningFieldButton() {
        Button btnAddField = (Button) getActivity().findViewById(R.id.button_add_meaning_field);
        btnAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(meaningButtonCount == DYNAMIC_FIELD_LIMIT) return;

                addMeaningFied((LinearLayout)getActivity().findViewById(R.id.linearLayoutMeaning), meaningButtonCount, meaningEditTextList);
                meaningButtonCount++;
            }
        });
    }

    void initAddExampleFieldButton() {
        Button btnAddField = (Button) getActivity().findViewById(R.id.button_add_example_field);
        btnAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (exampleButtonCount == DYNAMIC_FIELD_LIMIT) return;

                addMeaningFied((LinearLayout) getActivity().findViewById(R.id.linearLayoutExample), exampleButtonCount, exampleEditTextList);
                exampleButtonCount++;
            }
        });
    }

    void addMeaningFied(LinearLayout layout, int id, List<EditText> editTextList) {
        LinearLayout dynamicview = layout;

        LinearLayout.LayoutParams lprams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        EditText editText = new EditText(getActivity());
        editText.setId(id);
        editText.setLayoutParams(lprams);
        dynamicview.addView(editText);
        editTextList.add(editText);
    }

    public void addVocab() {

        try {
            validateFields();
            prepareVocabs();
            Vocab vocab = new Vocab();
            Word _word = new Word();
            _word.setWord(word);
            vocab.setVocab(_word, meanings, examples);
            long id = addVocabInDB(vocab);
            updateDataModel(id);
        } catch (ValidationException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), e.getErrorMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void updateDataModel (long id) {
        if(id == -1)
            return;
        DataBaseHelper db = new DataBaseHelper(getActivity().getApplicationContext());
        Word words = db.getWordById((int)id);
        DataModel.setWordInCurrentList(words);
    }

    void validateFields() throws ValidationException{

        EditText editText = (EditText) getActivity().findViewById(R.id.editText_word);

        if(!validateField(editText)) {
            ValidationException e = new ValidationException("Please insert word");
            throw e;
        }

        editText = (EditText) getActivity().findViewById(R.id.editText_meaning);

        if(!validateField(editText)) {

            ValidationException e = new ValidationException("Please insert meanings");
            throw e;
        }

       // editText = (EditText) getActivity().findViewById(R.id.editText_example);

//        if(!validateField(editText)) {
//            ValidationException e = new ValidationException("Empty field 3");
//            throw e;
//        }

    }

    void prepareVocabs() {
        word = ((EditText) getActivity().findViewById(R.id.editText_word)).getText().toString();

        meanings = new ArrayList<String>();
        examples = new ArrayList<String>();

        String txt = ((EditText) getActivity().findViewById(R.id.editText_meaning)).getText().toString();
        meanings.add(txt);

        //getting dynamic field data
        for (EditText editText:meaningEditTextList) {
            txt = editText.getText().toString();
            meanings.add(txt);
        }

        txt = ((EditText) getActivity().findViewById(R.id.editText_example)).getText().toString();
        examples.add(txt);

        //getting dynamic field data
        for (EditText editText:exampleEditTextList) {
            txt = editText.getText().toString();
            examples.add(txt);
        }

    }

    boolean validateField(EditText field) {

        if(StringUtil.stringEmptyOrNull(field.getText().toString())) {
            return false;
        }

        return true;
    }

    long addVocabInDB(Vocab vocab) {

        long result = DataModel.addVocab(vocab, getActivity());

        CharSequence text;

        if(result == -1) {
            text = "Failed to add vocab.";
        } else {
            getActivity().onBackPressed();
            text = "Vocab added successfully.";
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
        return result;
    }

    void hideAddButtons() {
        Button btnAddField = (Button) getActivity().findViewById(R.id.button_add_meaning_field);
        btnAddField.setVisibility(View.INVISIBLE);
        btnAddField = (Button) getActivity().findViewById(R.id.button_add_example_field);
        btnAddField.setVisibility(View.INVISIBLE);
    }

    void prepareFieldsForEditMode () {
        mCurrentVocab = DataModel.getCurrentVocab();

        EditText editText = (EditText) getActivity().findViewById(R.id.editText_word);
        editText.setText(mCurrentVocab.getWord().getWord());

        editText = (EditText) getActivity().findViewById(R.id.editText_meaning);
        editText.setText(mCurrentVocab.getMeaning().get(0));

        if(mCurrentVocab.getExample().size() > 0) {
            editText = (EditText) getActivity().findViewById(R.id.editText_example);
            editText.setText(mCurrentVocab.getExample().get(0));
        }
    }

    public void editVocab() {

        try {
            validateFields();
            prepareVocabs();
            Vocab vocab = new Vocab();
            Word _word = new Word();
            _word.setId(DataModel.getCurrentVocab().getWord().getId());
            _word.setWord(word);
            vocab.setVocab(_word, meanings, examples);
            long id = editVocabInDB(vocab);
            refreshDataModel();
        } catch (ValidationException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), e.getErrorMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    long editVocabInDB(Vocab vocab) {

        long result = DataModel.editVocab(vocab, getActivity());

        CharSequence text;

        if(result == -1) {
            text = "Failed to update vocab.";
        } else {
            getActivity().onBackPressed();
            text = "Vocab updated successfully.";
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
        return result;
    }

    private void refreshDataModel()
    {
        DataModel.refreshWordList(getActivity());
    }

}
