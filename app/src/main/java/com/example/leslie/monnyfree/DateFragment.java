package com.example.leslie.monnyfree;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leslie.monnyfree.common.transactions.DateComponentViewHolder;
import com.example.leslie.monnyfree.common.transactions.EditTransactionCommonFunctions;
import com.example.leslie.monnyfree.model.Expense;
import com.example.leslie.monnyfree.utils.DateUtil;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DateFragment extends Fragment {
    @BindView(R.id.txtDate)
    TextView txtDate;
    Unbinder unbinder;

    @BindView(R.id.llDate)
    LinearLayout dateLayout;
    private EditTransactionCommonFunctions mCommon;

    @OnClick(R.id.llDate)
    public void onClick() {
        Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mCommon.updateLabel(myCalendar);
        };
        new DatePickerDialog(getActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_view, viewGroup, false);
        mCommon = new EditTransactionCommonFunctions(view, (AppCompatActivity) getActivity());
        mCommon.initDatePicker();
        unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null) initializeModel();
        return view;
    }

    public void initializeModel() {
        txtDate.setText(getArguments().getString(Expense.DATE, "Today"));
    }

    public void setDate(String date) {
        txtDate.setText(date);
    }
    public Date getDate() {
        if (txtDate != null && !TextUtils.isEmpty(txtDate.getText().toString())) {
            String date = txtDate.getText().toString();
            if (date.equals("Today")) {
                return new Date();
            }
            return DateUtil.convertToDate(date);
        }
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Expense expense = new Expense();
        expense.setDate(getDate());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
