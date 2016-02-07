package common.component.ui;


import android.content.Context;
import android.util.AttributeSet;

public class MyButton extends android.widget.Button
{
	
	public MyButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public MyButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		CustomFontHelper.setCustomFont(this, context, attrs);
	}

	

	

   
}