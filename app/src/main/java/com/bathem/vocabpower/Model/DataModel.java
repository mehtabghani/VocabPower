package com.bathem.vocabpower.Model;


import android.content.Context;
import android.util.Log;

import com.bathem.vocabpower.Entity.Vocab;
import com.bathem.vocabpower.Entity.Word;
import com.bathem.vocabpower.Helper.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehtab on 1/17/16.
 */
public class DataModel {

    private static List<Vocab> sVocabs;
    private static List<Word> sWords;
    private static Vocab sCurrentRandomVocab;
    private static Vocab sCurrentVocab;


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

    public static void setWordInCurrentList(Word word) {

        DataModel.sWords.add(word);
    }

    public static List<Word> getWordList(Context context) {
        DataBaseHelper db = new DataBaseHelper(context);

        List<Word> words = db.getWordList();

        if(sWords == null) {
            sWords = new ArrayList<Word>();
        }   else {
            sWords.clear();
        }
        sWords.addAll(words);

        return sWords;
    }

    public static Vocab getRandomVocab(Context context) {

        if(sWords == null || sWords.size() <= 0) {
            Log.d("debug", "No word list available for random words prepartion.");
            return null;
        }
//
//        int id = Utils.getRandomNumber(1, sWords.size());

        DataBaseHelper db = new DataBaseHelper(context);
        sCurrentRandomVocab = db.getRandomVocab();
        return sCurrentRandomVocab;
    }

    public static Vocab getCurrentRandomVocab() {
        return sCurrentRandomVocab;
    }

    public static Vocab getCurrentVocab() {
        return sCurrentVocab;
    }

    public static void setCurrentVocab(Vocab sCurrentVocab) {
        DataModel.sCurrentVocab = sCurrentVocab;
    }

    public static long addVocab(Vocab vocab, Context context) {
        DataBaseHelper db = new DataBaseHelper(context);
        long result = db.addVocab(vocab);
        return result;
    }

    public static long editVocab(Vocab vocab, Context context) {
        DataBaseHelper db = new DataBaseHelper(context);
        long result = db.editVocab(vocab);
        return  result;
    }

    public static void deleteVocab(List<Integer> ids, Context context) {
        DataBaseHelper db = new DataBaseHelper(context);
         db.deleteVocabs(ids);
    }

    public static void refreshWordList (Context context) {
        DataModel.getWordList(context);
    }
}
