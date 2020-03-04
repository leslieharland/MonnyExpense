package com.example.leslie.monnyfree;

import android.net.Uri;

public class ExpenseContract {
    public static String AUTHORITY = "com.example.leslie.monnyfree.provider";
    public static final Uri EXPENSE_PATH = Uri.parse("content://" + AUTHORITY).buildUpon().appendPath("expenses").build();
    public static final Uri DATEGROUP_PATH = Uri.parse("content://" + AUTHORITY).buildUpon().appendPath("dategroup").build();
    public static final Uri CATEGORY_PATH = Uri.parse("content://" + AUTHORITY).buildUpon().appendPath("category").build();
    public static final Uri ACCOUNT_PATH = Uri.parse("content://" + AUTHORITY).buildUpon().appendPath("account").build();

}
