package com.bathem.vocabpower.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.bathem.vocabpower.Entity.Category;

/**
 * Created by mehtab on 1/24/16.
 */
public class DataBaseHelper extends SQLiteOpenHelper{

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

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
            + "FOREIGN KEY (" + COL_FK_WORD_ID + ") REFERENCES " + TABLE_WORD + "(" + COL_ID + "),"
            + COL_FK_CATEGORY_ID + " INTEGER NOT NULL,"
            + "FOREIGN KEY (" + COL_FK_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + COL_ID + ")"
            + ")";

//    static DataBaseHelper instance;
//
//    public static DataBaseHelper getInstance() {
//
//        if(instance == null)
//            instance = new DataBaseHelper()
//    }

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
    public boolean createGroup(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_CATEGORY_NAME, category.getCatgoryName());

        // insert row
        long res = db.insert(TABLE_CATEGORY, null, values);

        if(res == -1)
            return  false;

        return true;
    }

}
