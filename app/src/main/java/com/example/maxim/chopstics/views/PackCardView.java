package com.example.maxim.chopstics.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.maxim.chopstics.R;

public class PackCardView extends CardView {

    public ImageView packIcon = null;
    public TextView packName = null;
    public TextView packProgressText = null;
    public ProgressBar packProgressBar = null;

    public PackCardView(Context context) {
        super(context);
    }

    public PackCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PackCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        packIcon = (ImageView) findViewById(R.id.pack_icon);
        packName = (TextView) findViewById(R.id.pack_name);
        packProgressText = (TextView) findViewById(R.id.pack_progress_text);
        packProgressBar = (ProgressBar) findViewById(R.id.pack_progress_bar);
    }
}
