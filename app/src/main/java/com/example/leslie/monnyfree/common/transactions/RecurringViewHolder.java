package com.example.leslie.monnyfree.common.transactions;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leslie.monnyfree.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecurringViewHolder {


    @BindView(R.id.lblDate)
    TextView lblDate;
    @BindView(R.id.txtViewDate)
    TextView txtViewDate;
    @BindView(R.id.dateFrom)
    TextView dateFrom;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.dateTo)
    TextView dateTo;
    @BindView(R.id.llRecurringDates)
    LinearLayout llRecurringDates;
    //R.layout.recurring_mode_fragment

    public RecurringViewHolder(Activity view) {
        ButterKnife.bind(this, view);

    }
}
