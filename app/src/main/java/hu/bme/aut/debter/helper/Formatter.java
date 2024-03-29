package hu.bme.aut.debter.helper;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import hu.bme.aut.debter.model.Debt;
import hu.bme.aut.debter.model.MyDebt;
import hu.bme.aut.debter.model.Payment;

public class Formatter {

    public static String formatPaymentValue(Payment payment) {
        double value = payment.getValue();
        String currency = payment.getCurrency();
        return addCurrency(getFormattedValue(value), currency);
    }

    public static String formatValue(double value, String currency) {
        return addCurrency(getFormattedValue(value), currency);
    }

    public static String formatDebtValue(Debt debt) {
        double value = debt.getValue();
        String currency = debt.getCurrency();
        return addCurrency(getFormattedValue(value), currency);
    }

    public static String formatMyDebtValue(MyDebt debt) {
        double value = debt.getValue();
        String currency = debt.getCurrency();
        return addCurrency(getFormattedValue(value), currency);
    }

    private static String getFormattedValue (double value) {
        List<String> seperatedDigits = new LinkedList<>();
        boolean negative = value < 0;
        double hundredth = Math.round(value * 10 - Math.floor(value) * 100);
        int n = (int)Math.round(Math.floor(value)) * (negative ? -1 : 1);

        while (true) {
            int digits = n % 1000;
            n -= digits;
            n /= 1000;
            if (n > 0)
                seperatedDigits.add(0, addZeros(digits));
            else {
                seperatedDigits.add(0, digits + "");
                break;
            }
        }

        if (negative)
            seperatedDigits.set(0, "-" + seperatedDigits.get(0));

        String res = join(seperatedDigits);
        if (hundredth > 0.0001)
            res += "." + hundredth;
        return res;
    }

    private static String addZeros(int n) {
        if (n > 100) return n + "";
        if (n < 10) return "00" + n;
        if (n < 100) return "0" + n;
        return "000";
    }

    private static String join (List<String> arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.size(); ++i) {
            sb.append(arr.get(i));
            if (i != arr.size() - 1)
                sb.append('\u00A0');
        }
        return sb.toString();
    }

    private static String addCurrency(String number, String currency) {
        switch (currency) {
            case "HUF":
                return number + "\u00A0Ft";
            case "EUR":
                return "€\u00A0" + number;
            case "USD":
                return "$\u00A0" + number;
            default:
                return number + ' ' + currency;
        }
    }

    public static String formatDate(Date date) {
        SimpleDateFormat formatter =
                new SimpleDateFormat(getDateFormat(date.getTime()), Locale.ENGLISH);

        return formatter.format(date);
    }

    public static String formatFullDate(Date date) {
        SimpleDateFormat formatter =
                new SimpleDateFormat("y MMM d. H:mm:ss", Locale.ENGLISH);

        return formatter.format(date);
    }

    private static String getDateFormat(long time) {
        long now = new Date().getTime();
        int lengthOfADay = 24 * 60 * 60 * 1000;

        if (time > now - lengthOfADay)
            return "H:mm";
        if (time < now - lengthOfADay * 365)
            return "y MMM d.";
        if (time > now - lengthOfADay * 7)
            return "E, H:mm";
        return "MMM d. H:mm";

    }
}
