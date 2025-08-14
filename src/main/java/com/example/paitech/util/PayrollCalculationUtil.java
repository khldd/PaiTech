package com.example.paitech.util;

import com.example.paitech.model.PayrollItem;

import java.math.BigDecimal;
import java.util.List;

public final class PayrollCalculationUtil {
    private PayrollCalculationUtil() {}

    public static BigDecimal net(BigDecimal gross, BigDecimal taxes, BigDecimal deductions) {
        if (gross == null) gross = BigDecimal.ZERO;
        if (taxes == null) taxes = BigDecimal.ZERO;
        if (deductions == null) deductions = BigDecimal.ZERO;
        BigDecimal net = gross.subtract(taxes).subtract(deductions);
        return net.max(BigDecimal.ZERO);
    }

    /** returns [totalGross, totalNet] */
    public static BigDecimal[] sumItems(List<PayrollItem> items) {
        BigDecimal g = BigDecimal.ZERO;
        BigDecimal n = BigDecimal.ZERO;
        if (items != null) {
            for (PayrollItem it : items) {
                if (it.getGross() != null) g = g.add(it.getGross());
                if (it.getNet() != null) n = n.add(it.getNet());
            }
        }
        return new BigDecimal[]{g, n};
    }
}
