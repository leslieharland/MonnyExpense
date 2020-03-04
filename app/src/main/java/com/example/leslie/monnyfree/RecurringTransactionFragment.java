package com.example.leslie.monnyfree;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.leslie.monnyfree.common.DateInputDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Leslie on 4/15/2018.
 */


public class RecurringTransactionFragment extends android.support.v4.app.Fragment {
    @OnClick({R.id.dateFrom, R.id.dateTo})
    public void showDatePicker(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        DatePicker picker = new DatePicker(getContext());
        picker.setCalendarViewShown(false);
        builder.setView(picker);
    }
    @OnClick(R.id.llRecurringDates)
    public void onClick(){
        DateInputDialog dialog = new DateInputDialog();
        dialog.show(getActivity().getSupportFragmentManager(), "");
    }
    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.recurring_mode_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
