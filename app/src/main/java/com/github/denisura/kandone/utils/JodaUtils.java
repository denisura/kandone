package com.github.denisura.kandone.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.Spannable;

import com.github.denisura.kandone.R;

import net.danlew.android.joda.DateUtils;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaUtils {

    public static String formatLocalDate(LocalDate date) {
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MMM dd, yyyy");
        return dtfOut.print(date);
    }

    public static String formatDueDate(Context context, LocalDate date) {
        return context.getResources().getString(R.string.format_due_date, formatLocalDate(date));
    }

    public static String formatDoneDate(Context context, LocalDate date) {
        return context.getResources().getString(R.string.format_done_date, formatLocalDate(date));
    }


    public static Spannable formatDueReminder(Context context, LocalDate date) {

        LocalDate now = LocalDate.now();
        Resources res = context.getResources();

        if (DateUtils.isToday(date)) {
            return Spannables.due(context, res.getString(R.string.format_due_today));
        }

        int diff = Months.monthsBetween(now, date).getMonths();

        if (Math.abs(diff) > 0) {
            if (diff > 0) {
                return Spannables.due(context, res.getQuantityString(R.plurals.format_due_in_months, diff, diff));
            }
            diff = Math.abs(diff);
            return Spannables.overdue(context, res.getQuantityString(R.plurals.overdue_months, diff, diff));
        }

        diff = Days.daysBetween(now, date).getDays();

        if (diff > 0) {
            return Spannables.due(context, res.getQuantityString(R.plurals.format_due_in_days, diff, diff));
        }

        diff = Math.abs(diff);
        return Spannables.overdue(context, res.getQuantityString(R.plurals.overdue_days, diff, diff));
    }
}
