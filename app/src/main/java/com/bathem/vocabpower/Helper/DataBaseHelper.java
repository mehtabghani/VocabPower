package com.bathem.vocabpower.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bathem.vocabpower.Entity.Category;
import com.bathem.vocabpower.Entity.Vocab;
import com.bathem.vocabpower.Entity.Word;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mehtab on 1/24/16.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final boolean DB_AVAIALBLE = true;

    private static final int ERROR_IN_QUERY = -1;

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "vocabsDB";

    // Table Names
    private static final String TABLE_WORD = "word";
    private static final String TABLE_MEANING = "meaning";
    private static final String TABLE_EXAMPLE = "example";
    private static final String TABLE_TYPE = "type";
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_WORD_CATEGORY = "word_category";


    // Common column names
    private static final String COL_ID = "id";
    private static final String COL_WORD = "word";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_FK_TYPE_ID = "type_id";

    private static final String COL_MEANING = "meaning";
    private static final String COL_EXAMPLE = "example";
    private static final String COL_FK_WORD_ID = "word_id";

    private static final String COL_TYPE = "type";
    private static final String COL_CATEGORY_NAME = "category_name";
    private static final String COL_FK_CATEGORY_ID = "category_id";


    // Table Create Statements

    // Type table create statement
    private static final String CREATE_TABLE_TYPE = "CREATE TABLE "
            + TABLE_TYPE + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_TYPE + " TEXT"
            + ")";

    // Word table create statement
    private static final String CREATE_TABLE_WORD = "CREATE TABLE "
            + TABLE_WORD + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_WORD + " TEXT,"
            + COL_CREATED_AT + " DATETIME,"
            + COL_FK_TYPE_ID + " INTEGER,"
            + "FOREIGN KEY (" + COL_FK_TYPE_ID + ") REFERENCES " + TABLE_TYPE + "(" + COL_ID + ")"
            + ")";

    // Meaning table create statement
    private static final String CREATE_TABLE_MEANING = "CREATE TABLE "
            + TABLE_MEANING + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_MEANING + " TEXT,"
            + COL_FK_WORD_ID + " INTEGER,"
            + "FOREIGN KEY (" + COL_FK_WORD_ID + ") REFERENCES " + TABLE_WORD + "(" + COL_ID + ")"
            + ")";

    // Example table create statement
    private static final String CREATE_TABLE_EXAMPLE = "CREATE TABLE "
            + TABLE_EXAMPLE + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_EXAMPLE + " TEXT,"
            + COL_FK_WORD_ID + " INTEGER,"
            + "FOREIGN KEY (" + COL_FK_WORD_ID + ") REFERENCES " + TABLE_WORD + "(" + COL_ID + ")"
            + ")";

    // Group table create statement
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE "
            + TABLE_CATEGORY + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_CATEGORY_NAME + " TEXT"
            + ")";


    // GROUP_WORD table create statement
    private static final String CREATE_TABLE_WORD_CATEGORY = "CREATE TABLE "
            + TABLE_WORD_CATEGORY + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_FK_WORD_ID + " INTEGER NOT NULL,"
            + COL_FK_CATEGORY_ID + " INTEGER NOT NULL,"
            + "FOREIGN KEY (" + COL_FK_WORD_ID + ") REFERENCES " + TABLE_WORD + "(" + COL_ID + "), "
            + "FOREIGN KEY (" + COL_FK_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + COL_ID + ")"
            + ")";

    /* SELECT QUERY */

    private static final String SELECT_ALL_VOCAB = "SELECT * FROM " + TABLE_WORD;


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TYPE);
        db.execSQL(CREATE_TABLE_WORD);
        db.execSQL(CREATE_TABLE_MEANING);
        db.execSQL(CREATE_TABLE_EXAMPLE);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_WORD_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

