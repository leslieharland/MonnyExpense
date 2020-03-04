package com.example.leslie.monnyfree;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.leslie.monnyfree.adapter.CategoryExpandableListAdapter;
import com.example.leslie.monnyfree.common.MonthlyBudgetDialog;
import com.example.leslie.monnyfree.database.QueryExpense;
import com.example.leslie.monnyfree.utils.DateUtil;
import com.example.leslie.monnyfree.utils.MoneyService;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.leslie.monnyfree.Constants.EXPENSEID;


/**
 * Created by Leslie on 3/11/2018.
 */

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = HomeFragment.class.getSimpleName();
    ProgressDialog dialog;
    SeekBar mSeekbar;
    @BindView(R.id.toggle_source)
    LinearLayout toggleSource;

    static MoneyService moneyService = new MoneyService();

    @OnClick(R.id.toggle_source)
    public void toggleOnClick() {

    }

    @BindView(R.id.textViewRemainingBudget)
    TextView textViewRemainingBudget;

    @BindView(R.id.progressDialogLinearLayout)
    LinearLayout progressDialogLinearLayout;

    @OnClick(R.id.progressDialogLinearLayout)
    public void progressOnClick() {
        MonthlyBudgetDialog dialog = MonthlyBudgetDialog.getInstance();
        dialog.setTargetFragment(HomeFragment.this, Constants.MONTHLY_BUDGET_DIALOG);
        dialog.show(getActivity().getSupportFragmentManager(), Constants.DIALOG);
    }

    private int mLayout;
    private static QueryExpense mQuery;
    FloatingActionButton mFloatingActionButton;
    CategoryExpandableListAdapter mAdapter;
    @BindView(R.id.expExpenses)
    ExpandableListView expandableListView;
    public static final int ID_LOADER_EXPENSE = 0;

    @BindView(R.id.fabAddExpenses)
    FloatingActionButton fabAddExpense;
    LinkedHashMap<String, List<QueryExpense>> result = new LinkedHashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        mSeekbar = v.findViewById(R.id.seekBar);
        mSeekbar.setEnabled(false);
        //mSeekbar.setOnClickListener();

        ButterKnife.bind(this, v);
        attachUI();


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLayout = R.layout.simple_expandable_list_item_selector;
        mQuery = new QueryExpense();
        getLoaderManager().initLoader(ID_LOADER_EXPENSE, null, this);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.MONTHLY_BUDGET_DIALOG) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getExtras().containsKey(Constants.INPUT_BUDGET_AMOUNT)) {
                    String inputBudgetAmount = data.getExtras().getString(Constants.INPUT_BUDGET_AMOUNT);
                    String budgetText = textViewRemainingBudget.getText().toString();
                    String[] result = budgetText.split("/");
                    String display = moneyService.convertToTwoDecimalPlaceString(inputBudgetAmount) + "/" + result[1];
                    textViewRemainingBudget.setText(display);
                }
            }
        }
    }

    private void attachUI() {
        fabAddExpense.setOnClickListener(v -> addNewExpenseRecord());
    }

    private void addNewExpenseRecord() {


        Intent intent = new Intent(getActivity(), ExpenseEditActivity.class);
        intent.setAction(Intent.ACTION_INSERT);
        getActivity().startActivity(intent);
        //coordinator.handleAddRecordTapped(this.getParentFragment().getActivity());
    }

    private void showChallengeDialog() {

    }

    public void setResultAndFinish() {
        this.setResult();
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(ID_LOADER_EXPENSE, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: " + id);
        switch (id) {
            case ID_LOADER_EXPENSE:
                result.clear();
                // update id selected
                if (mAdapter != null && mAdapter.getGroupCount() > 0) {
                    CategoryExpandableListAdapter adapter = mAdapter;
//                    mIdGroupChecked = adapter.getIdGroupChecked();
//                    mIdChildChecked = adapter.getIdChildChecked();
                }

                // load data
         /*       String whereClause = null;
                String selectionArgs[] = null;
                if (!TextUtils.isEmpty(mCurFilter)) {
                    whereClause = QueryCategorySubCategory.CATEGNAME + " LIKE ? OR "
                            + QueryCategorySubCategory.SUBCATEGNAME + " LIKE ?";
                    selectionArgs = new String[]{mCurFilter + "%", mCurFilter + "%"};
                }
                Select query = new Select(mQuery.getAllColumns())
                        .where(whereClause, selectionArgs)
                        .orderBy(QueryCategorySubCategory.CATEGNAME + ", " + QueryCategorySubCategory.SUBCATEGNAME);

                return new MmxCursorLoader(getActivity(), mQuery.getUri(), query);*/
                Log.d(TAG, "onCreateLoader: " + QueryExpense.getUri());
                return new CursorLoader(getActivity(), QueryExpense.getUri(), mQuery.getAllColumns(), null, null, null);
        }
        return null;
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {

            case ID_LOADER_EXPENSE:
                // clear the data storage collections.
                result.clear();
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case ID_LOADER_EXPENSE:
                expandableListView.setAdapter(getAdapter(data));
                expandableListView.setOnChildClickListener(myListItemClicked);
                expandableListView.expandGroup(0);
                Log.d(TAG, "onLoadFinished: setOnClickListener");
             /*   if (isResumed()) {
                    setListShown(true);

                    boolean noData = data == null || data.getCount() <= 0;
                    if (noData && mFloatingActionButton != null) {
                        mFloatingActionButton.show();
                    }
                } else {
                    setListShownNoAnimation(true);
                }

                for (int i = 0; i < mPositionToExpand.size(); i++) {
                    getExpandableListView().expandGroup(mPositionToExpand.get(i));
                }*/
        }
    }

    //our child listener
    private ExpandableListView.OnChildClickListener myListItemClicked = (parent, v, groupPosition, childPosition, id) -> {

        if (((QueryExpense)mAdapter.getChild(groupPosition, childPosition)).getExpenseId() != -1) {
            QueryExpense entity = (QueryExpense) getAdapter().getChild(groupPosition, childPosition);
            Log.d(TAG, ": " + entity.getAmount());
            startActivity(new Intent(getActivity(), ExpenseItemActivity.class)
                    .putExtra(EXPENSEID, entity.getExpenseId()));
        } else {
            //insert new expense
            startActivity(new Intent(getActivity(), ExpenseEditActivity.class).setAction(Intent.ACTION_INSERT));
        }
        return false;
    };
    // Other

    protected void setResult() {

    /*    String mAction = Intent.ACTION_EDIT;
        if (Intent.ACTION_PICK.equals(mAction)) {
            if (getExpandableListAdapter() == null) return;

            Intent result = null;

            if (getExpandableListAdapter() instanceof CategoryExpandableListAdapter) {
                CategoryExpandableListAdapter adapter = (CategoryExpandableListAdapter) getExpandableListAdapter();
                int categId = adapter.getIdGroupChecked();
                int subCategId = adapter.getIdChildChecked();

                if (categId == ExpandableListView.INVALID_POSITION) return;

                for (int groupIndex = 0; groupIndex < mCategories.size(); groupIndex++) {
                    if (mCategories.get(groupIndex).getId() == categId) {
                        // Get subcategory
                        if (subCategId != ExpandableListView.INVALID_POSITION) {
                            for (int child = 0; child < mSubCategories.get(mCategories.get(groupIndex)).size(); child++) {
                                if (mSubCategories.get(mCategories.get(groupIndex)).get(child).getSubCategId() == subCategId) {
                                    result = new Intent();
                                    result.putExtra(CategoryListActivity.INTENT_RESULT_CATEGID, categId);
                                    result.putExtra(CategoryListActivity.INTENT_RESULT_CATEGNAME,
                                            mSubCategories.get(mCategories.get(groupIndex)).get(child).getCategName().toString());
                                    result.putExtra(CategoryListActivity.INTENT_RESULT_SUBCATEGID, subCategId);
                                    result.putExtra(CategoryListActivity.INTENT_RESULT_SUBCATEGNAME,
                                            mSubCategories.get(mCategories.get(groupIndex)).get(child).getSubcategoryName().toString());
                                    break;
                                }
                            }
                        } else {
                            result = new Intent();
                            result.putExtra(CategoryListActivity.INTENT_RESULT_CATEGID, categId);
                            result.putExtra(CategoryListActivity.INTENT_RESULT_CATEGNAME,
                                    mCategories.get(groupIndex).getName());
                            result.putExtra(CategoryListActivity.INTENT_RESULT_SUBCATEGID, subCategId);
                            result.putExtra(CategoryListActivity.INTENT_RESULT_SUBCATEGNAME, "");
                        }
                    }
                }
            }

            if (result != null) {
                result.putExtra(CategoryListActivity.KEY_REQUEST_ID, this.requestId);

                getActivity().setResult(Activity.RESULT_OK, result);
            } else {
                getActivity().setResult(Activity.RESULT_CANCELED);
            }
        }*/
    }


    public void removePlaceHolderItem() {
        String dateToday = DateUtil.formatDateString(new Date());
        mAdapter.getList().get(dateToday).remove(0);
        mAdapter.notifyDataSetChanged();
        mAdapter.notifyDataSetInvalidated();
    }

    public CategoryExpandableListAdapter getAdapter() {
        return mAdapter;
    }

    public CategoryExpandableListAdapter getAdapter(Cursor data) {
        if (data == null) return null;


        //mPositionToExpand.clear();
        // create core and fixed string filter to highlight
//        Core core = new Core(getActivity().getApplicationContext());
//        String filter = mCurFilter != null ? mCurFilter.replace("%", "") : "";

        int key = -1;
        List<QueryExpense> listSubCategories = null;

        // reset cursor if getting back on the fragment.
        if (data.getPosition() > 0) {
            data.moveToPosition(-1);
        }

        String dateKey = DateUtil.formatDateString(new Date());
        List<QueryExpense> placeHolderList = new ArrayList<>();
        QueryExpense e = new QueryExpense();
        e.setExpenseId(-1);
        placeHolderList.add(e);
        result.put(dateKey, placeHolderList);
        while (data.moveToNext()) {
            List<QueryExpense> list = new ArrayList<>();
            if (key != data.getInt(data.getColumnIndex(QueryExpense.DATEID)) &&
                    data.getInt(data.getColumnIndex(QueryExpense.EXPENSEID)) != -1) {

                key = data.getInt(data.getColumnIndex(QueryExpense.CATEGID));
                String datetime = data.getString(data.getColumnIndex(QueryExpense.DATESTRING));


                if (result.containsKey(datetime)) {
                    if (datetime.equals(dateKey)){
                        result.get(dateKey).remove(e);
                    }
                    list = result.get(datetime);
                } else {
                    list.clear();
                    result.put(datetime, list);
                }
                // add list

            }
            listSubCategories = new ArrayList<>();
            QueryExpense expense = new QueryExpense();
            expense.setExpenseId(data.getInt(data.getColumnIndex(QueryExpense.EXPENSEID)));
            String categoryName = data.getString(data.getColumnIndex(QueryExpense.CATEGNAME));
            expense.setCategoryName(categoryName);
            expense.setCategoryId(data.getInt(data.getColumnIndex(QueryExpense.CATEGID)));
            expense.setCategoryIcon(data.getString(data.getColumnIndex(QueryExpense.CATEGICON)));
            expense.setDateId(data.getInt(data.getColumnIndex(QueryExpense.DATEID)));
            expense.setDateName(data.getString(data.getColumnIndex(QueryExpense.DATESTRING)));
            expense.setAmount(data.getDouble(data.getColumnIndex(QueryExpense.AMOUNT)));
            expense.setDescription(data.getString(data.getColumnIndex(QueryExpense.DESCRIPTION)));
            expense.setDisplayTitle(categoryName);
            // add to hashmap
            listSubCategories.add(expense);
            // check if expand group
               /* if (!TextUtils.isEmpty(filter)) {
                    String normalizedText = Normalizer.normalize(expense.getCategoryName(), Normalizer.Form.NFD)
                            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
                    if ((normalizedText.indexOf(filter) >= 0) && (!mPositionToExpand.contains(mCategories.size() - 1))) {
                        mPositionToExpand.add(mCategories.size() - 1);
                    }
                }*/

            list.add(expense);
        }

//        boolean showSelector = mAction.equals(Intent.ACTION_PICK);
        mAdapter = new CategoryExpandableListAdapter(getActivity(),
                mLayout, result, false);
        //adapter.setIdChildChecked(mIdGroupChecked, mIdChildChecked);

        return mAdapter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }
}
