/*
 * Copyright (C) 2012-2018 The Android Money Manager Ex Project Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.leslie.monnyfree.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.leslie.monnyfree.R;


/**
 * View Holder for subcategory list item.
 */
public class CategoryListItemViewHolderChild {
    public CategoryListItemViewHolderChild(View view) {
//        textContainer = (ViewGroup) view.findViewById(R.id.textContainer);
        text1 =  view.findViewById(android.R.id.text1);
        text2 = view.findViewById(android.R.id.text2);
        selector = view.findViewById(R.id.selector);
        indent = view.findViewById(R.id.indent);
        imageView = view.findViewById(R.id.list_item_image);
        container = view.findViewById(R.id.linearLayoutContainer);
    }

//    public ViewGroup textContainer;
    public TextView text1;
    public TextView text2;
    public LinearLayout container;
    public LinearLayout selector;
    public ViewGroup indent;
    public ImageView imageView;
}
