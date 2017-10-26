package com.duy.compass.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.duy.compass.utils.TypefaceManager;

/**
 * Created by Duy on 10/20/2017.
 */

public class RobotoTextView extends AppCompatTextView {

    public RobotoTextView(Context context) {
        super(context);
        setTypeface(TypefaceManager.get(context, "Roboto-Regular.ttf"));
    }

    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(TypefaceManager.get(context, "Roboto-Regular.ttf"));

    }

    public RobotoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(TypefaceManager.get(context, "Roboto-Regular.ttf"));
    }
}
