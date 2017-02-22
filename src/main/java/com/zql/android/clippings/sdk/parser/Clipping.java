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

package com.zql.android.clippings.sdk.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/2/21.
 */

public class Clipping {

    public static final int K_CLIPPING_TYPE_BOOKMARK = 0x00;

    public static final int K_CLIPPING_TYPE_LABEL = 0x01;

    public static final int K_CLIPPING_TYPE_NOTE = 0x02;
    /**
     * 书名
     */
    public String title;

    /**
     * 作者
     */
    public String author;

    /**
     * 简报类型 <br />
     * {@link #K_CLIPPING_TYPE_BOOKMARK} <br />
     * {@link #K_CLIPPING_TYPE_LABEL} <br />
     * {@link #K_CLIPPING_TYPE_NOTE}
     */
    public int type;

    /**
     * 该简报在书本中的位置 <br />
     * 通常形式#211-213 <br />
     * 一般认为一个“位置”指的是128字节的数据 <br />
     */
    public String location;

    /**
     * 日期
     */
    public long date;

    /**
     * 标注内容 <br />
     * 仅当{@link Clipping#type}为{@link #K_CLIPPING_TYPE_LABEL}和{@link #K_CLIPPING_TYPE_NOTE}时有内容
     */
    public String content;

    @Override
    public String toString() {
        return title + " | " + author + " | " + location + " | " + type + " | " + date + " | \n" + content ;
    }
}
