package com.example.leslie.monnyfree.common.transactions;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leslie.monnyfree.R;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DateComponentViewHolder {

    @BindView(R.id.llDate)
    LinearLayout llDate;
    @BindView(R.id.txtDate)
    TextView txtDate;

    public DateComponentViewHolder(View view){
        ButterKnife.bind(this, view);
    }
}
