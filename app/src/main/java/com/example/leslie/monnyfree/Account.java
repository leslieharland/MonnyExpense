package com.example.leslie.monnyfree;

import android.net.Uri;

public class Account {
    public static final String ACCOUNTID = "accountId";
    public static final String ACCOUNTNAME = "accountName";
    public static final String IMAGE = "image";
    public static final String CURRENCYID = "currencyId";
    public static final String CURRENCYCODE = "abv";
    public static final String DEFAULTACCOUNT = "default";
    public static final String TABLE = "accountDatabase";
    public static final String NUMBEROFRECORDS = "numberOfRecords";

    public String getAccountName(){
        return "Main Account";
    }

    public static int id;
    public String name;
    public String image;
    public int currencyId;
    public String currencyCode;


    public boolean defaultAccount;


    public int numberOfRecords;

    public static int getId() {
        return id;
    }

    public static void setGlobalAccountId() {
        id = 1;
    }

    public static void setId(int value) {
        id = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public void setDefault(boolean value) {
        defaultAccount = value;
    }

    public boolean isDefaultAccount() {
        return defaultAccount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(int numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    public static Uri getUri() {
        return ExpenseContract.ACCOUNT_PATH;
    }

    public String[] getAllColumns() {
        return new String[]{ACCOUNTID, ACCOUNTNAME, IMAGE, CURRENCYID, CURRENCYCODE, NUMBEROFRECORDS};
    }
}
