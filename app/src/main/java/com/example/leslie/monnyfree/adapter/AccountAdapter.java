package com.example.leslie.monnyfree.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leslie.monnyfree.Account;
import com.example.leslie.monnyfree.R;
import com.example.leslie.monnyfree.common.transactions.EditTransactionCommonFunctions;

import java.io.IOException;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountViewHolder> {
    private List<Account> labels;
    private Context mContext;
    Boolean isSelected = false;
    int row_index = -1;
    EditTransactionCommonFunctions mCommon;

    public AccountAdapter(List<Account> labels, Context context) {
        this.labels = labels;
        mContext = context;
        mCommon = new EditTransactionCommonFunctions();
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.account_item;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AccountViewHolder holder, final int position) {
        if (labels.size() > 0) {
            Account account = labels.get(position);
            holder.txtViewAccountName.setText(account.getName());
            holder.txtViewNumberOfRecords.setText(String.valueOf(account.getNumberOfRecords()));
            holder.txtViewAbv.setText(account.getCurrencyCode());

            try {
                Drawable d = Drawable.createFromStream(mContext.getAssets().open("account/" + account.getImage()), null);
                holder.ivAccount.setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }


            holder.cardView.setOnClickListener(view -> {
                row_index = position;
                notifyDataSetChanged();
            });
            if (row_index == position) {
                holder.txtDefaultAccount.setVisibility(View.VISIBLE);

            } else {
                holder.txtDefaultAccount.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return labels.size();
    }
}
