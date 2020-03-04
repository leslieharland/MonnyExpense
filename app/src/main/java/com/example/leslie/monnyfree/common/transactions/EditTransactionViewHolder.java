package com.example.leslie.monnyfree.common.transactions;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.leslie.monnyfree.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Leslie on 3/23/2018.
 */

public class EditTransactionViewHolder {
    @BindView(R.id.textViewInputAmount)
    TextView textViewInputAmount;
    @BindView(R.id.llDescription)
    RelativeLayout llDescription;

    @BindView(R.id.recyclerContainer)
    LinearLayout recyclerContainer;

    //R.layout.add_expense_fragment_activity

    public EditTransactionViewHolder(Activity view) {
        ButterKnife.bind(this, view);

    }

    public EditTransactionViewHolder(View view){
        ButterKnife.bind(this, view);
    }
}

