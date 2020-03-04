package com.example.leslie.monnyfree.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.leslie.monnyfree.Constants;
import com.example.leslie.monnyfree.R;

public class CategoryDialog extends DialogFragment {
    static final int DATE_INPUT_DIALOG = 0;

    public static CategoryDialog getInstance() {
        return new CategoryDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_category, null);

        Button btnSave = dialogView.findViewById(R.id.btnSave);
        ImageView btnClose = dialogView.findViewById(R.id.btnClose);
        btnSave.setOnClickListener(v -> {
            String inputName = ((EditText) dialogView.findViewById(R.id.inputName)).getText().toString();

            Bundle bundle = new Bundle();
            bundle.putString(Constants.INPUT_CATEGORY_NAME, inputName);

            Intent intent = new Intent().putExtras(bundle);

            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

            dismiss();
            //listener.onSaveDialog(inputBudget);
            this.dismiss();
        });

        btnClose.setOnClickListener(v -> {
            dismiss();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        return builder.create();
    }

}
