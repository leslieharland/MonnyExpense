package com.example.leslie.monnyfree.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {
    public static Double Round(double value, int places) {
        return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }
}
