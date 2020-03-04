package com.example.leslie.monnyfree.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leslie.monnyfree.CategoryActivity;
import com.example.leslie.monnyfree.ExpenseEditActivity;
import com.example.leslie.monnyfree.R;
import com.example.leslie.monnyfree.common.transactions.EditTransactionCommonFunctions;
import com.example.leslie.monnyfree.model.Category;

import java.io.IOException;
import java.util.List;

import static com.example.leslie.monnyfree.common.transactions.EditTransactionCommonFunctions.mCategory;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private static final String TAG = CategoryAdapter.class.getSimpleName();
    private List<Category> labels;
    private Context mContext;
    Boolean isSelected = false;
    int row_index = -1;
    EditTransactionCommonFunctions mCommon;

    public CategoryAdapter(List<Category> labels, Context context) {
        this.labels = labels;
        mContext = context;
        mCommon = new EditTransactionCommonFunctions();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = 0;
        if (mContext instanceof ExpenseEditActivity) {
            layout = R.layout.simple_recycler_button;
        } else if (mContext instanceof CategoryActivity) {
            layout = R.layout.li_category;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, final int position) {
        if (labels != null) {
            final Category category = labels.get(position);
            holder.textTitle.setText(category.getCategoryName());
            holder.imageIcon.setContentDescription(category.getCategoryIcon());

            try {
                String folderToSelect = "";
                if (mContext instanceof ExpenseEditActivity) {
                    folderToSelect = "categoryic/";
                    holder.lButton.setId(category.getCategoryId());
                } else if (mContext instanceof CategoryActivity) {
                    folderToSelect = "categoryicgreen/";
                }
                Drawable d = Drawable.createFromStream(mContext.getAssets().open(folderToSelect + category.getCategoryIcon()), null);
                holder.imageIcon.setImageDrawable(d);
            } catch (IOException e) {
                Log.e(TAG, "onBindViewHolder:" + e.getLocalizedMessage());
            }


            if (mContext instanceof ExpenseEditActivity) {
                holder.lButton.setOnClickListener(view -> {
                    row_index = position;
                    mCategory.setCategoryId(holder.lButton.getId());
                    notifyDataSetChanged();
                });
                if (row_index == position) {
                    setItemSelected(holder);


                } else {
                    setItemUnSelected(holder);
                }

                if (category.getCategoryId() == mCategory.getCategoryId()) {
                    setItemSelected(holder);
                }
            }

        }

    }

    public void setItemSelected(CategoryViewHolder holder) {
        holder.lButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.monny_default));
        holder.textTitle.setTextColor(Color.parseColor("#ffffff"));
    }

    public void setItemUnSelected(CategoryViewHolder holder) {
        holder.lButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.monny_green));
        holder.textTitle.setTextColor(ContextCompat.getColor(mContext, R.color.monny_default));
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }
}
