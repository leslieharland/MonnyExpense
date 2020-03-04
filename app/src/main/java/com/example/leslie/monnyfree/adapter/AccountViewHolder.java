package com.example.leslie.monnyfree.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leslie.monnyfree.R;

public class AccountViewHolder extends RecyclerView.ViewHolder {
    TextView txtViewAccountName;
    TextView txtViewAbv;
    TextView txtDefaultAccount;
    TextView txtViewNumberOfRecords;
    ImageView ivAccount;
    RecyclerView recyclerView;
    CardView cardView;

    public AccountViewHolder(View itemView) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.recycler_view);
        cardView = itemView.findViewById(R.id.cv);
        txtViewAccountName = itemView.findViewById(R.id.txtViewAccountName);
        txtDefaultAccount = itemView.findViewById(R.id.txtDefaultAccount);
        txtViewAbv = itemView.findViewById(R.id.txtViewAbv);
        txtViewNumberOfRecords = itemView.findViewById(R.id.txtViewNumberOfRecords);
        ivAccount = itemView.findViewById(R.id.iv_account);

    }
}
