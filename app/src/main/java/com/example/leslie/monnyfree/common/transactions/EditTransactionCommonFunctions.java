package com.example.leslie.monnyfree.common.transactions;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.leslie.monnyfree.core.RequestCodes;
import com.example.leslie.monnyfree.model.Category;
import com.example.leslie.monnyfree.utils.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Leslie on 3/23/2018.
 */

public class EditTransactionCommonFunctions {
    private static final String TAG = EditTransactionCommonFunctions.class.getSimpleName();
    public EditTransactionViewHolder editTransactionViewHolder;
    public DateComponentViewHolder dateViewHolder;
    private AppCompatActivity activity;
    public static String mDescription;
    public static Category mCategory;
    public static String mImage;
    public int btnId;
    BottomSheetBehavior bottomSheetBehavior;
    View v;

    public EditTransactionCommonFunctions() {

    }

    public EditTransactionCommonFunctions(View v, AppCompatActivity activity) {
        this.activity = activity;
        this.v = v;

    }

    public EditTransactionCommonFunctions(AppCompatActivity activity) {
        this.activity = activity;
        editTransactionViewHolder = new EditTransactionViewHolder(activity);
    }


    //endregion
    //region Datepicker

    public void initDatePicker() {
        dateViewHolder = new DateComponentViewHolder(v);
    }


    public void updateLabel(Calendar myCalendar) {
        dateViewHolder = new DateComponentViewHolder(v);
        if (!DateUtil.dateEquals(myCalendar.getTime(), new Date())) {
            dateViewHolder.txtDate.setText(DateUtil.formatDateString(myCalendar.getTime()));
        } else {
            dateViewHolder.txtDate.setText("Today");
        }

    }

    //endregion Datepicker

    public void initCategory() {
  /*      final int childCount = editTransactionViewHolder.recyclerContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View b = editTransactionViewHolder.recyclerContainer.getChildAt(i);
            b.setBackgroundColor(getContext().getResources().getColor(R.color.monny_green));
        }
*/

    }





/*    public void initAmountSelectors() {

        // amount
        viewHolder.txtAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currencyId = getSourceCurrencyId();
                Money amount = transactionEntity.getAmount();

//                Intent intent = IntentFactory.getNumericInputIntent(getContext(), amount, currencyId);
//                getActivity().startActivityForResult(intent, REQUEST_AMOUNT);
                Calculator.forActivity(getActivity())
                        .currency(currencyId)
                        .amount(amount)
                        .show(RequestCodes.AMOUNT);
            }
        });

        // amount to
        viewHolder.txtAmountTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currencyId = getDestinationCurrencyId();
                Money amount = transactionEntity.getAmountTo();

//                Intent intent = IntentFactory.getNumericInputIntent(getContext(), amount, currencyId);
//                getActivity().startActivityForResult(intent, REQUEST_AMOUNT_TO);
                Calculator.forActivity(getActivity())
                        .amount(amount).currency(currencyId)
                        .show(RequestCodes.AMOUNT);
            }
        });
    }*/

    public void launchImageCropIntent(Uri uri, AppCompatActivity context) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(uri, "image/*");
            cropIntent.putExtra("crop", true);
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            cropIntent.putExtra("return-data", true);
            Intent openInChooser = Intent.createChooser(cropIntent, "");
            PackageManager pm = context.getPackageManager();
            List resInfo = pm.queryIntentActivities(cropIntent, 0);
            List intentList = new ArrayList();
            for (int i = 0; i < resInfo.size(); i++) {
                ResolveInfo ri = (ResolveInfo) resInfo.get(i);
                String packageName = ri.activityInfo.packageName;
                //Log.d(TAG, "launchImageCropIntent: " + packageName);
                if (packageName.contains("android.gallery3d")) {
                    cropIntent.setPackage(packageName);
                }
                intentList.add(new LabeledIntent(cropIntent, packageName, ri
                        .loadLabel(pm), ri.icon));
            }
            LabeledIntent[] extraIntents = (LabeledIntent[]) intentList
                    .toArray(new LabeledIntent[intentList.size()]);
            openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
            context.startActivityForResult(openInChooser, RequestCodes.CROP_IMAGE);
            //getContext().startActivityForResult(cropIntent, RequestCodes.CROP_IMAGE);
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Snackbar.make(getActivity().findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT);
        }
    }


    private AppCompatActivity getActivity() {
        return activity;
    }

}
