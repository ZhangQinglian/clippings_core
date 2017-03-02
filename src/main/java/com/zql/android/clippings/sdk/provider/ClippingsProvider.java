/*******************************************************************************
 *    Copyright 2017-present, Clippings Contributors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/

package com.zql.android.clippings.sdk.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.zql.android.clippings.sdk.parser.Clipping;
import com.zqlite.android.logly.Logly;

import java.util.Arrays;

/**
 * @author qinglian.zhang, created on 2017/2/22.
 */
public class ClippingsProvider extends ContentProvider {

    private final Logly.Tag kTag = new Logly.Tag(Logly.FLAG_THREAD_NAME,ClippingsProvider.class.getSimpleName(),Logly.DEBUG);

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ClippingContract.AUTHORITY,ClippingContract.CLIPPINGS_PATH,1);
        sUriMatcher.addURI(ClippingContract.AUTHORITY,ClippingContract.CLIPPINGS_PATH +"/#",2);
    }

    private ClippingsDBHelper mDBHelper ;
    @Override
    public boolean onCreate() {
        mDBHelper = new ClippingsDBHelper(getContext(),ClippingContract.DB_CLIPPINGS,null,1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Logly.d("***************** clippings provider query  ***********************");
        Logly.d(kTag,uri.toString());
        Logly.d(kTag, Arrays.toString(projection));
        Logly.d(kTag,selection);
        Logly.d(kTag,Arrays.toString(selectionArgs));
        Logly.d(kTag,sortOrder);
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)){
            case 1:
                sortOrder = " " + ClippingContract.TABLE_CLIPPINGS_DATE;

                if(selection == null){
                    selection = ClippingContract.CLIPPING_TYPE_SELECTION;
                }else {
                    selection = selection + " and " + ClippingContract.CLIPPING_TYPE_SELECTION;
                }
                if(selectionArgs == null){
                    selectionArgs = new String[]{String.valueOf(Clipping.K_CLIPPING_TYPE_BOOKMARK)};
                }else {
                    selectionArgs = Arrays.copyOf(selectionArgs,selectionArgs.length + 1);
                    selectionArgs[selectionArgs.length-1] = String.valueOf(Clipping.K_CLIPPING_TYPE_BOOKMARK);
                }
                return db.query(ClippingContract.TABLE_CLIPPINGS,projection,selection,selectionArgs,null,null,sortOrder);
            case 2:
                String id = uri.getLastPathSegment();
                return db.query(ClippingContract.TABLE_CLIPPINGS,projection,ClippingContract.CLIPPING_ID_SELECTION,new String[]{id},null,null,sortOrder);
            default:
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Logly.d("***************** clippings provider insert  ***********************");
        int match = sUriMatcher.match(uri);
        if(match == 1){
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            //Logly.d(kTag,values.toString());
            long id = db.insert(ClippingContract.TABLE_CLIPPINGS,"",values);
            return  Uri.withAppendedPath(ClippingContract.CLIPPINGS_URI,String.valueOf(id));
        }
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

    private class ClippingsDBHelper extends SQLiteOpenHelper{

        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INTEGER";
        private static final String COMMA_SEP = ",";
        private final String kCreateClippingsTableSQL =
                        "CREATE TABLE " + ClippingContract.TABLE_CLIPPINGS + " (" +
                                ClippingContract.TABLE_CLIPPINGS_ID + INTEGER_TYPE +" PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                                ClippingContract.TABLE_CLIPPINGS_TITLE + TEXT_TYPE + COMMA_SEP +
                                ClippingContract.TABLE_CLIPPINGS_AUTHOR + TEXT_TYPE + COMMA_SEP +
                                ClippingContract.TABLE_CLIPPINGS_LOCATION + TEXT_TYPE + COMMA_SEP +
                                ClippingContract.TABLE_CLIPPINGS_DATE + INTEGER_TYPE + COMMA_SEP +
                                ClippingContract.TABLE_CLIPPINGS_TYPE + INTEGER_TYPE + COMMA_SEP +
                                ClippingContract.TABLE_CLIPPINGS_CONTENT + TEXT_TYPE + COMMA_SEP +
                                ClippingContract.TABLE_CLIPPINGS_MD5 + TEXT_TYPE + COMMA_SEP+
                                ClippingContract.TABLE_CLIPPINGS_STATUS + INTEGER_TYPE +
                        ")";

        public ClippingsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Logly.d(kCreateClippingsTableSQL);
            db.execSQL(kCreateClippingsTableSQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
