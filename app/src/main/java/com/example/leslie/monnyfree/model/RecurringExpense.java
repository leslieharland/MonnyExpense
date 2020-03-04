package com.example.leslie.monnyfree.model;

public class RecurringExpense extends Expense {
    public static final String RECURRINGMODE = "recurringMode";
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";

    @Override
    public String[] getAllColumns() {
        return new String[]{EXPENSEID, CATEGORYID, CATEGORYNAME, CATEGORYICON, DESCRIPTION, AMOUNT, IMAGE, STARTDATE, ENDDATE, RECURRINGMODE};
    }
}
