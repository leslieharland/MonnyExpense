package com.example.leslie.monnyfree.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.leslie.monnyfree.R;

import butterknife.ButterKnife;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.WINDOW_SERVICE;


/**
 * Created by Leslie on 4/15/2018.
 */

public class DateInputDialog extends DialogFragment {
    static final int DATE_INPUT_DIALOG = 0;


    public static DateInputDialog getInstance(){
        return new DateInputDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.recurring_date_dialog, null);

        ButterKnife.bind(this, dialogView);
        Button btnSave = dialogView.findViewById(R.id.dbSave);
        btnSave.setOnClickListener(v -> {
                    String dateFrom = ((TextView) dialogView.findViewById(R.id.txtViewFrom)).getText().toString();
            String dateTo = ((TextView) dialogView.findViewById(R.id.txtViewTo)).getText().toString();
        });

        TextView txtViewMonth = dialogView.findViewById(R.id.txtViewMonth);
        txtViewMonth.setOnClickListener(v -> {
            createFragment();
            createFragment();
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        return builder.create();
    }


    private void createFragment() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        WindowManager wm = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.recurring_date_dialog, null);

        // Add layout to window manager
        wm.addView(myView, params);
    }
}
