package com.example.leslie.monnyfree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.leslie.monnyfree.common.MonnyBaseFragmentActivity;
import com.example.leslie.monnyfree.core.RequestCodes;

import static com.example.leslie.monnyfree.IntentActions.INTENT_RECURRING;

/**
 * Created by Leslie on 4/15/2018.
 */

public class RecurringTransactionsActivity extends MonnyBaseFragmentActivity{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuItem itemSearch = menu.add(Menu.NONE, R.id.menu_add, 1000, R.string.add);
        itemSearch.setIcon(R.drawable.ic_add);
        itemSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button_default_red
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.menu_add:
                Intent intent = new Intent(this, ExpenseEditActivity.class);
                intent.setAction(INTENT_RECURRING);
                startActivityForResult(intent, RequestCodes.RECURRING);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
