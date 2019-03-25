package com.example.wayup.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class CusEditText extends EditText
{

    public CusEditText(Context context)
    {
        super(context);
        init(context);
    }

    public CusEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public CusEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }

    public void init(Context context)
    {
        Typeface myFont = Typeface.createFromAsset(context.getAssets(), "fonts/SVN-Bariol.ttf");
        setTypeface(myFont);
    }
}