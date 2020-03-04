package com.example.leslie.monnyfree.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leslie.monnyfree.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    ImageView imageIcon;
    TextView textTitle;
    TextView textId;
    LinearLayout lButton;
    RecyclerView recyclerView;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.recycler_view);
        lButton = itemView.findViewById(R.id.lButton);
        textTitle = itemView.findViewById(R.id.title);
        textId = itemView.findViewById(R.id.categoryId);
        imageIcon = itemView.findViewById(R.id.icon);
    }

}
