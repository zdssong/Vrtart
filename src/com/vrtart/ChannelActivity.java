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
 * Ƶ������
 */
public class ChannelActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener {
	public static String TAG = "ChannelActivity";
	/** �û���Ŀ��GRIDVIEW */
	private DragGrid userGridView;
	/** ������Ŀ��GRIDVIEW */
	private OtherGridView otherGridView;
	/** �û���Ŀ��Ӧ���������������϶� */
	DragAdapter userAdapter;
	/** ������Ŀ��Ӧ�������� */
	OtherAdapter otherAdapter;
	/** ������Ŀ�б� */
	List<Column> otherChannelList = new ArrayList<Column>();
	/** �û���Ŀ�б� */
	List<Column> userChannelList = new ArrayList<Column>();
	/** �Ƿ����ƶ�����������Ƕ���������Ž��е����ݸ��棬�����������Ϊ�˱������̫Ƶ����ɵ����ݴ��ҡ� */
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

	/** ��ʼ������ */
	private void initData() {
		m_ChannelManage = new ChannelManage(this);
		userChannelList = m_ChannelManage.getUserChannel();
		otherChannelList = m_ChannelManage.getOtherChannel();
		userAdapter = new DragAdapter(this, userChannelList);
		userGridView.setAdapter(userAdapter);
		otherAdapter = new OtherAdapter(this, otherChannelList);
		otherGridView.setAdapter(otherAdapter);
		// ����GRIDVIEW��ITEM�ĵ������
		otherGridView.setOnItemClickListener(this);
		userGridView.setOnItemClickListener(this);
	}

	/** ��ʼ������ */
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

	/** GRIDVIEW��Ӧ��ITEM��������ӿ� */
	@Override
	public void onItemClick(AdapterView<?> parent, final View view,
			final int position, long id) {
		// ��������ʱ��֮ǰ������û��������ô���õ���¼���Ч
		if (isMove) {
			return;
		}
		switch (parent.getId()) {
		case R.id.userGridView:
			// positionΪ 0��1 �Ĳ����Խ����κβ���
			if (position != 0 && position != 1) {
				final ImageView moveImageView = getView(view);
				if (moveImageView != null) {
					TextView newTextView = (TextView) view
							.findViewById(R.id.text_item);
					final int[] startLocation = new int[2];
					newTextView.getLocationInWindow(startLocation);
					final Column channel = ((DragAdapter) parent.getAdapter())
							.getItem(position);// ��ȡ�����Ƶ������
					otherAdapter.setVisible(false);
					// ��ӵ����һ��
					otherAdapter.addItem(channel);
					new Handler().postDelayed(new Runnable() {
						public void run() {
							try {
								int[] endLocation = new int[2];
								// ��ȡ�յ������
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
				// ��ӵ����һ��
				userAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							// ��ȡ�յ������
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
	 * ���ITEM�ƶ�����
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
		// ��ȡ���ݹ�����VIEW������
		moveView.getLocationInWindow(initLocation);
		// �õ�Ҫ�ƶ���VIEW,�������Ӧ��������
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView,
				initLocation);
		// �����ƶ�����
		TranslateAnimation moveAnimation = new TranslateAnimation(
				startLocation[0], endLocation[0], startLocation[1],
				endLocation[1]);
		moveAnimation.setDuration(300L);// ����ʱ��
		// ��������
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);// ����Ч��ִ����Ϻ�View���󲻱�������ֹ��λ��
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
				// instanceof �����ж�2��ʵ���ǲ���һ�����жϵ������DragGrid����OtherGridView
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
	 * ��ȡ�ƶ���VIEW�������ӦViewGroup��������
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
	 * �����ƶ���ITEM��Ӧ��ViewGroup��������
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
	 * ��ȡ�����Item�Ķ�ӦView��
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

	/** �˳�ʱ�򱣴�ѡ������ݿ������ */
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
			Toast.makeText(ArtApplication.getArtApplication(), "�ؼ��ֲ���Ϊ��",
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
