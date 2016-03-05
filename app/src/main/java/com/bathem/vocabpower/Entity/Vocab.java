package com.bathem.vocabpower.Entity;

import com.bathem.vocabpower.Helper.StringUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mehtab on 12/19/15.
 */
public class Vocab {

     private String word;
     private List<String> meaning;
     private List<String> example;


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getMeaning() {
        return meaning;
    }

    public void setMeaning(List<String> meaning) {

        this.meaning = meaning;
    }

    public List<String> getExample() {
        return example;
    }

    public void setExample(List<String> example) {

        this.example = example;
    }

    public void setVocab(String _word, String[] _meanings, String[] _examples) {

        setWord(_word);

        if(_meanings.length > 0) {

            meaning = new ArrayList<String>();
            for (String _meaning : _meanings) {
                meaning.add(_meaning);
            }
        }

        if(_examples.length > 0) {

            example = new ArrayList<String>();
            for (String _example : _examples) {
                example.add(_example);
            }
        }
    }
}

