package com.bathem.vocabpower.Model;


import com.bathem.vocabpower.Entity.Vocab;
import com.bathem.vocabpower.Entity.Word;

import java.util.List;

/**
 * Created by mehtab on 1/17/16.
 */
public class DataModel {

    private static List<Vocab> vocabs;
    private static List<Word> words;


    public static List<Vocab> getVocabs() {
        return vocabs;
    }

    public static void setVocabs(List<Vocab> vocabs) {
        DataModel.vocabs = vocabs;
    }

    public static List<Word> getWords() {
        return words;
    }

    public static void setWords(List<Word> words) {
        DataModel.words = words;
    }
}
