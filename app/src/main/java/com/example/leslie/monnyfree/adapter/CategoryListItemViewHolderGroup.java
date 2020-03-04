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

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leslie.monnyfree.R;

/**
 * View Holder for the Category list group item (Category).
 */
public class CategoryListItemViewHolderGroup {
    public CategoryListItemViewHolderGroup(View view) {
        collapseImageView = view.findViewById(R.id.expandable_list_indicator);
        text1 =  view.findViewById(android.R.id.text1);

        text2 =  view.findViewById(android.R.id.text2);

        selector = view.findViewById(R.id.selector);
        linearLayoutContainer = view.findViewById(R.id.linearLayoutContainer);
    }

    public ImageView collapseImageView;
    public TextView text1;
    public LinearLayout selector;
    public LinearLayout linearLayoutContainer;
    public TextView text2;
}
