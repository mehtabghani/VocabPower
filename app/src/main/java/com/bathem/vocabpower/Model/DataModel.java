package com.bathem.vocabpower.Model;


import android.content.Context;
import android.util.Log;

import com.bathem.vocabpower.Entity.Vocab;
import com.bathem.vocabpower.Entity.Word;
import com.bathem.vocabpower.Helper.DataBaseHelper;
import com.bathem.vocabpower.Helper.Utils;

import java.util.List;
import java.util.Random;

/**
 * Created by mehtab on 1/17/16.
 */
public class DataModel {

    private static List<Vocab> sVocabs;
    private static List<Word> sWords;
    private static Vocab sCurrentRandomVocab;



    public static List<Vocab> getsVocabs() {
        return sVocabs;
    }

    public static void setsVocabs(List<Vocab> sVocabs) {
        DataModel.sVocabs = sVocabs;
    }

    public static List<Word> getCurrentWordList() {
        return DataModel.sWords;
    }

    public static void setCurrentWordList(List<Word> sWords) {
        DataModel.sWords = sWords;
    }

    public static List<Word> getWordList(Context context) {
        DataBaseHelper db = new DataBaseHelper(context);
        sWords = db.getWordList();
        return sWords;
    }

    public static Vocab getRandomVocab(Context context) {

        if(sWords == null && sWords.size() < 0) {
            Log.d("debug", "No word list available for random words prepartion.");
            return null;
        }

        int id = Utils.getRandomNumber(1, sWords.size());

        DataBaseHelper db = new DataBaseHelper(context);
        sCurrentRandomVocab = db.getVocabByID(id);
        return sCurrentRandomVocab;
    }

    public static Vocab getCurrentRandomVocab() {
        return sCurrentRandomVocab;
    }
}
