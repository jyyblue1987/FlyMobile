package common.component.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class MyBKTextView extends android.widget.TextView
{

    public MyBKTextView(Context context)
    {
        this(context, null);
    }

    public MyBKTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MyBKTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs);
        if (this.isInEditMode()) return ;

        this.setTypeface(
            Typeface.createFromAsset(context.getAssets(), "fonts/FRADM.TTF")
        );  
    }
}