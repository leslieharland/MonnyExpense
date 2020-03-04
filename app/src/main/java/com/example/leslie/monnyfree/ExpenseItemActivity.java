package com.example.leslie.monnyfree;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.leslie.monnyfree.common.MonnyBaseFragmentActivity;
import com.example.leslie.monnyfree.model.DateGroup;
import com.example.leslie.monnyfree.model.Expense;
import com.example.leslie.monnyfree.utils.BitmapUtil;
import com.example.leslie.monnyfree.utils.DateUtil;
import com.example.leslie.monnyfree.utils.MoneyService;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.leslie.monnyfree.Constants.EXPENSEID;
import static com.example.leslie.monnyfree.Constants.KEY_EXPENSE_ID;

public class ExpenseItemActivity extends MonnyBaseFragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    protected Toolbar toolbar;
    @BindView(R.id.txtViewAmount)
    TextView txtViewAmount;
    @BindView(R.id.txtViewCategory)
    TextView txtViewCategory;
    @BindView(R.id.txtViewDate)
    TextView txtViewDate;
    @BindView(R.id.txtViewDescription)
    TextView txtViewDescription;
    @BindView(R.id.btnDelete)
    Button btnDelete;

    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.ivCategoryIcon)
    ImageView ivCategoryIcon;

    Toolbar mToolbar;
    MoneyService moneyService;
    Expense expense = new Expense();
    private int mExpenseId;
    private static Expense mExpense = new Expense();

    @OnClick(R.id.btnDelete)
    public void onClick() {
        new MaterialDialog.Builder(this)
                .title(R.string.delete)
                .content(R.string.delete_content)
                .positiveText(R.string.delete)
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                    onDelete();
                })
                .negativeText(R.string.cancel)
                .onNegative((dialog, which) -> {
                    dialog.dismiss();
                })
                .build().show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_detail_fragment);
        ButterKnife.bind(this);
        initializeToolbar();
        setDisplayHomeAsUpEnabled(true);
        if (getIntent().getExtras() != null) {
            mExpenseId = getIntent().getExtras().getInt(EXPENSEID);
            Log.d(TAG, "Expense Id: " + mExpenseId);
        }
        moneyService = new MoneyService();
        getSupportLoaderManager().initLoader(0, null, this);
    }

    public void initializeToolbar(){
        toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Expense Detail");
    }

    public void onDelete() {
        try {
            String where = Expense.EXPENSEID + "=?";
            String where2 = DateGroup.EXPENSEID + "=?";

            if (mExpenseId != 0) {
                String[] args = new String[]{String.valueOf(mExpenseId)};
                int rows;
                rows = getContentResolver().delete(Expense.getUri(), where, args);
                getContentResolver().delete(DateGroup.getUri(), where2, args);
                if (rows > 0) {
                    finish();
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "onSave: " + e.getMessage());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> result = null;

        try {
            Uri singleUri = ContentUris.withAppendedId(Expense.getUri(), mExpenseId);
            result = new CursorLoader(this,
                    singleUri, null, Expense.EXPENSEID + "=?", new String[]{String.valueOf(mExpenseId)}, null);
        } catch (Exception ex) {
            Log.d(TAG, "onCreateLoader: " + ex.getMessage());
        }

        return result;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        try {

            data.moveToFirst();
            if (!data.isAfterLast()) {
                Log.d(TAG, "onLoadFinished: " + "test");
                expense.setId(data.getInt(data.getColumnIndex(Expense.EXPENSEID)));
                expense.setDate(DateUtil.convertToDate(data.getString(data.getColumnIndex(Expense.DATE))));
                expense.setImage(data.getString(data.getColumnIndex(Expense.IMAGE)));
                expense.setDescription(data.getString(data.getColumnIndex(Expense.DESCRIPTION)));
                expense.setInputAmount(Float.parseFloat(String.valueOf(data.getDouble(data.getColumnIndex(Expense.AMOUNT)))));
                expense.setCategoryId(data.getInt(data.getColumnIndex(Expense.CATEGORYID)));
                expense.setCategoryName(data.getString(data.getColumnIndex(Expense.CATEGORYNAME)));
                expense.setCategoryIcon(data.getString(data.getColumnIndex(Expense.CATEGORYICON)));
            }
            data.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtViewAmount.setText(moneyService.convertToTwoDecimalPlaceString(expense.getInputAmount()));
        txtViewCategory.setText(String.valueOf(expense.getCategoryName()));
        txtViewDate.setText(DateUtil.formatDateString(expense.getDate()));
        if (expense.getDescription() != null) {
            txtViewDescription.setText(String.valueOf(expense.getDescription()));
        }
        try {
            ivCategoryIcon.setImageDrawable(Drawable.createFromStream(getAssets().open("categoryicgreen/" + expense.getCategoryIcon()), null));
        } catch (IOException e) {
            Log.e(TAG, "onLoadFinished: " + e.getLocalizedMessage());
        }
        mExpenseId = expense.getId();

        ivImage.setImageBitmap(BitmapUtil.convertToBitmap(expense.getImage()));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivImage.setLayoutParams(layoutParams);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.clear();
        getMenuInflater().inflate(R.menu.view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button_default_red
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_edit:
                startActivity(new Intent(ExpenseItemActivity.this, ExpenseEditActivity.class).setAction(Intent.ACTION_EDIT).putExtra(KEY_EXPENSE_ID, mExpenseId));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
