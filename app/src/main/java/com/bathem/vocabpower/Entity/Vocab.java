package com.bathem.vocabpower.Entity;

import java.util.ArrayList;

/**
 * Created by mehtab on 12/19/15.
 */
public class Vocab {

    private String vocab;
    private ArrayList<String> meaning;
    private ArrayList<String> examples;


    public String getVocab() {
        return vocab;
    }

    public void setVocab(String vocab) {
        this.vocab = vocab;
    }

    public ArrayList<String> getMeaning() {
        return meaning;
    }

    public void setMeaning(ArrayList<String> meaning) {
        this.meaning = meaning;
    }

    public ArrayList<String> getExamples() {
        return examples;
    }

    public void setExamples(ArrayList<String> examples) {
        this.examples = examples;
    }
}
