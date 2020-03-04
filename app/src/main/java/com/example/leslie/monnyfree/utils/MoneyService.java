package com.example.leslie.monnyfree.utils;

import java.text.DecimalFormat;

public class MoneyService {
    DecimalFormat precision = new DecimalFormat("0.00");

    public Float convertToTwoDecimalPlace(Double amount){
      return  Float.parseFloat(precision.format(amount));
    }

    public Float convertToTwoDecimalPlace(String amount){
        return  Float.parseFloat(precision.format(amount));
    }

    public Float convertToTwoDecimalPlace(Float amount){
        return  Float.parseFloat(precision.format(amount));
    }

    public String convertToTwoDecimalPlaceString(Double amount){
        return  precision.format(amount);
    }

    public String convertToTwoDecimalPlaceString(String amount){
        return  precision.format(Double.parseDouble(amount));
    }

    public String convertToTwoDecimalPlaceString(Float amount){
        return  precision.format(amount);
    }
}
