package com.bathem.vocabpower.Entity;

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
}

