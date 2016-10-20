package com.bathem.vocabpower.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bathem.vocabpower.Constant.AppConstant;
import com.bathem.vocabpower.Entity.Category;
import com.bathem.vocabpower.Entity.Vocab;
import com.bathem.vocabpower.Entity.Word;
import com.bathem.vocabpower.Enum.DriveMode;
import com.bathem.vocabpower.Interface.IFileStatusListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mehtab on 1/24/16.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final boolean DB_AVAIALBLE = true;

    private static final int ERROR_IN_QUERY = -1;

    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Name
    public static final String DATABASE_NAME = "VocabMaster";

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
    private static final String COL_IS_FAVOURITE = "is_favourite";


    private static final String COL_MEANING = "meaning";
    private static final String COL_EXAMPLE = "example";
    private static final String COL_FK_WORD_ID = "word_id";

    private static final String COL_TYPE = "type";
    private static final String COL_CATEGORY_NAME = "category_name";
    private static final String COL_FK_CATEGORY_ID = "category_id";



    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(LOG, "onCreate starts");

        db.execSQL(CREATE_TABLE_TYPE);
        db.execSQL(CREATE_TABLE_WORD);
        db.execSQL(CREATE_TABLE_MEANING);
        db.execSQL(CREATE_TABLE_EXAMPLE);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_WORD_CATEGORY);
        addTypes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG, "Upgradtion starts");

        runUpgradeQueries(db);
    }

    private void runUpgradeQueries(SQLiteDatabase db) {
        db.beginTransaction();

        // addTypes(db); //version 2
        db.execSQL(ALTER_TABLE_WORD_ADD_COL_IS_FAVOURITE); //version 3

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /*
    * Creating a Group
    */
    public long createCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TYPE, category.getCatgoryName());

        // insert row
        long id = db.insert(TABLE_CATEGORY, null, values);
        db.close();

        return id;
    }

    private void addTypes(SQLiteDatabase db) {

        for (String type : AppConstant.WORD_TYPES) {
            ContentValues values = new ContentValues();
            values.put(COL_TYPE, type);
            long id = db.insert(TABLE_TYPE, null, values);

            if(id > 0)
                Log.d(LOG, type +  ": added to  types table");
            else
                Log.d(LOG, type +  ": failed to  add type");

        }

        //db.close();
    }


    // Add Methods

    public long addVocab(Vocab vocab) {

        long id;
        id = addWord(vocab.getWord());

        if(id != ERROR_IN_QUERY) {

            addMeaning(vocab.getMeaning(), id);
            addExample(vocab.getExample(), id);
        }

        return id;
    }

    private long addWord(Word word) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_WORD, word.getWord());
        Date date= new Date();
        String sDate = Utils.getStringDate(date);
        values.put(COL_CREATED_AT, sDate);
        values.put(COL_FK_TYPE_ID, word.getTypeID());
        values.put(COL_IS_FAVOURITE, word.isFavourite() ? 1 : 0);

        // insert row
        long id = db.insert(TABLE_WORD, null, values);
        db.close();

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

        db.close();
    }

    private void addExample(List<String> examples, long wordID) {

        SQLiteDatabase db = this.getWritableDatabase();

        for (String example : examples) {
            ContentValues values = new ContentValues();
            values.put(COL_EXAMPLE, example);
            values.put(COL_FK_WORD_ID, wordID);
            db.insert(TABLE_EXAMPLE, null, values);
        }

        db.close();
    }

    //Update Methods

    public long editVocab(Vocab vocab) {

        long result;
        int id = vocab.getWord().getId();


        result = editWordbyId(vocab.getWord(), vocab.getWord().getId());

        if(result != ERROR_IN_QUERY) {

            editMeaning(vocab.getMeaning(), id);
            editExample(vocab.getExample(), id);
        }

        return id;
    }

    private long editWordbyId(Word word, int _id) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_WORD, word.getWord());
        values.put(COL_FK_TYPE_ID, word.getTypeID());
        values.put(COL_IS_FAVOURITE, word.isFavourite() ? 1 : 0);

        long id = db.update(TABLE_WORD, values, COL_ID + "= ?", new String[]{String.valueOf(_id)});
        db.close();
        return id;
    }

    private void editMeaning(List<String> meanings, long wordID) {

        SQLiteDatabase db = this.getWritableDatabase();

        for (String meaning : meanings) {
            ContentValues values = new ContentValues();
            values.put(COL_MEANING, meaning);
            values.put(COL_FK_WORD_ID, wordID);
            db.update(TABLE_MEANING, values, COL_FK_WORD_ID + "= ?", new String[]{ String.valueOf(wordID) } );
        }
            db.close();
    }

    private void editExample(List<String> meanings, long wordID) {

        SQLiteDatabase db = this.getWritableDatabase();

        for (String meaning : meanings) {
            ContentValues values = new ContentValues();
            values.put(COL_EXAMPLE, meaning);
            values.put(COL_FK_WORD_ID, wordID);
            db.update(TABLE_EXAMPLE, values, COL_FK_WORD_ID  + "= ? ", new String[]{String.valueOf(wordID) } );
        }
        db.close();
    }

    //Get Methods

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
              word.setFavourite( (c.getInt(c.getColumnIndex(COL_IS_FAVOURITE)) == 1) );
              words.add(word);
          } while (c.moveToNext());
        }
        db.close();
        return words;
    }

    public Vocab getVocabByID(int id) {

        if(!DB_AVAIALBLE)
            return  null;

        Vocab vocab = new Vocab();
        //getWord
        vocab.setWord(getWordById(id));
        //getMeaning
        vocab.setMeaning(getMeaningsByid(id));
        //getExample
        vocab.setExample(getExamplesByid(id));

        return vocab;
    }

