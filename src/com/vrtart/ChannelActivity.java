package com.vrtart;

import java.util.ArrayList;
import java.util.List;

import com.vrtart.R;
import com.vrtart.adapter.DragAdapter;
import com.vrtart.adapter.OtherAdapter;
import com.vrtart.application.ArtApplication;
import com.vrtart.baseActicity.BaseActivity;
import com.vrtart.contants.ArtContants;
import com.vrtart.models.Column;
import com.vrtart.tools.ChannelManage;
import com.vrtart.view.DragGrid;
import com.vrtart.view.OtherGridView;

import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * 频道管理
 */
public class ChannelActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener {
	public static String TAG = "ChannelActivity";
	/** 用户栏目的GRIDVIEW */
	private DragGrid userGridView;
	/** 其它栏目的GRIDVIEW */
	private OtherGridView otherGridView;
	/** 用户栏目对应的适配器，可以拖动 */
	DragAdapter userAdapter;
	/** 其它栏目对应的适配器 */
	OtherAdapter otherAdapter;
	/** 其它栏目列表 */
	List<Column> otherChannelList = new ArrayList<Column>();
	/** 用户栏目列表 */
	List<Column> userChannelList = new ArrayList<Column>();
	/** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
	boolean isMove = false;

	private Button mSearchNewButton;
	private Button mSearchShowButton;
	private Button mSearchMemberButton;
	private Button mSearchAuctionButton;
	private EditText mSearchEditText;

	private ChannelManage m_ChannelManage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.channel);
		initView();
		initData();
		initListener();
	}

	/** 初始化数据 */
	private void initData() {
		m_ChannelManage = new ChannelManage(this);
		userChannelList = m_ChannelManage.getUserChannel();
		otherChannelList = m_ChannelManage.getOtherChannel();
		userAdapter = new DragAdapter(this, userChannelList);
		userGridView.setAdapter(userAdapter);
		otherAdapter = new OtherAdapter(this, otherChannelList);
		otherGridView.setAdapter(otherAdapter);
		// 设置GRIDVIEW的ITEM的点击监听
		otherGridView.setOnItemClickListener(this);
		userGridView.setOnItemClickListener(this);
	}

	/** 初始化布局 */
	private void initView() {
		userGridView = (DragGrid) findViewById(R.id.userGridView);
		otherGridView = (OtherGridView) findViewById(R.id.otherGridView);

		mSearchMemberButton = (Button) findViewById(R.id.member_button);
		mSearchNewButton = (Button) findViewById(R.id.new_button);
		mSearchShowButton = (Button) findViewById(R.id.show_button);
		mSearchAuctionButton = (Button) findViewById(R.id.auction_button);
		mSearchEditText = (EditText) findViewById(R.id.edt_search);
	}

