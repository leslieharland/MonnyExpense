package com.example.leslie.monnyfree;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.leslie.monnyfree.common.MonnyBaseFragmentActivity;
import com.example.leslie.monnyfree.common.transactions.EditTransactionCommonFunctions;
import com.example.leslie.monnyfree.core.Core;
import com.example.leslie.monnyfree.core.RequestCodes;
import com.example.leslie.monnyfree.model.Category;
import com.example.leslie.monnyfree.model.DateGroup;
import com.example.leslie.monnyfree.model.Expense;
import com.example.leslie.monnyfree.model.RecurringExpense;
import com.example.leslie.monnyfree.utils.BitmapUtil;
import com.example.leslie.monnyfree.utils.DateUtil;
import com.example.leslie.monnyfree.utils.MoneyService;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.leslie.monnyfree.Constants.EXPENSEID;
import static com.example.leslie.monnyfree.Constants.EXPENSEMODEL;
import static com.example.leslie.monnyfree.Constants.KEY_EXPENSE_ID;
import static com.example.leslie.monnyfree.IntentActions.INTENT_RECURRING;
import static com.example.leslie.monnyfree.common.transactions.EditTransactionCommonFunctions.mCategory;
import static com.example.leslie.monnyfree.common.transactions.EditTransactionCommonFunctions.mDescription;
import static com.example.leslie.monnyfree.common.transactions.EditTransactionCommonFunctions.mImage;
/**
 * Created by Leslie on 3/15/2018.
 */

public class ExpenseEditActivity extends MonnyBaseFragmentActivity {
    private static final String TAG = ExpenseEditActivity.class.getSimpleName();
    private static final String FRAGMENTTAG = ExpenseEditActivity.class.getSimpleName() + "_Fragment";
    //BottomSheetDialog calculator;


    List<Category> categoryList;
    private static Expense mExpense;
    private static DateGroup mDateGroup;

    private EditTransactionCommonFunctions mCommon;
    private int mDateId;

    private final int LOADER_CATEGORIES = 1;

    //region Calculator variables
    private int[] idButtonKeyNum = {
            R.id.buttonKeyNum0, R.id.buttonKeyNum1, R.id.buttonKeyNum2, R.id.buttonKeyNum3,
            R.id.buttonKeyNum4, R.id.buttonKeyNum5, R.id.buttonKeyNum6, R.id.buttonKeyNum7,
            R.id.buttonKeyNum8, R.id.buttonKeyNum9,
            R.id.buttonKeyNumDecimal,
    };
    private int[] idOperatorKeys = {
            R.id.buttonKeyAdd, R.id.buttonKeyDiv,
            R.id.buttonKeyLess, R.id.buttonKeyMultiplication,
            R.id.buttonKeyLeftParenthesis, R.id.buttonKeyRightParenthesis
    };
    Integer mDefaultColor;
    boolean mAddition;
    boolean mDivision;
    boolean mSubtraction;
    boolean mMultiple;
    float mValue1, mValue2;

    DateFragment dateFragment;
    CategoryFragment categoryFragment;


    //endregion

    @BindView(R.id.textViewInputAmount)
    TextView txtInputAmount;
    @BindView(R.id.edtInputAmount)
    EditText edtInputAmount;
    private String MODEL;

    @BindView(R.id.imageIcon)
    ImageView imageIcon;

    @BindView(R.id.txtDescription)
    TextView txtDescription;
    BottomSheetBehavior sheetBehavior;
    static MoneyService moneyService = new MoneyService();
    static Account account = new Account();

    TextView toolbarText;

