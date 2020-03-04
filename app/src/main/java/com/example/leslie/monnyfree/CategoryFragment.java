package com.example.leslie.monnyfree;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leslie.monnyfree.adapter.CategoryAdapter;
import com.example.leslie.monnyfree.adapter.MarginDecoration;
import com.example.leslie.monnyfree.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "CategoryFragment";
    private Category mCategory = new Category();
    List<Category> categoryList;
    RecyclerView recyclerView;
    private CategoryAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        int layout = R.layout.recycler_view;
        View view = inflater.inflate(layout, viewGroup, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler_view);
        if (getActivity() instanceof ExpenseEditActivity) {
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new MarginDecoration(getActivity()));
        } else {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        categoryList = new ArrayList<>();

        mAdapter = new CategoryAdapter(categoryList, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> result = null;

        try {
            result = new CursorLoader(getActivity(),
                    Category.getUri(), mCategory.getAllColumns(), null, null, null);
        } catch (Exception ex) {
            Log.d(TAG, "onCreateLoader: " + ex.getMessage());
        }

        return result;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        if (c == null) {
            categoryList = new ArrayList<>();
        }
        categoryList.clear();
        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Category category = new Category();
                category.setCategoryId(c.getInt(c.getColumnIndex(Category.CATEGORYID)));
                category.setCategoryName(c.getString(c.getColumnIndex(Category.CATEGORYNAME)));
                category.setCategoryIcon(c.getString(c.getColumnIndex(Category.CATEGORYICON)));
                categoryList.add(category);
                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onLoadFinished: " + categoryList.size());
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        categoryList.clear();
    }
}
