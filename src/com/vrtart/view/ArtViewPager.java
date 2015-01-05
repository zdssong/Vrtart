package com.vrtart.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ArtViewPager extends ViewPager {

	private PointF downPoint = new PointF();
	private OnSingleTouchListener onSingleTouchListener;

	public ArtViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ArtViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// ��¼����ʱ�������
			downPoint.x = ev.getX();
			downPoint.y = ev.getY();
			if (this.getChildCount() > 1) { // �����ݣ�����1��ʱ
				// ֪ͨ�丸�ؼ������ڽ��е��Ǳ��ؼ��Ĳ���������������
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (this.getChildCount() > 1) { // �����ݣ�����1��ʱ
				// ֪ͨ�丸�ؼ������ڽ��е��Ǳ��ؼ��Ĳ���������������
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;
		case MotionEvent.ACTION_UP:
			// ��upʱ�ж��Ƿ��º����ֵ�����Ϊһ����
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
