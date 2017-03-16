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

import android.net.Uri;


import java.util.List;

import static com.zql.android.clippings.sdk.provider.ClippingsProvider.AUTHORITY;

/**
 * @author qinglian.zhang, created on 2017/2/22.
 */
public class ClippingContract {

    //---------------------------- clippings table  ------------------------------------//
    public static final String DB_CLIPPINGS = "db_clippings";
    public static final String TABLE_CLIPPINGS = "t_clippings";
    public static final String TABLE_CLIPPINGS_ID = "_id";
    public static final String TABLE_CLIPPINGS_TITLE = "_title";
    public static final String TABLE_CLIPPINGS_AUTHOR = "_author";
    public static final String TABLE_CLIPPINGS_TYPE = "_type";
    public static final String TABLE_CLIPPINGS_LOCATION= "_location";
    public static final String TABLE_CLIPPINGS_DATE= "_date";
    public static final String TABLE_CLIPPINGS_CONTENT= "_content";
    public static final String TABLE_CLIPPINGS_MD5 = "_md5";
    public static final String TABLE_CLIPPINGS_STATUS = "_status";
    public static final String TABLE_CLIPPINGS_FAVOURITE = "_favourite";

    //---------------------------- clippings provider  ------------------------------------//

    public static final String CLIPPINGS_PATH = "clippings";
    public static final Uri CLIPPINGS_URI = Uri.parse("content://" + AUTHORITY + "/" + CLIPPINGS_PATH);

    public static final String[] PROJECTION_CLIPPINGS_ALL = {
            TABLE_CLIPPINGS_ID,
            TABLE_CLIPPINGS_TITLE,
            TABLE_CLIPPINGS_AUTHOR,
            TABLE_CLIPPINGS_TYPE,
            TABLE_CLIPPINGS_LOCATION,
            TABLE_CLIPPINGS_DATE,
            TABLE_CLIPPINGS_CONTENT,
            TABLE_CLIPPINGS_MD5,
            TABLE_CLIPPINGS_STATUS,
            TABLE_CLIPPINGS_FAVOURITE
    };


    public static final String CLIPPING_TYPE_SELECTION = ClippingContract.TABLE_CLIPPINGS_TYPE + "!=?";

    public static final String CLIPPING_ID_SELECTION = ClippingContract.TABLE_CLIPPINGS_ID + "=?";

    public static final String CLIPPING_FAVOURITE_SELECTION = ClippingContract.TABLE_CLIPPINGS_FAVOURITE + "=?";

    public static final String CLIPPING_NOTE_SELECTION = " " + ClippingContract.TABLE_CLIPPINGS_TITLE + "=? and " +
            ClippingContract.TABLE_CLIPPINGS_AUTHOR + "=? and " +
            ClippingContract.TABLE_CLIPPINGS_LOCATION + "=? and " +
            ClippingContract.TABLE_CLIPPINGS_TYPE + "=?";

    public static String getMd5Selection(List<String> md5s){
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<md5s.size() ;i++){
            if(i < md5s.size()  -1){
                sb.append(" ").append(ClippingContract.TABLE_CLIPPINGS_MD5).append("=? || ");
            }else {
                sb.append(" ").append(ClippingContract.TABLE_CLIPPINGS_MD5).append("=?");
            }
        }
        return sb.toString();
    }
}
