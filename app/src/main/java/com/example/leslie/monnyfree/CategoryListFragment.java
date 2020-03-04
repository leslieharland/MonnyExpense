package com.example.leslie.monnyfree;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.leslie.monnyfree.adapter.CategoryListAdapter;
import com.example.leslie.monnyfree.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryListFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>{

    private String title;
    static Category category;
    List<Category> categoryList;
    protected ListView mListView;

    public static CategoryListFragment createFragment(String title) {
        CategoryListFragment customFragment = new CategoryListFragment();
        customFragment.title = title;
        return customFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        categoryList = new ArrayList<>();
        getLoaderManager().initLoader(0, null, this);
        return inflater.inflate(R.layout.recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mListView.findViewById(android.R.id.list);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Category.getUri(), category.getAllColumns(), null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        categoryList.clear();
        mListView.setAdapter(getAdapter(data));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        categoryList.clear();
    }

    public CategoryListAdapter getAdapter(Cursor data) {
        if (data == null) return null;
        if (data.getPosition() > 0) {
            data.moveToPosition(-1);
        }


        while (data.moveToNext()) {
            Category category = new Category();
            category.setCategoryId(data.getInt(data.getColumnIndex(Category.CATEGORYID)));
            category.setCategoryName(data.getString(data.getColumnIndex(Category.CATEGORYNAME)));
            category.setCategoryIcon(data.getString(data.getColumnIndex(Category.CATEGORYICON)));
            categoryList.add(category);
        }
        CategoryListAdapter adapter = new CategoryListAdapter(getActivity(), R.layout.simple_list_item, categoryList);
        return adapter;
    }
}