package com.gcrj.library;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageView extends ImageView {
	public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareImageView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// For simple implementation, or internal size is always 0.
		// We depend on the container to specify the layout size of
		// our view. We can't really know what it is since we will be
		// adding and removing different arbitrary views and do not
		// want the layout to change as this happens.
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
				getDefaultSize(0, heightMeasureSpec));

		// Children are just made to fill our space.
		int childWidthSize = getMeasuredWidth();
		int childHeightSize = getMeasuredHeight();
		// 高度和<span id="24_nwp"
		// style="width: auto; height: auto; float: none;"><a id="24_nwl"
		// href="http://cpro.baidu.com/cpro/ui/uijs.php?adclass=0&app_id=0&c=news&cf=1001&ch=0&di=128&fv=18&is_app=0&jk=28dfea4bb95e9f73&k=%BF%ED%B6%C8&k0=%BF%ED%B6%C8&kdi0=0&luki=8&n=10&p=baidu&q=31010181_cpr&rb=0&rs=1&seller_id=1&sid=739f5eb94beadf28&ssp2=1&stid=0&t=tpclicked3_hc&td=2318395&tu=u2318395&u=http%3A%2F%2Fblog%2Echengyunfeng%2Ecom%2F%3Fp%3D465&urlid=0"
		// target="_blank" mpid="24" style="text-decoration: none;"><span
		// style="color:#0000ff;font-size:14px;width:auto;height:auto;float:none;">宽度</span></a></span>一样
		heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(
				childHeightSize, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