	private void initListener() {
		mSearchShowButton.setOnClickListener(this);
		mSearchNewButton.setOnClickListener(this);
		mSearchMemberButton.setOnClickListener(this);
		mSearchAuctionButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/** GRIDVIEW对应的ITEM点击监听接口 */
	@Override
	public void onItemClick(AdapterView<?> parent, final View view,
			final int position, long id) {
		// 如果点击的时候，之前动画还没结束，那么就让点击事件无效
		if (isMove) {
			return;
		}
		switch (parent.getId()) {
		case R.id.userGridView:
			// position为 0，1 的不可以进行任何操作
			if (position != 0 && position != 1) {
				final ImageView moveImageView = getView(view);
				if (moveImageView != null) {
					TextView newTextView = (TextView) view
							.findViewById(R.id.text_item);
					final int[] startLocation = new int[2];
					newTextView.getLocationInWindow(startLocation);
					final Column channel = ((DragAdapter) parent.getAdapter())
							.getItem(position);// 获取点击的频道内容
					otherAdapter.setVisible(false);
					// 添加到最后一个
					otherAdapter.addItem(channel);
					new Handler().postDelayed(new Runnable() {
						public void run() {
							try {
								int[] endLocation = new int[2];
								// 获取终点的坐标
								otherGridView.getChildAt(
										otherGridView.getLastVisiblePosition())
										.getLocationInWindow(endLocation);
								MoveAnim(moveImageView, startLocation,
										endLocation, channel, userGridView);
								userAdapter.setRemove(position);
							} catch (Exception localException) {
							}
						}
					}, 50L);
				}
			}
			break;
		case R.id.otherGridView:
			final ImageView moveImageView = getView(view);
			if (moveImageView != null) {
				TextView newTextView = (TextView) view
						.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final Column channel = ((OtherAdapter) parent.getAdapter())
						.getItem(position);
				userAdapter.setVisible(false);
				// 添加到最后一个
				userAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							// 获取终点的坐标
							userGridView.getChildAt(
									userGridView.getLastVisiblePosition())
									.getLocationInWindow(endLocation);
							MoveAnim(moveImageView, startLocation, endLocation,
									channel, otherGridView);
							otherAdapter.setRemove(position);
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 点击ITEM移动动画
	 * 
	 * @param moveView
	 * @param startLocation
	 * @param endLocation
	 * @param moveChannel
	 * @param clickGridView
	 */
	private void MoveAnim(View moveView, int[] startLocation,
			int[] endLocation, final Column moveChannel,
			final GridView clickGridView) {
		int[] initLocation = new int[2];
		// 获取传递过来的VIEW的坐标
		moveView.getLocationInWindow(initLocation);
		// 得到要移动的VIEW,并放入对应的容器中
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView,
				initLocation);
		// 创建移动动画
		TranslateAnimation moveAnimation = new TranslateAnimation(
				startLocation[0], endLocation[0], startLocation[1],
				endLocation[1]);
		moveAnimation.setDuration(300L);// 动画时间
		// 动画配置
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
		moveAnimationSet.addAnimation(moveAnimation);
		mMoveView.startAnimation(moveAnimationSet);
		moveAnimationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				isMove = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				moveViewGroup.removeView(mMoveView);
				// instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
				if (clickGridView instanceof DragGrid) {
					otherAdapter.setVisible(true);
					otherAdapter.notifyDataSetChanged();
					userAdapter.remove();
				} else {
					userAdapter.setVisible(true);
					userAdapter.notifyDataSetChanged();
					otherAdapter.remove();
				}
				isMove = false;
			}
		});
	}

	/**
	 * 获取移动的VIEW，放入对应ViewGroup布局容器
	 * 
	 * @param viewGroup
	 * @param view
	 * @param initLocation
	 * @return
	 */
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}

	/**
	 * 创建移动的ITEM对应的ViewGroup布局容器
	 */
	private ViewGroup getMoveViewGroup() {
		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(this);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}

	/**
	 * 获取点击的Item的对应View，
	 * 
	 * @param view
	 * @return
	 */
	private ImageView getView(View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(cache);
		return iv;
	}

	/** 退出时候保存选择后数据库的设置 */
	private void saveChannel() {
		m_ChannelManage.saveUserChannel(userChannelList);
		m_ChannelManage.saveOtherChannel(otherChannelList);
	}

	@Override
	public void onBackPressed() {
		saveChannel();
		if (userAdapter.isListChanged()) {
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			setResult(ArtContants.CHANNEL_RESULT, intent);
			finish();
		} else {
			super.onBackPressed();
		}
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(ChannelActivity.this, ArtSearchActivity.class);
		String searchKey = mSearchEditText.getText().toString();
		if (searchKey == "" || searchKey.equals("")) {
			Toast.makeText(ArtApplication.getArtApplication(), "关键字不能为空",
					Toast.LENGTH_SHORT).show();
		} else {
			intent.putExtra(ArtContants.SEARCH_KEY, searchKey);
			switch (view.getId()) {
			case R.id.new_button:
				intent.putExtra(ArtContants.SEARCH_TAG, ArtContants.NEW_TAG);
				startActivity(intent);
				break;
			case R.id.show_button:
				intent.putExtra(ArtContants.SEARCH_TAG, ArtContants.SHOW_TAG);
				startActivity(intent);
				break;
			case R.id.member_button:
				intent.putExtra(ArtContants.SEARCH_TAG, ArtContants.MEMBER_TAG);
				startActivity(intent);
				break;
			case R.id.auction_button:
				intent.putExtra(ArtContants.SEARCH_TAG, ArtContants.AUCTION_TAG);
				startActivity(intent);
				break;
			}
		}
	}
}
