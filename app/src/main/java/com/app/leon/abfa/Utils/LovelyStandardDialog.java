package com.app.leon.abfa.Utils;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.app.leon.abfa.R;

import static android.view.View.VISIBLE;

/**
 * Created by Leon on 2/7/2018.
 */

public class LovelyStandardDialog extends AbsLovelyDialog<LovelyStandardDialog> {

    public static final int POSITIVE_BUTTON = com.yarolegovich.lovelydialog.R.id.ld_btn_yes;
    public static final int NEGATIVE_BUTTON = com.yarolegovich.lovelydialog.R.id.ld_btn_no;
    public static final int NEUTRAL_BUTTON = com.yarolegovich.lovelydialog.R.id.ld_btn_neutral;

    private Button positiveButton;
    private Button negativeButton;
    private Button neutralButton;
    private Context context;

    {
        positiveButton = findView(R.id.ld_btn_yes);
        negativeButton = findView(R.id.ld_btn_no);
        neutralButton = findView(R.id.ld_btn_neutral);
    }

    public LovelyStandardDialog(Context context) {
        super(context);
        this.context = context;

    }

    public LovelyStandardDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public LovelyStandardDialog setPositiveButton(@StringRes int text, View.OnClickListener listener) {
        return setPositiveButton(string(text), listener);
    }

    public LovelyStandardDialog setPositiveButton(String text, @Nullable View.OnClickListener listener) {
        positiveButton.setVisibility(VISIBLE);
        positiveButton.setText(text);
        positiveButton.setTextColor(context.getResources().getColor(R.color.white));
        positiveButton.setTextSize(context.getResources().getDimension(R.dimen.textSizeMedium));
        positiveButton.setOnClickListener(new ClickListenerDecorator(listener, true));
        return this;
    }

    public LovelyStandardDialog setNegativeButtonText(@StringRes int text) {
        return setNegativeButton(string(text), null);
    }

    public LovelyStandardDialog setNegativeButtonText(String text) {
        return setNegativeButton(text, null);
    }

    public LovelyStandardDialog setNegativeButton(@StringRes int text, View.OnClickListener listener) {
        return setNegativeButton(string(text), listener);
    }

    public LovelyStandardDialog setNegativeButton(String text, @Nullable View.OnClickListener listener) {
        negativeButton.setVisibility(VISIBLE);
        negativeButton.setText(text);
        negativeButton.setOnClickListener(new ClickListenerDecorator(listener, true));
        return this;
    }

    public LovelyStandardDialog setNeutralButtonText(@StringRes int text) {
        return setNeutralButton(string(text), null);
    }

    public LovelyStandardDialog setNeutralButtonText(String text) {
        return setNeutralButton(text, null);
    }

    public LovelyStandardDialog setNeutralButton(@StringRes int text, @Nullable View.OnClickListener listener) {
        return setNeutralButton(string(text), listener);
    }

    public LovelyStandardDialog setNeutralButton(String text, @Nullable View.OnClickListener listener) {
        neutralButton.setVisibility(VISIBLE);
        neutralButton.setText(text);
        neutralButton.setOnClickListener(new ClickListenerDecorator(listener, true));
        return this;
    }

    public LovelyStandardDialog setButtonsColor(@ColorInt int color) {
        positiveButton.setBackgroundColor(color);
        negativeButton.setBackgroundColor(color);
        neutralButton.setBackgroundColor(color);
        return this;
    }

    public LovelyStandardDialog setButtonsColorRes(@ColorRes int colorRes) {
        return setButtonsColor(color(colorRes));
    }

    public LovelyStandardDialog setButtonsBackgroundRes(int res) {
        positiveButton.setBackgroundDrawable(context.getResources().getDrawable(res));
        negativeButton.setBackgroundDrawable(context.getResources().getDrawable(res));
        neutralButton.setBackgroundDrawable(context.getResources().getDrawable(res));
        return this;
    }

    public LovelyStandardDialog setButtonsBackground(int backgroundRes) {
        return setButtonsBackgroundRes(backgroundRes);
    }


    public LovelyStandardDialog setOnButtonClickListener(View.OnClickListener listener) {
        return setOnButtonClickListener(true, listener);
    }

    public LovelyStandardDialog setOnButtonClickListener(boolean closeOnClick, View.OnClickListener listener) {
        View.OnClickListener clickHandler = new ClickListenerDecorator(listener, closeOnClick);
        positiveButton.setOnClickListener(clickHandler);
        neutralButton.setOnClickListener(clickHandler);
        negativeButton.setOnClickListener(clickHandler);
        return this;
    }

    public LovelyStandardDialog setPositiveButtonText(@StringRes int text) {
        return setPositiveButton(string(text), null);
    }

    public LovelyStandardDialog setPositiveButtonText(String text) {
        return setPositiveButton(text, null);
    }

    public LovelyStandardDialog setPositiveButtonColor(@ColorInt int color) {
        positiveButton.setTextColor(color);
        return this;
    }

    public LovelyStandardDialog setNegativeButtonColor(@ColorInt int color) {
        negativeButton.setTextColor(color);
        return this;
    }

    public LovelyStandardDialog setNeutralButtonColor(@ColorInt int color) {
        neutralButton.setTextColor(color);
        return this;
    }

    public LovelyStandardDialog setPositiveButtonColorRes(@ColorRes int colorRes) {
        return setPositiveButtonColor(color(colorRes));
    }

    public LovelyStandardDialog setNegativeButtonColorRes(@ColorRes int colorRes) {
        return setNegativeButtonColor(color(colorRes));
    }

    public LovelyStandardDialog setNeutralButtonColorRes(@ColorRes int colorRes) {
        return setNeutralButtonColor(color(colorRes));
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_standard;
    }
}
