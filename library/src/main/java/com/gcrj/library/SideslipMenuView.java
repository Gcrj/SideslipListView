package com.gcrj.library;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by zhangxin on 2016-2-25.
 */
public class SideslipMenuView extends LinearLayout {

    public SideslipMenuView(Context context, int[] resourceId, @Nullable int[] backgroundColor, final onClickListener listener) {
        super(context);
        setOrientation(HORIZONTAL);
        boolean hasColor = (backgroundColor != null);
        for (int i = 0; i < resourceId.length; i++) {
            SquareImageView iv = new SquareImageView(context);
            iv.setImageResource(resourceId[i]);
            final int finalI = i;
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(finalI);
                }
            });
            if (hasColor) {
                iv.setBackgroundColor(backgroundColor[i % backgroundColor.length]);
            }
            addView(iv, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    public interface onClickListener {
        void onClick(int sidePosition);
    }

}
