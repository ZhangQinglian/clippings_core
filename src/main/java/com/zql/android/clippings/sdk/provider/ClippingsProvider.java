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
import java.util.concurrent.Executors;

/**
 * @author qinglian.zhang, created on 2017/2/22.
 */
public class ClippingsProvider extends ContentProvider {

    public static final String AUTHORITY = "com.zql.android.clippings.sdk.provider";

    private final Logly.Tag kTag = new Logly.Tag(Logly.FLAG_THREAD_NAME,ClippingsProvider.class.getSimpleName(),Logly.DEBUG);

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY,ClippingContract.CLIPPINGS_PATH,1);
        sUriMatcher.addURI(AUTHORITY,ClippingContract.CLIPPINGS_PATH +"/#",2);
        sUriMatcher.addURI(AUTHORITY,LabelContract.LABEL_PATH,10);
    }

    private ClippingsDBHelper mDBHelper ;
    @Override
    public boolean onCreate() {
        mDBHelper = new ClippingsDBHelper(getContext(),ClippingContract.DB_CLIPPINGS,null,2);
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
            case 10:
                return db.query(LabelContract.TABLE_LABEL,projection,selection,selectionArgs,null,null,sortOrder);
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
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        switch (match){
            case 1:
                //Logly.d(kTag,values.toString());
                String md5 = values.getAsString(ClippingContract.TABLE_CLIPPINGS_MD5);
                if(existByMd5(md5,db)) {
                    db.close();
                    return ClippingContract.CLIPPINGS_URI;
                }
                long id = db.insert(ClippingContract.TABLE_CLIPPINGS,"",values);
                getContext().getContentResolver().notifyChange(ClippingContract.CLIPPINGS_URI,null);
                db.close();
                return  Uri.withAppendedPath(ClippingContract.CLIPPINGS_URI,String.valueOf(id));
            case 10:
                getContext().getContentResolver().notifyChange(LabelContract.LABEL_URI,null);
                long labelId =  db.insert(LabelContract.TABLE_LABEL,"",values);
                db.close();
                return Uri.withAppendedPath(LabelContract.LABEL_URI,String.valueOf(labelId));

        }

        return null;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        switch (match){
            case 10:
                int result =  db.delete(LabelContract.TABLE_LABEL,selection,selectionArgs);
                getContext().getContentResolver().notifyChange(ClippingContract.CLIPPINGS_URI,null);
                return result;
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        switch (match){
            case 1:
                int result =  db.update(ClippingContract.TABLE_CLIPPINGS,values,selection,selectionArgs);
                getContext().getContentResolver().notifyChange(ClippingContract.CLIPPINGS_URI,null);
                return result;
        }
        return 0;
    }


    private boolean existByMd5(String md5,SQLiteDatabase sqLiteDatabase){
        Cursor cursor = sqLiteDatabase.query(ClippingContract.TABLE_CLIPPINGS,ClippingContract.PROJECTION_CLIPPINGS_ALL,LabelContract.LABEL_SELECTION_MD5,new String[]{md5},null,null,null);
        if(cursor != null){
            try {
                if(cursor.getCount()>0) {
                    return true;
                }else {
                    return false;
                }
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }finally {
                cursor.close();
            }
        }else {
            return false;
        }
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

        private final String kCreateLabelTableSQL =
                "CREATE TABLE " + LabelContract.TABLE_LABEL + " (" +
                        LabelContract.TABLE_LABEL_ID + INTEGER_TYPE +" PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                        LabelContract.TABLE_LABEL_MD5 + TEXT_TYPE + COMMA_SEP +
                        LabelContract.TABLE_LABEL_LABEL + TEXT_TYPE +
                        ")";

        public ClippingsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Logly.d(kCreateClippingsTableSQL);
            db.execSQL(kCreateClippingsTableSQL);
            db.execSQL(kCreateLabelTableSQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if(newVersion == 2){
                db.execSQL("ALTER TABLE " + ClippingContract.TABLE_CLIPPINGS + " ADD " + ClippingContract.TABLE_CLIPPINGS_FAVOURITE + INTEGER_TYPE);
            }
        }
    }
}
