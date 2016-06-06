package com.gcrj.library;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by zhangxin on 2016-2-25.
 */
public class SideslipListView extends ListView {
    private static final int TOUCH_STATE_NONE = 0;
    private static final int TOUCH_STATE_X = 1;
    private static final int TOUCH_STATE_Y = 2;

    private int touchState;
    private int lastItemCount;
    private int currentItemCount;
    private SideslipItemLayout lastTouchLayout;
    private SideslipItemLayout currentTouchLayout;
    private int touchSlop;
    private float startX;
    private float startY;
    private float delayX;
    private Rect originalRect;

    private int[] resourceId;
    private int[] backgroundColor;
    private SideslipItemLayout.onSideItemClickListener listener;


    public SideslipListView(Context context) {
        super(context);
        init(context);
    }

    public SideslipListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context));
        originalRect = new Rect();
    }

    public void setOnSideItemClickListener(SideslipItemLayout.onSideItemClickListener listener){
        this.listener = listener;
    }

    /**
     * invoke before {@link SideslipListView#setAdapter(ListAdapter)} if backgroundColor.length < resourceId.length, we will use the result of (position % backgroundColor.length)
     */
    public void setMenu(int[] resourceId, @Nullable int[] backgroundColor){
        this.resourceId = resourceId;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void setAdapter(final ListAdapter adapter) {
        if(resourceId == null || resourceId.length == 0){
            throw new IllegalStateException("The resourceId is null !!");
        }

        ListAdapter adapter1 = new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return adapter.areAllItemsEnabled();
            }

            @Override
            public boolean isEnabled(int position) {
                return adapter.isEnabled(position);
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {
                adapter.registerDataSetObserver(observer);
            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {
                adapter.unregisterDataSetObserver(observer);
            }

            @Override
            public int getCount() {
                return adapter.getCount();
            }

            @Override
            public Object getItem(int position) {
                return adapter.getItem(position);
            }

            @Override
            public long getItemId(int position) {
                return adapter.getItemId(position);
            }

            @Override
            public boolean hasStableIds() {
                return adapter.hasStableIds();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = new SideslipItemLayout(SideslipListView.this, adapter.getView(position, convertView, parent), resourceId, backgroundColor, position);
                    ((SideslipItemLayout)convertView).setOnSideItemClickListener(listener);
                } else {
                    ((SideslipItemLayout) convertView).resetContentView(adapter.getView(position, ((SideslipItemLayout) convertView).getChildAt(0), parent), position);
                }
                return convertView;
            }

            @Override
            public int getItemViewType(int position) {
                return adapter.getItemViewType(position);
            }

            @Override
            public int getViewTypeCount() {
                return adapter.getViewTypeCount();
            }

            @Override
            public boolean isEmpty() {
                return adapter.isEmpty();
            }
        };

        super.setAdapter(adapter1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                startY = ev.getY();
                currentItemCount = getAdapter().getCount();
                currentTouchLayout = (SideslipItemLayout) getChildAt(pointToPosition((int) ev.getX(), (int) ev.getY()) - getFirstVisiblePosition());
                if(currentTouchLayout == null){
                    return super.onTouchEvent(ev);
                }
                if (lastTouchLayout != null) {
                    if(currentTouchLayout == lastTouchLayout && currentItemCount == lastItemCount){
                        if(delayX > 0){
                            startX += currentTouchLayout.getMaxSlipDistance();
                        }
                    }else {
                        //last归位
                        lastTouchLayout.layout(originalRect.left, lastTouchLayout.getTop(), originalRect.right, lastTouchLayout.getBottom());
                        lastItemCount = currentItemCount;
                        lastTouchLayout = currentTouchLayout;
                        originalRect.set(currentTouchLayout.getLeft(), currentTouchLayout.getTop(), currentTouchLayout.getRight(), currentTouchLayout.getBottom());
                    }
                }else{
                    lastItemCount = getAdapter().getCount();
                    lastTouchLayout = currentTouchLayout;
                    originalRect.set(currentTouchLayout.getLeft(), currentTouchLayout.getTop(), currentTouchLayout.getRight(), currentTouchLayout.getBottom());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(currentTouchLayout == null){
                    return super.onTouchEvent(ev);
                }
                if (touchState == TOUCH_STATE_NONE) {
                    if (Math.abs(startY - ev.getY()) > touchSlop) {
                        touchState = TOUCH_STATE_Y;
                        return super.onTouchEvent(ev);
                    }
                    if (startX - ev.getX() > touchSlop) {
                        delayX = startX - ev.getX();
                        if (delayX > currentTouchLayout.getMaxSlipDistance()) {
                            delayX = currentTouchLayout.getMaxSlipDistance();
                        } else if (delayX < 0) {
                            delayX = 0;
                        }
                        touchState = TOUCH_STATE_X;
                        currentTouchLayout.layout((int) (originalRect.left - delayX), currentTouchLayout.getTop(), originalRect.right, currentTouchLayout.getBottom());
                        getSelector().setState(new int[] { 0 });
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        super.onTouchEvent(ev);
                        return true;
                    }
//                    else{
//                        return true;
//                    }
                } else if (touchState == TOUCH_STATE_X) {
                    delayX = startX - ev.getX();
                    if (delayX > currentTouchLayout.getMaxSlipDistance()) {
                        delayX = currentTouchLayout.getMaxSlipDistance();
                    } else if (delayX < 0) {
                        delayX = 0;
                    }
                    currentTouchLayout.layout((int) (originalRect.left - delayX), currentTouchLayout.getTop(), originalRect.right, currentTouchLayout.getBottom());
                    return true;
                } else if (touchState == TOUCH_STATE_Y) {
                    //last 归位
                }


                break;
            case MotionEvent.ACTION_UP:
                if(currentTouchLayout == null){
                    return super.onTouchEvent(ev);
                }
                if (touchState == TOUCH_STATE_X) {
                    //位置纠正
                    if(delayX >  currentTouchLayout.getMaxSlipDistance()/2){
                        delayX =  currentTouchLayout.getMaxSlipDistance();
                    }else{
                        delayX = 0;
                    }
                    currentTouchLayout.layout((int) (originalRect.left - delayX), currentTouchLayout.getTop(), originalRect.right, currentTouchLayout.getBottom());
                }else if(touchState == TOUCH_STATE_Y){
                    currentTouchLayout.layout(originalRect.left, currentTouchLayout.getTop(), originalRect.right, currentTouchLayout.getBottom());
                }
                touchState = TOUCH_STATE_NONE;
                break;
        }
        return super.onTouchEvent(ev);
    }

}
