package com.example.leslie.monnyfree.model;

import android.net.Uri;

import com.example.leslie.monnyfree.ExpenseContract;

/**
 * Created by Leslie on 3/24/2018.
 */

public class Category  {
    public static final String CATEGORYID = "categoryId";
    public static final String CATEGORYNAME = "categoryName";
    public static final String CATEGORYICON = "categoryIcon";

    public static final String TABLE = "category";

    public int categoryId;
    public String categoryName;
    //icon from getAssets
    public String categoryIcon;


    public Category(){}

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int value) {
        categoryId = value;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String value) {
        categoryName = value;
    }


    public String[] getAllColumns() {
        return new String[]{ CATEGORYID, CATEGORYNAME, CATEGORYICON};
    }

    public static Uri getUri(){
        return ExpenseContract.CATEGORY_PATH;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String value) {
        categoryIcon = value;
    }
}
