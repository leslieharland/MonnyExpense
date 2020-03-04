package com.example.leslie.monnyfree;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.leslie.monnyfree.adapter.AccountAdapter;
import com.example.leslie.monnyfree.common.MonnyBaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends MonnyBaseFragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = AccountActivity.class.getSimpleName();
    Account mAccount = new Account();
    private List<Account> accountList;
    protected RecyclerView recyclerView;
    private AccountAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_activity);

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        accountList = new ArrayList<>();
        mAdapter = new AccountAdapter(accountList, this);
        recyclerView.setAdapter(mAdapter);

        Account a = new Account();
        a.setId(1);
        a.setImage("pyramid.jpg");
        a.setCurrencyId(1);
        a.setCurrencyCode("GBP");
        a.setNumberOfRecords(0);
        a.setName("asfa");
        a.setDefault(true);
        accountList.add(a);
        mAdapter.notifyDataSetChanged();
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> result = null;

        try {
            result = new CursorLoader(this,
                    Account.getUri(), mAccount.getAllColumns(), null, null, null);
        } catch (Exception ex) {
            Log.d(TAG, "onCreateLoader: " + ex.getMessage());
        }

        return result;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        if (accountList != null)
            accountList.clear();
        accountList = new ArrayList<>();
        try {
            c.moveToFirst();
            while (c.moveToNext()) {
                Account account = new Account();
                account.setId(c.getInt(c.getColumnIndex(Account.ACCOUNTID)));
                account.setName(c.getString(c.getColumnIndex(Account.ACCOUNTNAME)));
                account.setCurrencyId(c.getInt(c.getColumnIndex(Account.CURRENCYID)));
                account.setCurrencyCode(c.getString(c.getColumnIndex(Account.CURRENCYCODE)));
                account.setCurrencyId(c.getInt(c.getColumnIndex(Account.CURRENCYID)));
                account.setNumberOfRecords(c.getInt(c.getColumnIndex(Account.NUMBEROFRECORDS)));
                accountList.add(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        accountList.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuItem itemAdd = menu.add(Menu.NONE, R.id.menu_add, 1000, R.string.add);
        itemAdd.setIcon(R.drawable.ic_add);
        itemAdd.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                startActivity(new Intent(AccountActivity.this, EditAccountActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