    @OnClick(R.id.textViewInputAmount)
    public void onClick() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @OnClick(R.id.btnSave)
    public void onSaveClick() {
        String[] selection = new String[]{DateGroup.DATEID};
        Cursor cursor = getContentResolver().query(DateGroup.getUri(), selection,
                DateGroup.DATESTRING + "=?", new String[]{DateUtil.formatDateString(dateFragment.getDate())}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                mDateId = cursor.getInt(cursor.getColumnIndex(DateGroup.DATEID));
            }
        }
        if (getExpenseId() == Constants.NOT_SET) {
            onSave();
        } else {
            if (onUpdate() > 0) {
                finish();
                startActivity(new Intent(this, ExpenseItemActivity.class).putExtra(EXPENSEID, getExpenseId()));
            }
        }
    }

    private int onUpdate() {
        Log.d(TAG, "onUpdate: ");
        if (validateData()) return -1;
        ContentValues contentValues = new ContentValues();
        contentValues.put(Expense.AMOUNT, txtInputAmount.getText().toString());
        contentValues.put(Expense.DESCRIPTION, mDescription);
        contentValues.put(Expense.IMAGE, mImage);
        contentValues.put(Expense.CATEGORYID, mCategory.getCategoryId());
        int id = 0;
        int rows = 0;
        String[] selectionArgs = new String[]{String.valueOf(getExpenseId())};
        try {
            rows = getContentResolver().update(Expense.getUri(), contentValues, Expense.EXPENSEID + "=?", selectionArgs);
        } catch (Exception e) {
            throw new Error(e.getMessage());
        }

        contentValues.clear();


        String dateString = DateUtil.formatDateString(dateFragment.getDate());
        contentValues.put(DateGroup.EXPENSEID, id);
        contentValues.put(DateGroup.DATESTRING, dateString);
        try {
            getContentResolver().insert(DateGroup.getUri(), contentValues);
        } catch (Exception e) {
            Log.d(TAG, "onSave: " + e.getMessage());
        }
        return rows;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense_fragment_activity);
        dateFragment = new DateFragment();
        categoryFragment = new CategoryFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.date_view, dateFragment)
                .replace(R.id.categoryTag, categoryFragment)
                .commit();
        initComponents();
        ButterKnife.bind(this);
        initializeToolbar();
        initializeModel();
        initDescription(savedInstanceState);

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);

        }

        if (getIntent().getExtras() != null) {
            restoreInstanceState(getIntent().getExtras());
        }
    }


    public void initializeToolbar() {
        toolbarText = findViewById(R.id.toolbar_title);
        setDisplayHomeAsUpEnabled(true);
    }

    public void initializeModel() {

        if (getIntent() != null && getIntent().getAction() != null) {
            switch (getIntent().getAction()) {

                case INTENT_RECURRING:
                    RecurringTransactionFragment fragment = new RecurringTransactionFragment();
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction().remove(dateFragment).add(R.id.date_view, fragment).commit();
                    break;
                case Intent.ACTION_INSERT:
                    mExpense = new Expense();
                    toolbarText.setText(account.getAccountName());
                    break;
                case Intent.ACTION_EDIT:

                    toolbarText.setText(R.string.edit);
                    Expense item = new Expense();
                    int expenseId = getExpenseId();
                    Uri singleUri = ContentUris.withAppendedId(Expense.getUri(), expenseId);
                    Cursor data = getContentResolver().query(singleUri, item.getAllColumns(), Expense.EXPENSEID + "=?", new String[]{String.valueOf(expenseId)}, null);
                    if (data != null) {
                        data.moveToFirst();
                        if (!data.isAfterLast()) {
                            item.setId(data.getInt(data.getColumnIndex(Expense.EXPENSEID)));
                            item.setCategoryId(data.getInt(data.getColumnIndex(Expense.CATEGORYID)));
                            item.setInputAmount(Float.parseFloat(String.valueOf(data.getDouble(data.getColumnIndex(Expense.AMOUNT)))));
                            item.setDescription(data.getString(data.getColumnIndex(Expense.DESCRIPTION)));
                            item.setDate(DateUtil.convertToDate(data.getString(data.getColumnIndex(Expense.DATE))));
                            item.setImage(data.getString(data.getColumnIndex(Expense.IMAGE)));
                        }
                        data.close();

                        setValuesFromModel(item);
                    }


                    break;
            }
        }
    }

    private void resizeFragment(Fragment f, int newWidth, int newHeight) {
        if (f != null) {
            View view = f.getView();
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(newWidth, newHeight);
            view.setLayoutParams(p);
            view.requestLayout();


        }
    }

    private void setValuesFromModel(Expense item) {
        txtInputAmount.setText(moneyService.convertToTwoDecimalPlaceString(item.getInputAmount()));
        Bundle args = new Bundle();
        args.putString(Expense.DATE, DateUtil.formatDateString(item.getDate()));
        dateFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.date_view, dateFragment).commit();
        mCategory.setCategoryId(item.getCategoryId());


        //initialize the common class with the initial values
        mDescription = item.getDescription();
        mImage = item.getImage();
        updateDescriptionLabelIcon(mImage);
        txtDescription.setText(mDescription);

    }


    private int getExpenseId() {
        return getIntent().getIntExtra(KEY_EXPENSE_ID, Constants.NOT_SET);
    }

    private void initBottomSheet() {

        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator);
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottomSheet);

        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initComponents() {
        //create a new instance
        mCategory = new Category();
        mExpense = new Expense();
        mDateGroup = new DateGroup();
        categoryList = new ArrayList<>();

        initCalculatorControls();


        mCommon = new EditTransactionCommonFunctions(this);

        initBottomSheet();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode);
        switch (requestCode) {
            case (RequestCodes.DESCRIPTION):
                if (resultCode == Activity.RESULT_OK) {
                    mDescription = data.getStringExtra(Expense.DESCRIPTION);
                    txtDescription.setText(mDescription);
                    mImage = data.getStringExtra(Expense.IMAGE);
                    updateDescriptionLabelIcon(mImage);

                }
                break;


        }
    }

    private void updateDescriptionLabelIcon(String icon) {
        if (icon != null) {
            if (!TextUtils.isEmpty(icon)) {
                imageIcon.setVisibility(View.VISIBLE);
                imageIcon.setImageBitmap(BitmapUtil.convertToBitmap(icon));
            }
        } else {
            imageIcon.setVisibility(View.GONE);
            imageIcon.setImageDrawable(null);
        }
    }

    //region add expense
    private boolean validateData() {
        /*        if (!mCommon.validateData()) return false;*/

        Core core = new Core(this);

        // Due Date is required
        if (txtInputAmount.getText().toString().equals("0.00")) {
            core.alert(R.string.input_amount);
            return true;
        }

        if (mCategory.getCategoryId() == 0) {
            core.alert(R.string.select_category);
            return true;
        }

        return false;
    }

    @Override
    public MenuInflater getMenuInflater() {
        return new MenuInflater(this);
    }

    public boolean onSave() {
        if (validateData()) return false;
        ContentValues contentValues = new ContentValues();
        contentValues.put(Expense.AMOUNT, txtInputAmount.getText().toString());
        contentValues.put(Expense.DESCRIPTION, mDescription);
        contentValues.put(Expense.IMAGE, mImage);
        contentValues.put(Expense.CATEGORYID, mCategory.getCategoryId());
        contentValues.put(Expense.ACCOUNTID, Account.getId());

        if (getIntent().getAction().equals(INTENT_RECURRING)) {
            contentValues.put(RecurringExpense.RECURRINGMODE, mCategory.getCategoryId());
            contentValues.put(Expense.ACCOUNTID, Account.getId());
        }
        int id = 0;
        try {
            Uri returnUri = getContentResolver().insert(Expense.getUri(), contentValues);
            assert returnUri != null;
            id = Integer.parseInt(returnUri.getLastPathSegment());
            if (id != 0) {
                finish();
            }
        } catch (Exception e) {
            Log.d(TAG, "onSave Expense: " + e.getMessage());
        }

        contentValues.clear();
        String dateString = DateUtil.formatDateString(dateFragment.getDate());
        contentValues.put(DateGroup.EXPENSEID, id);
        contentValues.put(DateGroup.DATESTRING, dateString);
        contentValues.put(DateGroup.DATEID, mDateId);
        //insert
        try {
            getContentResolver().insert(DateGroup.getUri(), contentValues);
        } catch (Exception e) {
            Log.d(TAG, "onSave DateGroup: " + e.getMessage());
        }


        /*ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        operations.add(ContentProviderOperation
                .newInsert(Expense.getUri())
                .withValues(contentValues)
                .build());

        int index = operations.size() - 1;

        operations.add(ContentProviderOperation
                .newInsert(DateGroup.getUri())
                .withValue(DateGroup.DATESTRING, dateString)
                .withValueBackReference(DateGroup.EXPENSEID, index)
                .build());
        try {
            this.getContentResolver().applyBatch(ExpenseContract.AUTHORITY, operations);
        } catch (RemoteException e) {
            e.getMessage();
        } catch (OperationApplicationException e) {
            e.getMessage();
        }*/
      /*  Uri uri = getContentResolver().insert(mExpense.getUri(), contentValues);
        if (uri != null)
            return true;
        return false;*/
        return true;
    }

    //endregion
    public void initDescription(Bundle savedInstanceState) {
        RelativeLayout lDescription = findViewById(R.id.llDescription);
        if (getIntent().getAction().equals(Intent.ACTION_INSERT)) {
            mDescription = "";
            mImage = "";
        }
        lDescription.setOnClickListener(v -> {
            Intent intent = new Intent(ExpenseEditActivity.this, DescriptionActivity.class);
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Expense.DESCRIPTION, mDescription);
            intent.putExtra(Expense.IMAGE, mImage);
            startActivityForResult(intent, RequestCodes.DESCRIPTION);

        });
    }

    //region data persistance
    private Expense retrieveAllUserInputValues() {
        Expense expense = new Expense();
        expense.setId(getExpenseId());
        expense.setInputAmount(Float.parseFloat(txtInputAmount.getText().toString()));
        expense.setDescription(mDescription);
        expense.setImage(mImage);
        expense.setCategoryId(mCategory.getCategoryId());
        expense.setCategoryName(mCategory.getCategoryName());
        expense.setCategoryIcon(mCategory.getCategoryIcon());
        expense.setDate(dateFragment.getDate());
        return expense;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXPENSEMODEL, Parcels.wrap(retrieveAllUserInputValues()));
    }

    public void restoreInstanceState(Bundle args) {
        Expense expense = Parcels.unwrap(args.getParcelable(EXPENSEMODEL));
        if (expense != null) {
            txtInputAmount.setText(String.valueOf(expense.getInputAmount()));
            String date;
            try {
                date = DateUtil.formatDateString(expense.getDate());
                if (!date.equals(DateUtil.formatDateString(new Date()))) {
                    dateFragment.setDate(date);
                }
                mCategory.setCategoryId(expense.getCategoryId());
                mCategory.setCategoryIcon(expense.getCategoryIcon());
                mCategory.setCategoryName(expense.getCategoryName());


            } catch (Exception e) {
                e.printStackTrace();
            }

            updateDescriptionLabelIcon(expense.getImage());
            mDescription = expense.getDescription();
        }
    }

    //endregion

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button_default_red
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region Calculator
    public void initCalculatorControls() {
        Log.d(TAG, "initCalculatorControls: ");

        // Numbers and Operators.
        View.OnClickListener numberClickListener = (v) -> {
            txtInputAmount.setText(moneyService.convertToTwoDecimalPlaceString(Double.parseDouble(edtInputAmount.getText() + ((Button) v).getText().toString())));
            edtInputAmount.setText(edtInputAmount.getText().append(((Button) v).getText().toString()));

        };

        View.OnClickListener decimalClickListener = (v) -> {
            if (!txtInputAmount.getText().equals("0.00")) {
                char value = txtInputAmount.getText().charAt(0);
                String display = String.valueOf(value) + ".";
                txtInputAmount.setText(moneyService.convertToTwoDecimalPlaceString(display));
                edtInputAmount.setText(display);
            } else {
                txtInputAmount.setText(R.string.no_amount);
                edtInputAmount.setText("0.");
            }

        };

        for (int id : idButtonKeyNum) {
            Button button = findViewById(id);
            button.setOnClickListener(numberClickListener);
            if (id == R.id.buttonKeyNumDecimal) {
                button.setOnClickListener(decimalClickListener);

            }
        }


        Button buttonKeyAdd = findViewById(R.id.buttonKeyAdd);
        buttonKeyAdd.setOnClickListener(v -> {
            try {
                mValue1 = Float.parseFloat(edtInputAmount.getText().toString());
                mAddition = true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            edtInputAmount.setText("");

        });
        Button buttonKeySubtract = findViewById(R.id.buttonKeyLess);
        buttonKeySubtract.setOnClickListener(v -> {
            try {
                mValue1 = Float.parseFloat(edtInputAmount.getText() + "");
                mSubtraction = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            edtInputAmount.setText("");
        });
        Button buttonKeyDiv = findViewById(R.id.buttonKeyDiv);
        buttonKeyDiv.setOnClickListener(v -> {
            try {
                mValue1 = Float.parseFloat(edtInputAmount.getText() + "");
                mDivision = true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            edtInputAmount.setText("");
        });
        Button buttonKeyMultiple = findViewById(R.id.buttonKeyMultiplication);
        buttonKeyMultiple.setOnClickListener(v -> {
            try {
                mValue1 = Float.parseFloat(edtInputAmount.getText() + "");
                mMultiple = true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            edtInputAmount.setText("");
        });
        Button buttonKeyEqual = findViewById(R.id.buttonKeyEqual);
        buttonKeyEqual.setOnClickListener(v -> {
            try {
                mValue2 = Float.parseFloat(edtInputAmount.getText() + "");
            } catch (Exception ex) {
                mValue2 = 0;
            }
            float result = 0;
            if (mAddition) {
                result = mValue1 + mValue2;
                mAddition = false;
            }


            if (mSubtraction) {
                result = mValue1 - mValue2;
                mSubtraction = false;
            }

            if (mMultiple) {
                result = mValue1 * mValue2;
                mMultiple = false;
            }

            if (mDivision) {
                result = mValue1 / mValue2;
                mDivision = false;
            }

            if (result != 0) {
                edtInputAmount.setText("");
                txtInputAmount.setText(moneyService.convertToTwoDecimalPlaceString(result));
            }

            if (result < 0) {
                clearValues();
            }
        });
        // Clear button_default_red. 'C'
        Button clearButton = findViewById(R.id.buttonKeyClear);
        clearButton.setOnClickListener(view1 -> {
            clearValues();
        });


        // Amounts
        //mDefaultColor = txtInputAmount.getCurrentTextColor();
    }

    public void clearValues() {
        edtInputAmount.setText("");
        txtInputAmount.setText("0.00");
    }

    @OnClick(R.id.deleteButton)
    public void onDeleteClicked() {
        String value;
        if (String.valueOf(edtInputAmount.getText()).equals("")) {
            value = String.valueOf(txtInputAmount.getText());
        } else {
            value = String.valueOf(edtInputAmount.getText());
        }
        String currentNumber = deleteLastCharacterFrom(value);

        edtInputAmount.setText(currentNumber);
        txtInputAmount.setText(moneyService.convertToTwoDecimalPlaceString(currentNumber));
    }

    private String deleteLastCharacterFrom(String number) {
        // check length
        if (number.length() <= 0) return number;

        // first cut-off the last digit
        number = number.substring(0, number.length() - 1);

        // Should we check if the next character is the decimal separator. (?)

        // Handle deleting the last number - set the remaining value to 0.
        if (TextUtils.isEmpty(number)) {
            number = "0";
        }

        return number;
    }


    //endregion

}
