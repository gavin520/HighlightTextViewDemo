package com.tanny.customhighlight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created By tanny on 2017/9/27.
 */
public class HighlightTextView extends TextView
{
    private int highlightColor = 0xffff0000;

    private boolean disallowHighlightLineBreak;

    private String text;

    private List<String> highlightTextList;

    private List<HighlightTextSpec> highlightTextSpecList;

    public HighlightTextView(Context context) {
        this(context, null);
    }

    public HighlightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // get custom attrs
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HighlightTextView);
        highlightColor = a.getColor(R.styleable.HighlightTextView_hltvHighlightColor, highlightColor);
        String highlightTexts = a.getString(R.styleable.HighlightTextView_hltvHighlightTexts);
        if (highlightTexts != null) {
            highlightTextList = Arrays.asList(highlightTexts.split(","));
        }
        disallowHighlightLineBreak = a.getBoolean(R.styleable.HighlightTextView_hltvDisallowHighlightLineBreak, disallowHighlightLineBreak);
        setDisplayedText(getText().toString(), highlightTextList);
        a.recycle();
    }

    @SuppressWarnings("deprecation")
    public void setDefaultColorResource(int resId) {
        setTextColor(getResources().getColor(resId));
    }

    public void setDefaultColor(int color) {
        setTextColor(color);
    }

    @SuppressWarnings("deprecation")
    public void setHighlightColorResource(int resId) {
        setHighlightColor(getResources().getColor(resId));
    }

    public void setHighlightColor(int color) {
        if (highlightColor != color) {
            highlightColor = color;
            if (!TextUtils.isEmpty(this.text)) {
                displayWithHighlight();
            }
        }
    }

    /**
     * Prefer to invork this method before {@link #setDisplayedText(String, List)}, otherwise low efficiency
     */
    public void setDisallowHighlightLineBreak(boolean nonLineBreak) {
        if (disallowHighlightLineBreak == nonLineBreak) {
            return;
        }

        disallowHighlightLineBreak = nonLineBreak;

        if (TextUtils.isEmpty(this.text)) {
            return;
        }

        if (!disallowHighlightLineBreak) {
            StringBuilder textBuilder = new StringBuilder(this.text);
            // recover to space
            if (highlightTextSpecList != null) {
                for (HighlightTextSpec spec : highlightTextSpecList) {
                    if (spec.spaceIndexs == null) {
                        continue;
                    }

                    for (Integer index : spec.spaceIndexs) {
                        textBuilder.replace(index, index + 1, " ");
                    }
                }
            }
            this.text = textBuilder.toString();
        }

        setDisplayedText(this.text, highlightTextList);
    }

    /**
     * only set the display text, highlight text may be from custom attrs
     * @param text text to display
     */
    public void setDisplayedText(final String text) {
        this.text = text;
        highlightTextSpecList = findHighlightText(text, highlightTextList);
        displayWithHighlight();
    }

    public void setDisplayedText(final String text, List<String> highlightTextList) {

        this.text = text;
        this.highlightTextList = highlightTextList;
        if (highlightTextList == null || highlightTextList.isEmpty()) {
            setText(text);
            return;
        }

        highlightTextSpecList = findHighlightText(text, highlightTextList);
        displayWithHighlight();
    }

    private void findSpaceAmongHighlightText(StringBuilder textBuilder, HighlightTextSpec spec) {
        Pattern pattern = Pattern.compile(" ");
        Matcher matcher = pattern.matcher(spec.hightText);
        int spaceIndex;
        while (matcher.find()) {
            if (spec.spaceIndexs == null) {
                spec.spaceIndexs = new ArrayList<>();
            }
            spaceIndex = matcher.start() + spec.start;
            spec.spaceIndexs.add(spaceIndex);
            // Use letters to connect highlighted word group as a whole
            textBuilder.replace(spaceIndex, spaceIndex + 1, "i");
        }
    }

    private void displayWithHighlight() {
        if (highlightTextSpecList.isEmpty()) {
            setText(this.text);
            return;
        }

        String text = "";
        if (!TextUtils.isEmpty(this.text)) {
            text = this.text;
        }

        SpannableString spannableString = new SpannableString(text);
        if (highlightTextSpecList != null) {
            for (HighlightTextSpec spec : highlightTextSpecList) {
                spannableString.setSpan(new ForegroundColorSpan(highlightColor), spec.start, spec.end,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                // make letters of connecting word group transparent
                if (spec.spaceIndexs != null) {
                    for (Integer spaceIndex : spec.spaceIndexs) {
                        spannableString.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), spaceIndex, spaceIndex + 1,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }

        setText(spannableString);
    }

    @NonNull
    private List<HighlightTextSpec> findHighlightText(String text, List<String> highlightTexts) {
        List<HighlightTextSpec> highlightTextSpecList = new ArrayList<>();
        // use mutuble string to edit string
        StringBuilder textBuilder = new StringBuilder(this.text);
        for (String highlightText : highlightTexts) {
            if (TextUtils.isEmpty(highlightText)) {
                continue;
            }

            Pattern pattern = Pattern.compile(highlightText);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                HighlightTextSpec spec = new HighlightTextSpec();
                spec.start = matcher.start();
                spec.end = matcher.end();
                spec.hightText = highlightText;
                highlightTextSpecList.add(spec);
                if (disallowHighlightLineBreak) {
                    findSpaceAmongHighlightText(textBuilder, spec);
                }
            }
        }
        this.text = textBuilder.toString();
        return highlightTextSpecList;
    }

    static class HighlightTextSpec {
        /** the beginning index, inclusive */
        int start;
        /** the ending index, exclusive */
        int end;
        /** all space index among word group*/
        List<Integer> spaceIndexs;
        String hightText;
    }
}
