package com.example.hendry.myapplication.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import com.example.hendry.myapplication.R;

/**
 * Custom Edit Text so show the prefix in font of the inputted text
 * For example:
 * Rp.50000
 * IDR 50000
 * http://www.google.com
 */
public class PrefixEditText extends AppCompatEditText
implements TextWatcher{
    private String mPrefix;
    private int mColor;

    public PrefixEditText(Context context) {
        super(context);
        init(null, 0);
    }

    public PrefixEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PrefixEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PrefixEditText, defStyle, 0);

        mPrefix = a.getString(
                R.styleable.PrefixEditText_prefix);
        mColor = a.getColor(
                R.styleable.PrefixEditText_color,
                Color.GREEN);
        a.recycle();

        int[] set = {
                android.R.attr.text        // idx 0
        };
        a = getContext().obtainStyledAttributes(
                attrs, set);
        String text = a.getString(0);
        a.recycle();

        setText(mPrefix+text);
        addTextChangedListener(this);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (TextUtils.isEmpty(mPrefix)) {
            super.setText(text, type);
        }
        else if (text == null) {
            //spannable.setSpan(new ForegroundColorSpan(Color.WHITE), text.length(), (text + CepVizyon.getPhoneCode()).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            int prefixLength = mPrefix.length();
            Spannable spannable = new SpannableString(mPrefix);
            spannable.setSpan(new ForegroundColorSpan(mColor),
                    0, mPrefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            super.setText(spannable, type);
            Selection.setSelection(spannable,prefixLength);
        }
        else {
            String textString = text.toString();
            if (textString.startsWith(mPrefix)) {
                Spannable spannable = new SpannableString(textString);
                spannable.setSpan(new ForegroundColorSpan(mColor),
                        0, mPrefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                super.setText(spannable, type);
            } else {
                String combinedString = mPrefix + textString;
                Spannable spannable = new SpannableString(combinedString);
                spannable.setSpan(new ForegroundColorSpan(mColor),
                        0, mPrefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                super.setText(spannable, type);
            }
            Selection.setSelection(super.getText(),
                    super.getText().length());
        }
    }

    public String getTextWithoutPrefix(){
        Editable s = super.getText();
        return s.toString().replaceFirst("^" + mPrefix, "").trim();
    }

    public String getPrefix() {
        return mPrefix;
    }

    public void setPrefix(String prefix) {
        mPrefix = prefix;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!s.toString().startsWith(mPrefix)) {
            removeTextChangedListener(this);
            PrefixEditText.super.setText(mPrefix);
            Selection.setSelection(PrefixEditText.super.getText(),
                    PrefixEditText.super.getText().length());
            addTextChangedListener(this);
        }
    }

}
