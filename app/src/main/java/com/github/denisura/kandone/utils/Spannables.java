package com.github.denisura.kandone.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.github.denisura.kandone.R;

public class Spannables {
    public static Spannable due(Context context, String text) {
        Spannable spanText = new SpannableString(text);
        spanText.setSpan(new ForegroundColorSpan(context.getResources()
                .getColor(R.color.colorPrimary)), 0, text.length(), 0);
        return spanText;
    }

    public static Spannable overdue(Context context, String text) {
        Spannable spanText = new SpannableString(text);
        spanText.setSpan(new ForegroundColorSpan(context.getResources()
                .getColor(R.color.colorAccent)), 0, text.length(), 0);
        return spanText;
    }
}
