package com.bathem.vocabpower.Model;

import com.bathem.vocabpower.Entity.Vocab;

import java.util.List;

/**
 * Created by mehtab on 1/17/16.
 */
public class DataModel {

    private static List<Vocab> vocabs;

    public static List<Vocab> getVocabs() {
        return vocabs;
    }

    public static void setVocabs(List<Vocab> vocabs) {
        DataModel.vocabs = vocabs;
    }
}
