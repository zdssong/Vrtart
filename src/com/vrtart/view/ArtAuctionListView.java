package com.vrtart.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class ArtAuctionListView extends ListView {
	private PointF downPoint = new PointF();
	private OnSingleTouchListener onSingleTouchListener;

	public ArtAuctionListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ArtAuctionListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ArtAuctionListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 记录按下时候的坐标
			downPoint.x = ev.getX();
			downPoint.y = ev.getY();
			if (this.getChildCount() > 1) { // 有内容，多于1个时
				// 通知其父控件，现在进行的是本控件的操作，不允许拦截
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (this.getChildCount() > 1) { // 有内容，多于1个时
				// 通知其父控件，现在进行的是本控件的操作，不允许拦截
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;
		case MotionEvent.ACTION_UP:
			// 在up时判断是否按下和松手的坐标为一个点
			if (PointF.length(ev.getX() - downPoint.x, ev.getY() - downPoint.y) < (float) 5.0) {
				onSingleTouch(this);
				return true;
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	public void onSingleTouch(View v) {
		if (onSingleTouchListener != null) {
			onSingleTouchListener.onSingleTouch(v);
		}
	}

	public interface OnSingleTouchListener {
		public void onSingleTouch(View v);
	}

	public void setOnSingleTouchListener(
			OnSingleTouchListener onSingleTouchListener) {
		this.onSingleTouchListener = onSingleTouchListener;
	}
}