/*
 * Creating a Group
 */
    public long createCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_CATEGORY_NAME, category.getCatgoryName());

        // insert row
        long id = db.insert(TABLE_CATEGORY, null, values);

        return id;
    }

    public long addVocab(Vocab vocab) {

        long id;
        id = addWord(vocab.getWord());

        if(id != ERROR_IN_QUERY) {

            addMeaning(vocab.getMeaning(), id);
            addExample(vocab.getExample(), id);
        }

        return id;
    }

    private long addWord(String word) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_WORD, word);
        Date date= new Date();
        String sDate = Utils.getStringDate(date);
        values.put(COL_CREATED_AT, sDate);

        // insert row
        long id = db.insert(TABLE_WORD, null, values);
        return id;
    }

    private void addMeaning(List<String> meanings, long wordID) {

        SQLiteDatabase db = this.getWritableDatabase();

        for (String meaning : meanings) {
            ContentValues values = new ContentValues();
            values.put(COL_MEANING, meaning);
            values.put(COL_FK_WORD_ID, wordID);
            db.insert(TABLE_MEANING, null, values);
        }
    }

    private void addExample(List<String> examples, long wordID) {

        SQLiteDatabase db = this.getWritableDatabase();

        for (String example : examples) {
            ContentValues values = new ContentValues();
            values.put(COL_EXAMPLE, example);
            values.put(COL_FK_WORD_ID, wordID);
            db.insert(TABLE_EXAMPLE, null, values);
        }
    }

    public List<Word> getWordList() {

        if(!DB_AVAIALBLE)
            return new ArrayList<Word>();


        List<Word> words= new ArrayList<Word>();

        String SELECT_ALL_VOCAB = "SELECT * FROM " + TABLE_WORD;
        Log.e(LOG, SELECT_ALL_VOCAB);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(SELECT_ALL_VOCAB, null);

        if (c.moveToFirst()) {
          do {
              Word word = new Word();
              word.setId(c.getInt(c.getColumnIndex(COL_ID)));
              word.setWord(c.getString(c.getColumnIndex(COL_WORD)));
              String date = c.getString(c.getColumnIndex(COL_CREATED_AT));
              word.setCreateAt(Utils.getDateFromString(date));
              words.add(word);
          } while (c.moveToNext());
        }

        return words;
    }

    public Vocab getVocabByID(int id) {

        if(!DB_AVAIALBLE)
            return  null;

        Vocab vocab = new Vocab();
        //getWord
        vocab.setWord(getColunmWordById(id));
        //getMeaning
        vocab.setMeaning(getMeaningsByid(id));
        //getExample
        vocab.setExample(getExamplesByid(id));

        return vocab;
    }

    private String getColunmWordById(int id) {

        String SELECT_WORD = "SELECT * FROM " + TABLE_WORD + " WHERE "+ COL_ID + " = " + id;
        Log.e(LOG, SELECT_WORD);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(SELECT_WORD, null);

        if (c != null)
            c.moveToFirst();

        String word = c.getString(c.getColumnIndex(COL_WORD));
        return word;
    }

    private List<String> getMeaningsByid(int wordId) {
        List<String> meanings= new ArrayList<String>();

        String SELECT_MEANINGS = "SELECT * FROM " + TABLE_MEANING + " WHERE " + COL_FK_WORD_ID + "=" + wordId;
        Log.e(LOG, SELECT_MEANINGS);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(SELECT_MEANINGS, null);

        if (c.moveToFirst()) {
            do {
                String meaning = c.getString(c.getColumnIndex(COL_MEANING));
                meanings.add(meaning);
            } while (c.moveToNext());
        }
        return meanings;
    }

    private List<String> getExamplesByid(int wordId) {
        List<String> examples = new ArrayList<String>();

        String SELECT_EXAMPLE = "SELECT * FROM " + TABLE_EXAMPLE + " WHERE " + COL_FK_WORD_ID + "=" + wordId;
        Log.e(LOG, SELECT_EXAMPLE);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(SELECT_EXAMPLE, null);

        if (c.moveToFirst()) {
            do {
                String example = c.getString(c.getColumnIndex(COL_EXAMPLE));
                examples.add(example);
            } while (c.moveToNext());
        }
        return examples;
    }


    public Word getWordById(int id) {

        String SELECT_WORD = "SELECT * FROM " + TABLE_WORD + " WHERE "+ COL_ID + " = " + id;
        Log.e(LOG, SELECT_WORD);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(SELECT_WORD, null);

        if (c != null)
            c.moveToFirst();

        Word word = new Word();
        word.setId(c.getInt(c.getColumnIndex(COL_ID)));
        word.setWord(c.getString(c.getColumnIndex(COL_WORD)));
        String date = c.getString(c.getColumnIndex(COL_CREATED_AT));
        word.setCreateAt(Utils.getDateFromString(date));
        return word;
    }
}
