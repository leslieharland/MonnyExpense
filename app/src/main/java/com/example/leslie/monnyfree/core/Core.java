package com.example.leslie.monnyfree.core;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.leslie.monnyfree.Constants;
import com.example.leslie.monnyfree.R;

/**
 * Created by Leslie on 3/25/2018.
 */

public class Core {

    private final Context mContext;

    public Core(Context context){
        super();
        this.mContext = context;
    }

    public void alert(int textResourceId){
        alert(Constants.NOT_SET, textResourceId);
    }

    private void alert(int title, int textResourceId) {
        if (title == Constants.NOT_SET) {
            title = R.string.no_title;
        }

        new MaterialDialog.Builder(getContext())
                .iconRes(R.drawable.ic_info)
                .title(title)
                .content(textResourceId)
                .positiveText(android.R.string.ok)
                .onPositive((dialog, which) -> dialog.dismiss())
                .show();
    }

    public Context getContext() {
        return mContext;
    }

}
