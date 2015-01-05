package com.vrtart.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class ArtScrollViewOne extends ScrollView {
	public ScrollView parentScrollView;

	public ArtScrollViewOne(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	private int lastScrollDelta = 0;

	public void resume() {
		overScrollBy(0, -lastScrollDelta, 0, getScrollY(), 0, getScrollRange(),
				0, 0, true);
		lastScrollDelta = 0;
	}

	int mTop = 10;

	public void scrollTo(View targetView) {

		int oldScrollY = getScrollY();
		int top = targetView.getTop() - mTop;
		int delatY = top - oldScrollY;
		lastScrollDelta = delatY;
		overScrollBy(0, delatY, 0, getScrollY(), 0, getScrollRange(), 0, 0,
				true);
	}

	private int getScrollRange() {
		int scrollRange = 0;
		if (getChildCount() > 0) {
			View child = getChildAt(0);
			scrollRange = Math.max(0, child.getHeight() - (getHeight()));
		}
		return scrollRange;
	}

	int currentY;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (parentScrollView == null) {
			return super.onInterceptTouchEvent(ev);
		} else {
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				// ����scrollview�Ĺ����¼�����
				currentY = (int) ev.getY();
				setParentScrollAble(false);
				return super.onInterceptTouchEvent(ev);
			} else if (ev.getAction() == MotionEvent.ACTION_UP) {
				// �ѹ����¼��ָ�����Scrollview
				setParentScrollAble(true);
			} else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			}
		}
		return super.onInterceptTouchEvent(ev);

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		View child = getChildAt(0);
		if (parentScrollView != null) {
			if (ev.getAction() == MotionEvent.ACTION_MOVE) {
				int height = child.getMeasuredHeight();
				height = height - getMeasuredHeight();

				// System.out.println("height=" + height);
				int scrollY = getScrollY();
				// System.out.println("scrollY" + scrollY);
				int y = (int) ev.getY();

				// ��ָ���»���
				if (currentY < y) {
					if (scrollY <= 0) {
						// ������»�����ͷ���Ͱѹ���������Scrollview
						setParentScrollAble(true);
						return false;
					} else {
						setParentScrollAble(false);

					}
				} else if (currentY > y) {
					if (scrollY >= height) {
						// ������ϻ�����ͷ���Ͱѹ���������Scrollview
						setParentScrollAble(true);
						return false;
					} else {
						setParentScrollAble(false);

					}

				}
				currentY = y;
			}
		}

		return super.onTouchEvent(ev);
	}

	private void setParentScrollAble(boolean flag) {

		parentScrollView.requestDisallowInterceptTouchEvent(!flag);
	}

}