//    private Word getWordById(int id) {
//
//        String SELECT_WORD = "SELECT * FROM " + TABLE_WORD + " WHERE "+ COL_ID + " = " + id;
//        Log.e(LOG, SELECT_WORD);
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(SELECT_WORD, null);
//
//        if (c != null)
//            c.moveToFirst();
//
//
//        Word word = new Word();
//        word.setWord( c.getString(c.getColumnIndex(COL_WORD)) );
//        word.setId(c.get);
//        return word;
//    }

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

        db.close();

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

        db.close();

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
        word.setTypeID(c.getInt(c.getColumnIndex(COL_FK_TYPE_ID)));
        word.setFavourite( (c.getInt(c.getColumnIndex(COL_IS_FAVOURITE)) == 1) );
        db.close();

        return word;
    }


    //Delete Methods


    public void deleteVocabs(List<Integer> ids) {

        for (Integer id:ids) {
            deleteWordById(id);
            deleteMeaningByWordId(id);
            deleteExampleByWordId(id);
        }
    }

    public void deleteWordById(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        if(db == null)
            return;

        db.delete(TABLE_WORD, COL_ID + "= ?", new String[] { String.valueOf(id) });
        db.close();
    }

    public void deleteMeaningByWordId(int wordId) {

        SQLiteDatabase db = this.getWritableDatabase();
        if(db == null)
            return;

        db.delete(TABLE_MEANING, COL_FK_WORD_ID + "= ?", new String[] { String.valueOf(wordId) });
        db.close();
    }

    public void deleteExampleByWordId(int wordId) {

        SQLiteDatabase db = this.getWritableDatabase();
        if(db == null)
            return;

        db.delete(TABLE_EXAMPLE, COL_FK_WORD_ID + "= ?", new String[]{String.valueOf(wordId)});
        db.close();
    }

    public Vocab getRandomVocab () {

        Vocab vocab = new Vocab();
        Word word = getRandomWord();
        vocab.setWord(word);
        vocab.setMeaning(getMeaningsByid(word.getId()));
        vocab.setExample(getExamplesByid(word.getId()));

        return vocab;
    }

    //Random word Method

    public Word getRandomWord() {

        String SELECT_WORD = "SELECT * FROM " + TABLE_WORD + " ORDER BY RANDOM() LIMIT 1";
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
        db.close();

        return word;
    }


    public int getTypeIdByWord(String type) {

        String SELECT_TYPE = "SELECT * FROM " + TABLE_TYPE + " WHERE "+ COL_TYPE + " = " + "\""+ type + "\"";
        Log.e(LOG, SELECT_TYPE);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(SELECT_TYPE, null);

        if (c != null)
            c.moveToFirst();

        int id = c.getInt(c.getColumnIndex(COL_ID));

        if(id > -1)
            return id;

        return 0;
    }


    public String getTypeByID(int id) {

        String SELECT_TYPE = "SELECT * FROM " + TABLE_TYPE + " WHERE "+ COL_ID + " = " + id ;
        Log.e(LOG, SELECT_TYPE);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(SELECT_TYPE, null);

        if (c != null) {
            c.moveToFirst();

            if (c.getCount() > 0) {
                return c.getString(c.getColumnIndex(COL_TYPE));
            }
        }

        return "";

    }

    public  void restoreDB(InputStream is, Context context, IFileStatusListener fileStatusListener) {
        Context ctx = context;
        try {
            File file = ctx.getDatabasePath(DATABASE_NAME);
            Log.d("DB", "restoreDB: path" + file.getAbsolutePath());
            if (file.exists())
                file.delete();
            FileOutputStream mOutput = new FileOutputStream(file);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = is.read(mBuffer)) > 0) {
                mOutput.write(mBuffer, 0, mLength);
            }
            mOutput.flush();
            mOutput.close();
            is.close();
            fileStatusListener.onFileRestored();
        } catch (Exception e) {
            fileStatusListener.onFileFailed(DriveMode.restore);
        }
    }




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
            + COL_IS_FAVOURITE + " INTEGER DEFAULT 0"
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


    // ALTER QUERIES

    private static final String ALTER_TABLE_WORD_ADD_COL_IS_FAVOURITE
            = "ALTER TABLE " + TABLE_WORD + " ADD COLUMN " + COL_IS_FAVOURITE + " INTEGER DEFAULT 0" ;

    /* SELECT QUERY */

    private static final String SELECT_ALL_VOCAB = "SELECT * FROM " + TABLE_WORD;

}
