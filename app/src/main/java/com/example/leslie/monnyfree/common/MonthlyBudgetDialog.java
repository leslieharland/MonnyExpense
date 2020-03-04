package com.example.leslie.monnyfree.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.leslie.monnyfree.Constants;
import com.example.leslie.monnyfree.R;

public class MonthlyBudgetDialog extends DialogFragment {
    static final int DATE_INPUT_DIALOG = 0;
    DialogListener listener;

    public static MonthlyBudgetDialog getInstance(){
        return new MonthlyBudgetDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.monthly_budget_dialog, null);

        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> {
            String inputBudget = ((EditText) dialogView.findViewById(R.id.inputBudget)).getText().toString();

            Bundle bundle = new Bundle();
            bundle.putString(Constants.INPUT_BUDGET_AMOUNT, inputBudget);

            Intent intent = new Intent().putExtras(bundle);

            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

            dismiss();
            //listener.onSaveDialog(inputBudget);
            this.dismiss();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        return builder.create();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (DialogListener) activity;
        }
        catch (ClassCastException e) {
            Log.d("DialogListener", activity.getClass().getSimpleName() + " doesn't implement this interface");
        }
    }
}