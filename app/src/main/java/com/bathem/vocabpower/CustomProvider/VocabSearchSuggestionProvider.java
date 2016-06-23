package com.bathem.vocabpower.CustomProvider;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.bathem.vocabpower.Entity.Word;
import com.bathem.vocabpower.Model.DataModel;

import java.util.ArrayList;

/**
 * Created by mehtab on 23/06/16.
 */
public class VocabSearchSuggestionProvider extends ContentProvider {

    ArrayList<Word> mWordsList;

    @Override
    public boolean onCreate() {

        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        mWordsList = (ArrayList<Word>) DataModel.getCurrentWordList();

        MatrixCursor cursor = new MatrixCursor(
                new String[] {
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA
                }
        );


        if(mWordsList != null) {

            String query = uri.getLastPathSegment().toUpperCase();
            int limit = Integer.parseInt(uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT));

            for (int i = 0; i < mWordsList.size() && cursor.getCount() < limit; i++) {
                Word word = mWordsList.get(i);

                if (word.getWord().toUpperCase().contains(query)){
                    cursor.addRow(new Object[]{ i, word.getWord(), word.getId() });
                }
            }

        }

        return cursor;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
