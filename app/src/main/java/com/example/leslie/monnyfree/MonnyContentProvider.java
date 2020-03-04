/*
 * Copyright (C) 2012-2018 The Android Money Manager Ex Project Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.example.leslie.monnyfree;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.leslie.monnyfree.database.DbHelper;
import com.example.leslie.monnyfree.model.Category;
import com.example.leslie.monnyfree.model.DateGroup;
import com.example.leslie.monnyfree.model.Expense;
import com.example.leslie.monnyfree.utils.DatabaseUtil;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;

import rx.schedulers.Schedulers;

//import net.sqlcipher.database.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteQueryBuilder;

/**
 * MonnyContentProvider is the extension of the base class of Android
 * ContentProvider. Its purpose is to implement the read access and modify the
 * application data
 */
public class MonnyContentProvider
        extends ContentProvider {

    private static final String TAG = MonnyContentProvider.class.getSimpleName();
    // object definition for the call to check the content
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    // object map for the definition of the objects referenced in the URI
    private static SparseArrayCompat<Object> mapContent = new SparseArrayCompat<>();
    BriteDatabase db;


    private static final int EXPENSE = 100;
    private static final int EXPENSE_ID = 101;

    private static final int DATEGROUP = 200;
    private static final int DATEGROUP_ID = 201;


    private static final int CATEGORY = 300;
    private static final int CATEGORY_ID = 301;


    private static final int ACCOUNT = 400;

    private static final int SQL = 1000;


    public MonnyContentProvider() {
        super();
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        db = sqlBrite.wrapDatabaseHelper(openHelper, Schedulers.io());
    }


    private DbHelper openHelper;


    @Override
    public boolean onCreate() {
        openHelper = new DbHelper(getContext());
        openHelper.getReadableDatabase();

        Context context = getContext();
        if (context == null) return false;


        // Cycle all data sets for the composition of UriMatcher

        return false;
    }

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(ExpenseContract.AUTHORITY, ExpenseContract.EXPENSE_PATH.getLastPathSegment(), EXPENSE);
        matcher.addURI(ExpenseContract.AUTHORITY, ExpenseContract.EXPENSE_PATH.getLastPathSegment().concat("/#"), EXPENSE_ID);

        matcher.addURI(ExpenseContract.AUTHORITY, ExpenseContract.CATEGORY_PATH.getLastPathSegment(), CATEGORY);
        matcher.addURI(ExpenseContract.AUTHORITY, ExpenseContract.CATEGORY_PATH.getLastPathSegment().concat("/#"), CATEGORY_ID);

        matcher.addURI(ExpenseContract.AUTHORITY, ExpenseContract.DATEGROUP_PATH.getLastPathSegment(), DATEGROUP);
        matcher.addURI(ExpenseContract.AUTHORITY, ExpenseContract.DATEGROUP_PATH.getLastPathSegment().concat("/#"), DATEGROUP_ID);

        matcher.addURI(ExpenseContract.AUTHORITY, ExpenseContract.ACCOUNT_PATH.getLastPathSegment(), ACCOUNT);


        matcher.addURI(ExpenseContract.AUTHORITY, "sql", 1000);

        return matcher;

    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try {
            return query_internal(uri, projection, selection, selectionArgs, sortOrder);


        } catch (Exception e) {
            Log.e(e.toString(), "content provider.query" + uri);
        }
        return null;


    }

    public String getSourceFromUri(Uri uri) {
        Log.d(TAG, "Uri: " + uri);
        Log.d(TAG, "getSourceFromUri: " + sUriMatcher.match(uri));
        switch (sUriMatcher.match(uri)) {
            case EXPENSE:
                return Expense.TABLE;
            case CATEGORY:
                return Category.TABLE;
            case DATEGROUP:
                return DateGroup.TABLE;
            case ACCOUNT:
                return Account.TABLE;
            default:
                return null;

        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri returnUri = null;
        values.remove("_id");
        long id = -1;
        String source = getSourceFromUri(uri);
        switch (sUriMatcher.match(uri)) {
            case EXPENSE:
                id = openHelper.getWritableDatabase()
                        .insertOrThrow(source, null, values);
                returnUri = ContentUris.withAppendedId(ExpenseContract.EXPENSE_PATH, id);

                break;
            case CATEGORY:
                id = openHelper.getWritableDatabase()
                        .insertOrThrow(source, null, values);
                break;
            case SQL:
                source = "sql";
                id = openHelper.getWritableDatabase()
                        .insertOrThrow(source, null, values);
                break;
            case DATEGROUP:
                String dateId = String.valueOf(values.get(DateGroup.DATEID));
                if (!dateId.equals("0")) {
                    openHelper.getWritableDatabase()
                            .execSQL("INSERT INTO dateGroup(dateId, dateString, expenseId) VALUES(?, ?, ?)",
                                    new String[]{dateId, String.valueOf(values.get(DateGroup.DATESTRING)), String.valueOf(values.get(DateGroup.EXPENSEID))});
                }else{
                    openHelper.getWritableDatabase()
                            .execSQL("INSERT INTO dateGroup(dateId, dateString, expenseId) VALUES((SELECT IFNULL(MAX(dateId), 0) + 1 FROM dateGroup), ?, ?)", new String[]{ String.valueOf(values.get(DateGroup.DATESTRING)),
                                    String.valueOf(values.get(DateGroup.EXPENSEID))});
                }
                break;
            case ACCOUNT:
                id = openHelper.getWritableDatabase()
                        .insertOrThrow(source, null, values);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported uri: " + uri);
        }


        return returnUri;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
        int rowsUpdate = 0;
        String source = getSourceFromUri(uri);
        if (source != null) {
            rowsUpdate = openHelper.getWritableDatabase().update(getSourceFromUri(uri), values, whereClause, whereArgs);
            if (rowsUpdate > 0) {
                notifyChange(uri);
            }
        }
        // return rows modified
        return rowsUpdate;

    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int rowsDelete = 0;
        String source = getSourceFromUri(uri);
        if (source != null) {
            rowsDelete = openHelper.getWritableDatabase()
                    .delete(source, selection, selectionArgs);
        }


        if (rowsDelete > 0) notifyChange(uri);

        return rowsDelete;
    }

    /**
     * Prepare statement SQL from data set object
     *
     * @param query      SQL query
     * @param projection ?
     * @param selection  ?
     * @param sortOrder  field name for sort order
     * @return statement
     */
    public String prepareQuery(String query, String[] projection, String selection, String sortOrder) {
        String selectList, from, where = "", sort = "";

        // todo: use builder?
//        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
//        SQLiteQueryBuilder.buildQueryString(false, )

        // compose select list
        if (projection == null) {
            selectList = "SELECT *";
        } else {
            selectList = "SELECT ";

            for (int i = 0; i < projection.length; i++) {
                if (i > 0) {
                    selectList += ", ";
                }
                selectList += projection[i];
            }
        }
        // FROM
        from = "FROM (" + query + ") T";
        // WHERE
        if (!TextUtils.isEmpty(selection)) {
//            if (!selection.contains("WHERE")) {
            if (!selection.startsWith("WHERE")) {
                where += "WHERE";
            }
            where += " " + selection;
        }
        // compose sort
        if (!TextUtils.isEmpty(sortOrder)) {
            if (!sortOrder.contains("ORDER BY")) {
                sort += "ORDER BY ";
            }
            sort += " " + sortOrder;
        }
        // compose statement to return
        query = selectList + " " + from;
        // check where or sort not empty
        if (!TextUtils.isEmpty(where)) {
            query += " " + where;
        }
        if (!TextUtils.isEmpty(sort)) {
            query += " " + sort;
        }

        return query;
    }


    private Cursor query_internal(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase database = openHelper.getWritableDatabase();
        Log.d(TAG, "query_internal: " + uri);
        String query;
        int match = sUriMatcher.match(uri);
        try {
            switch (match) {
                case EXPENSE:
                    query = prepareQuery(DatabaseUtil.getRawAsString(getContext(), R.raw.query_expenses), projection, selection, sortOrder);
                    cursor = database.rawQuery(query, selectionArgs);
                    break;

                case EXPENSE_ID:
                    Expense expense = new Expense();
                    String columns = "";
                    for (String column : expense.getAllColumns()) {
                        columns = columns.concat(column + " , ");
                    }
                    query = "SELECT expense.expenseId , category.categoryId , category.categoryIcon, category.categoryName, description , amount , image , dateGroup.dateString AS date FROM expense INNER JOIN category ON category.categoryId = expense.categoryId LEFT OUTER JOIN dateGroup ON dateGroup.expenseId = expense.expenseId where expense.expenseId = ?";
                    cursor = database.rawQuery(query, selectionArgs);

                    break;
                case CATEGORY:
                    cursor = database.rawQuery("Select * from " + Category.TABLE, selectionArgs);
                    break;

                case CATEGORY_ID:
                    query = "SELECT categoryName, categoryIcon  FROM category where categoryId = ?";
                    cursor = database.rawQuery(query, selectionArgs);
                    break;

                case DATEGROUP:
                    cursor = database.rawQuery("Select dateId from " + DateGroup.TABLE + " where " + DateGroup.DATESTRING + "=?", selectionArgs);
                    break;
                case ACCOUNT:
                    query = prepareQuery(DatabaseUtil.getRawAsString(getContext(), R.raw.query_accounts), projection, selection, sortOrder);
                    cursor = database.rawQuery(query, selectionArgs);
                    break;
                default:
                    Log.d(TAG, "query_internal: URI not handled");
                    query = "";
            }


            // notify listeners waiting for the data is ready
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            if (!cursor.isClosed()) {
                Log.d(String.valueOf(match), "Rows returned: " + cursor.getCount());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    public void resetDatabase() {
        if (openHelper != null) {
            openHelper.close();
        }

        openHelper = null;
        //initializeDependencies();
    }

    //region Log Queries unused
/*    private void initializeDependencies() {
        if (openHelper != null) return;

        MmexApplication.getApp().iocComponent.inject(this);
    }*/


    //endregion
    private void notifyChange(Uri uri) {
        if (getContext() == null) return;

        // notify update. todo Do this also after changes via sqlite.
        getContext().getContentResolver().notifyChange(uri, null);
        // notify the sync that database has changed.
        // new SyncManager(getContext()).dataChanged();
    }

    /**
     * Performs the work provided in a single transaction
     */
    @Override
    public ContentProviderResult[] applyBatch(
            ArrayList<ContentProviderOperation> operations) {
        ContentProviderResult[] result = new ContentProviderResult[operations
                .size()];
        int i = 0;
        // Opens the database object in "write" mode.
        SQLiteDatabase db = openHelper.getWritableDatabase();
        // Begin a transaction
        db.beginTransaction();
        try {
            for (ContentProviderOperation operation : operations) {
                // Chain the result for back references
                result[i++] = operation.apply(this, result, i);
            }

            db.setTransactionSuccessful();
        } catch (OperationApplicationException e) {
            Log.d(TAG, "batch failed: " + e.getMessage());
        } finally {
            db.endTransaction();
        }

        return result;
    }
}
