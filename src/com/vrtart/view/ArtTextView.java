package com.vrtart.view;

import com.vrtart.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * �Զ����TextView��������ʾ�α��Ǵ��߿��TextView
 */

public class ArtTextView extends TextView {

	private Paint mBorderPaint;
	private boolean mTopBorder;
	private boolean mBottomBorder;
	private boolean mLeftBorder;
	private boolean mRightBorder;

	public ArtTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initZsxyTextView();
		mLeftBorder = true;
		mRightBorder = true;
	}

	@SuppressLint("Recycle")
	public ArtTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initZsxyTextView();
		// ���������Զ�������У�������Ҫʹ��һ����ΪobtainStyledAttributes�ķ�������ȡ���ǵĶ��塣
		TypedArray params = context.obtainStyledAttributes(attrs,
				R.styleable.TestView);
		// �õ��Զ���ؼ�������ֵ��

		mTopBorder = params.getBoolean(R.styleable.TestView_topBorder, true);
		mLeftBorder = params.getBoolean(R.styleable.TestView_leftBorder, true);
		mBottomBorder = params.getBoolean(R.styleable.TestView_bottomBorder,
				true);
		mRightBorder = params
				.getBoolean(R.styleable.TestView_rightBorder, true);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mTopBorder == true)
			canvas.drawLine(0, 0, this.getWidth() - 1, 0, mBorderPaint);
		if (mRightBorder == true)
			canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1,
					this.getHeight() - 1, mBorderPaint);
		if (mBottomBorder == true)
			canvas.drawLine(this.getWidth() - 1, this.getHeight() - 1, 0,
					this.getHeight() - 1, mBorderPaint);
		if (mLeftBorder == true)
			canvas.drawLine(0, this.getHeight() - 1, 0, 0, mBorderPaint);
	}

	// ��ʼ����Ӧ�ı���
	public void initZsxyTextView() {
		mBorderPaint = new Paint();
		mBorderPaint.setColor(0XFFFFFFFF);
	}

	// �����ϱ߽�
	public void setTopBorder(boolean mTopBorder) {
		this.mTopBorder = mTopBorder;
	}

	// �����±߽�
	public void setBottomBorder(boolean mBottomBorder) {
		this.mBottomBorder = mBottomBorder;
	}

	// ������߽�
	public void setLeftBorder(boolean mLeftBorder) {
		this.mLeftBorder = mLeftBorder;
	}

	// �����ұ߽�
	public void setRightBorder(boolean mRightBorder) {
		this.mRightBorder = mRightBorder;
	}

}
