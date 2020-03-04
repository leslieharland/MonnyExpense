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
package com.example.leslie.monnyfree.database;

import android.database.Cursor;
import android.net.Uri;

import com.example.leslie.monnyfree.ExpenseContract;

public class QueryExpense {
    public static final String ID = "_id";
    public static final String DATEID = "dateId";
    public static final String DATESTRING = "dateString";
    public static final String EXPENSEID = "expenseId";
    public static final String AMOUNT = "amount";
    public static final String CATEGID = "categoryId";
    public static final String CATEGNAME = "categoryName";
    public static final String CATEGICON = "categoryIcon";
    public static final String DISPLAYTITLE = "displayTitle";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE = "image";

    private int dateId;
    private CharSequence dateName;
    private int expenseId;
    private double amount;
    private int categoryId;
    private CharSequence categoryName;
    private String displayTitle;
    private String description;
    private String categoryIcon;
    private String image;

    public QueryExpense() {

    }


    public String[] getAllColumns() {
        return new String[]{ID, CATEGID, CATEGNAME, CATEGICON, DATEID, DATESTRING, EXPENSEID, AMOUNT, DISPLAYTITLE, DESCRIPTION, IMAGE};
    }

    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

    public CharSequence getDateName() {
        return dateName;
    }

    public void setDateName(CharSequence dateName) {
        this.dateName = dateName;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public CharSequence getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(CharSequence categoryName) {
        this.categoryName = categoryName;
    }

    public void setValueFromCursor(Cursor c) {
        // controllo che non sia null il cursore
        if (c == null) {
            return;
        }
        // controllo che il numero di colonne siano le stesse
        if (!(c.getColumnCount() == this.getAllColumns().length)) {
            return;
        }
        // set dei valori
        this.setCategoryId(c.getInt(c.getColumnIndex(CATEGID)));
        this.setCategoryName(c.getString(c.getColumnIndex(CATEGNAME)));
        this.setDateId(c.getInt(c.getColumnIndex(DATEID)));
        this.setAmount(c.getDouble(c.getColumnIndex(AMOUNT)));
        this.setExpenseId(c.getInt(c.getColumnIndex(EXPENSEID)));
        this.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
    }

    public String getDisplayTitle() {
        return displayTitle;
    }

    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null) {
            int characterLimit = 20;
            if (description.length() > characterLimit) {
                description = description.substring(0, characterLimit) + "...";
            }
            this.description = description;
        }
    }

    public static Uri getUri(){
        return ExpenseContract.EXPENSE_PATH;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

