package com.example.leslie.monnyfree.model;

import android.net.Uri;

import com.example.leslie.monnyfree.ExpenseContract;
import com.example.leslie.monnyfree.database.QueryExpense;
import com.example.leslie.monnyfree.utils.DateUtil;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Date;

/**
 * Created by Leslie on 3/25/2018.
 */

@Parcel
public class Expense {
    public static final String DESCRIPTION = "description";
    public static final String IMAGE = "image";
    public static final String AMOUNT = "amount";
    public static final String DATE = "date";
    public static final String CATEGORYID = "categoryId";
    public static final String EXPENSEID = "expenseId";
    public static final String TABLE = "expense";
    public static final String CATEGORYNAME = "categoryName";
    public static final String ACCOUNTID = "accountId";
    public static final String CATEGORYICON = "categoryIcon";



    Float inputAmount;
    Date date;
    String description;
    String image;
    int categoryId;
    String categoryIcon;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String[] getAllColumns(){
        return new String[]{EXPENSEID, CATEGORYID, CATEGORYNAME, CATEGORYICON, DESCRIPTION, AMOUNT, IMAGE};
    }
    String categoryName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;



    @ParcelConstructor
    public Expense() {


    }


    public Expense translateToExpense(QueryExpense q) {
        Expense expense = new Expense();
        expense.setCategoryId(q.getCategoryId());
        try {
            expense.setDate(DateUtil.convertToDate(String.valueOf(q.getDateName())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        expense.setDescription(q.getDescription());
        expense.setInputAmount(Float.parseFloat(String.valueOf(q.getAmount())));
        expense.setImage(q.getImage());
        expense.setId(q.getExpenseId());
        expense.setCategoryName(q.getCategoryName().toString());
        expense.setCategoryIcon(q.getCategoryIcon());
        return expense;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryIcon(int categoryId) {
        return "";
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }



    public double getInputAmount() {
        return inputAmount;
    }

    public void setInputAmount(Float inputAmount) {
        this.inputAmount = inputAmount;
    }

    public static Uri getUri() {
        return ExpenseContract.EXPENSE_PATH;
    }
}
