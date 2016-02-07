package common.component.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class MyTextView extends android.widget.TextView
{

    public MyTextView(Context context)
    {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        if (this.isInEditMode()) return ;

        this.setTypeface(
            Typeface.createFromAsset(context.getAssets(), "fonts/FRABK.TTF")
        );  
    }
}