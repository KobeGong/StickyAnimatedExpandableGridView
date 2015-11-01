package com.baidu.stickyheadergridview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;


/**
 * Created by gonggaofeng on 15/10/31.
 */
public class StickyHeaderExpandableGridView extends AnimatedExpandableGridView implements AbsListView.OnScrollListener {

    private ExpandableListAdapter mAdapter;

    public interface OnHeaderUpdateListener {

        public void updatePinnedHeader(View headerView, int firstVisibleGroupPos);
    }

    private SparseArray<View> cacheHeaderViews;

    private View mHeaderView;
    private int mHeaderWidth;
    private int mHeaderHeight;
    private AbsListView.OnScrollListener mScrollListener;
    private OnHeaderUpdateListener mHeaderUpdateListener;

    private static final String TAG = StickyHeaderExpandableGridView.class.getSimpleName();

    public StickyHeaderExpandableGridView(Context context) {
        this(context, null);

    }

    public StickyHeaderExpandableGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyHeaderExpandableGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFadingEdgeLength(0);
        setOnScrollListener(this);
    }

    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = adapter;
        mHeaderView = adapter.getGroupView(0, false, null, this);
        boolean isBaseAdapter = adapter instanceof BaseExpandableListAdapter;
        if (cacheHeaderViews == null) {
            if (isBaseAdapter) {
                int typeCount = ((BaseExpandableListAdapter) adapter).getGroupTypeCount();
                cacheHeaderViews = new SparseArray<>(typeCount);
            }
            cacheHeaderViews = new SparseArray<>(1);
        }
        if (mHeaderView != null) {
            int groupType = 0;
            if (isBaseAdapter) {
                groupType = ((BaseExpandableListAdapter) adapter).getGroupType(0);
                cacheHeaderViews.put(groupType, mHeaderView);
            }
            cacheHeaderViews.put(groupType, mHeaderView);
        }
    }


    @Override
    public void setOnScrollListener(AbsListView.OnScrollListener l) {
        if (l != this) {
            mScrollListener = l;
        } else {
            mScrollListener = null;
        }
        super.setOnScrollListener(this);
    }

    public void setOnHeaderUpdateListener(OnHeaderUpdateListener listener) {
        mHeaderUpdateListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView == null) {
            return;
        }
        measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
        mHeaderWidth = mHeaderView.getMeasuredWidth();
        mHeaderHeight = mHeaderView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mHeaderView == null) {
            return;
        }
        int delta = mHeaderView.getTop();
        mHeaderView.layout(0, delta, mHeaderWidth, mHeaderHeight + delta);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderView != null) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }

    public void requestRefreshHeader() {
        refreshHeader();
        invalidate(new Rect(0, 0, mHeaderWidth, mHeaderHeight));
    }

    protected void refreshHeader() {
        if (mHeaderView == null) {
            return;
        }
        int firstVisiblePos = getFirstVisiblePosition();
        int pos = firstVisiblePos + 1;
        int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
        int group = getPackedPositionGroup(getExpandableListPosition(pos));
        int type = mAdapter instanceof BaseExpandableListAdapter ?
                ((BaseExpandableListAdapter) mAdapter).getGroupType(firstVisibleGroupPos) : 0;
        View convertView = cacheHeaderViews == null ? null : cacheHeaderViews.get(type);
        mHeaderView = mAdapter.getGroupView(firstVisibleGroupPos, false, convertView, this);
        if (group == firstVisibleGroupPos + 1) {
            View view = getChildAt(1);
            if (view == null) {
                return;
            }
            if (view.getTop() <= mHeaderHeight) {
                int delta = mHeaderHeight - view.getTop();
                mHeaderView.layout(0, -delta, mHeaderWidth, mHeaderHeight - delta);
            } else {
                mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
            }
        } else {
            mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
        }

        if (mHeaderUpdateListener != null) {
            mHeaderUpdateListener.updatePinnedHeader(mHeaderView, firstVisibleGroupPos);
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount > 0) {
            refreshHeader();
        }
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}

