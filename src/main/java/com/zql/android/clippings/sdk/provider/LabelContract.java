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

import static com.zql.android.clippings.sdk.provider.ClippingsProvider.AUTHORITY;

/**
 * @author qinglian.zhang, created on 2017/3/6.
 */
public class LabelContract {

    //---------------------------- label table  ------------------------------------//
    public static final String DB_LABEL = "db_label";

    public static final String TABLE_LABEL = "t_label";

    public static final String TABLE_LABEL_ID = "_id";

    public static final String TABLE_LABEL_MD5 = "_md5";

    public static final String TABLE_LABEL_LABEL = "_label";

    //---------------------------- label provider  ------------------------------------//

    public static final String LABEL_PATH = "labels";

    public static final Uri LABEL_URI = Uri.parse("content://" + AUTHORITY + "/" + LABEL_PATH);

    public static final String[] LABEL_PROJECTION_ALL = {TABLE_LABEL_MD5, TABLE_LABEL_LABEL};

    public static final String LABEL_SELECTION_MD5 = " " + TABLE_LABEL_MD5 + "=?";
    public static final String LABEL_SELECTION_LABEL = " " + TABLE_LABEL_LABEL + "=?";

    public static final String LABEL_SELECTION_MD5_LABEL = " " + TABLE_LABEL_MD5 + "=?" + " and " + TABLE_LABEL_LABEL + "=?";
}
