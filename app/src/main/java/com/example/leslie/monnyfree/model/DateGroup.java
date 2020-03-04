package com.example.leslie.monnyfree.model;

import android.net.Uri;

import com.example.leslie.monnyfree.ExpenseContract;
import com.example.leslie.monnyfree.MonnyContentProvider;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by Leslie on 3/18/2018.
 */

@Parcel
public class DateGroup {
    public static final String DATEID = "dateId";
    public static final String DATESTRING  = "dateString";
    public static final String EXPENSEID =  "expenseId";
    public static final String TABLE = "dateGroup";
    int dateId;
    String dateName;

    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    @ParcelConstructor
    public DateGroup() {

    }


    public String[] getAllColumns() {
        return new String[] { DATEID, DATESTRING, EXPENSEID};
    }

    public static Uri getUri(){
        return ExpenseContract.DATEGROUP_PATH;
    }
}
