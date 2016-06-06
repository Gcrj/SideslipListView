package com.gcrj.library;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;

/**
 * Created by zhangxin on 2016-2-25.
 */
public class SideslipItemLayout extends FrameLayout {

    private int position;
    private onSideItemClickListener listener;

    public SideslipItemLayout(final AdapterView<?> parent, View contentView, int[] resourceId, @Nullable int[] backgroundColor, final int position) {
        super(contentView.getContext());
        setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(contentView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(new SideslipMenuView(getContext(), resourceId, backgroundColor, new SideslipMenuView.onClickListener() {
            @Override
            public void onClick(int sidePosition) {
                if(listener != null){
                    listener.onSideItemClick(parent, SideslipItemLayout.this, SideslipItemLayout.this.position, sidePosition);
                }
            }
        }), new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        this.position = position;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        getChildAt(0).layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        getChildAt(1).layout(right, 0, right + getChildAt(1).getMeasuredWidth(), getMeasuredHeight());
    }

    public void resetContentView(View contentView, int position) {
        removeViewAt(0);
        contentView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(contentView, 0);

        this.position = position;
    }

    public void setOnSideItemClickListener(SideslipItemLayout.onSideItemClickListener listener) {
        this.listener = listener;
    }

    public int getMaxSlipDistance() {
        return getChildAt(1).getMeasuredWidth();
    }


    public interface onSideItemClickListener {
        void onSideItemClick(AdapterView<?> parent, View view, int itemPosition, int sidePosition);
    }

}
