package common.component.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class MyHVTextView extends android.widget.TextView
{

    public MyHVTextView(Context context)
    {
        this(context, null);
    }

    public MyHVTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MyHVTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs);
        if (this.isInEditMode()) return ;

        this.setTypeface(
            Typeface.createFromAsset(context.getAssets(), "fonts/FRAHV.TTF")
        );  
    }
}