package com.bathem.vocabpower.Entity;

import java.util.List;


/**
 * Created by mehtab on 12/19/15.
 */
public class Vocab {

     //private String word;
     private Word word;
     private List<String> meaning;
     private List<String> example;

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

    public void setVocab(Word _word, List<String> _meanings, List<String> _examples) {

        setWord(_word);
        setMeaning(_meanings);
        setExample(_examples);

//        if(_meanings.size() > 0) {
//
//            meaning = new ArrayList<String>();
//            for (String _meaning : _meanings) {
//                meaning.add(_meaning);
//            }
//        }
//
//        if(_examples.size() > 0) {
//
//            example = new ArrayList<String>();
//            for (String _example : _examples) {
//                example.add(_example);
//            }
//        }
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }
}

