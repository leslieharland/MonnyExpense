package com.example.leslie.monnyfree.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leslie.monnyfree.R;
import com.example.leslie.monnyfree.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Leslie on 4/15/2018.
 */

    public class CategoryListAdapter extends ArrayAdapter<Category> {

        Context context;
        int layoutResId;
        List<Category> data = null;

        public CategoryListAdapter(Context context, int layoutResId, List<Category> data) {
            super(context, layoutResId, data);
            this.layoutResId = layoutResId;
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CategoryHolder holder = null;

            if(convertView == null)
            {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                convertView = inflater.inflate(layoutResId, parent, false);

                holder = new CategoryHolder();
                holder.imageIcon = convertView.findViewById(R.id.icon);
                holder.textTitle = convertView.findViewById(R.id.title);

                convertView.setTag(holder);
            }
            else
            {
                holder = (CategoryHolder) convertView.getTag();
            }

            Category category = data.get(position);
            holder.textTitle.setText(category.getCategoryName());
            Picasso.get().load(category.getCategoryIcon()).into(holder.imageIcon);


            return convertView;
        }

        static class CategoryHolder
        {
            ImageView imageIcon;
            TextView textTitle;
        }

}
