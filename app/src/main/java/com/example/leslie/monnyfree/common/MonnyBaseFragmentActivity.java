package com.example.leslie.monnyfree.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.leslie.monnyfree.MainActivity;
import com.example.leslie.monnyfree.R;


/**
 * Created by Leslie on 3/23/2018.
 */

public abstract class MonnyBaseFragmentActivity extends AppCompatActivity {

    private boolean mDisplayHomeAsUpEnabled = false;
    private boolean mStartedTyping;

    public double mAmount;
    TextView textViewInputAmount;
    public static final String TAG = "Calculator";

    private Integer mDefaultColor;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) setSupportActionBar(mToolbar);
    }

    protected void setTheme(){
        this.setTheme(R.style.ThemeDefault);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDisplayHomeAsUpEnabled) {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                    return true;
                }

        }
        return false;
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    public void setDisplayHomeAsUpEnabled(boolean mDisplayHomeAsUpEnabled) {
        this.mDisplayHomeAsUpEnabled = mDisplayHomeAsUpEnabled;
        getSupportActionBar().setDisplayHomeAsUpEnabled(mDisplayHomeAsUpEnabled);
        getSupportActionBar().setDisplayShowHomeEnabled(mDisplayHomeAsUpEnabled);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
