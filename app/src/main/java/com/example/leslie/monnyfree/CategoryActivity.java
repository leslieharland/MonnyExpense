package com.example.leslie.monnyfree;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.leslie.monnyfree.common.CategoryDialog;
import com.example.leslie.monnyfree.common.MonnyBaseFragmentActivity;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by Leslie on 4/15/2018.
 */

public class CategoryActivity extends MonnyBaseFragmentActivity {
    private ListView mListView;
    CategoryDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_list);
        // Create a progress bar to display while the list loads
     /*   ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);*/
        initializeToolbar();
        SegmentedGroup segmented2 = findViewById(R.id.segmented2);
        segmented2.setTintColor(ContextCompat.getColor(this, R.color.monny_default));
        segmented2.setSelected(true);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new CategoryFragment()).commit();
        // Must add the progress bar to the root of the layout
  /*      ViewGroup root = findViewById(android.R.id.content);
        root.addView(progressBar);*/
    }

    public void initializeToolbar() {
        setDisplayHomeAsUpEnabled(true);
        TextView toolbarText = findViewById(R.id.toolbar_title);
        toolbarText.setText(R.string.category_list);
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
                dialog = CategoryDialog.getInstance();
                dialog.show(getSupportFragmentManager(), Constants.DIALOG_CATEGORY);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
