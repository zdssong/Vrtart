package com.vrtart;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vrtart.R;
import com.vrtart.adapter.ArtMainViewPagerAdapter;
import com.vrtart.application.ArtApplication;
import com.vrtart.contants.ArtContants;
import com.vrtart.db.DatabaseManager;
import com.vrtart.fragment.ArtFragment;
import com.vrtart.fragment.ArtMemberFragment;
import com.vrtart.fragment.ArtShowMainFragment;
import com.vrtart.fragment.ArtWeiGuangWangFragment;
import com.vrtart.models.Column;
import com.vrtart.models.Login;
import com.vrtart.tools.BaseTools;
import com.vrtart.view.ColumnHorizontalScrollView;
import com.vrtart.view.DrawerView;
import com.vrtart.webServe.ArtCookieStore;
import com.vrtart.webServe.GetArtHttp;
import com.vrtart.webServe.GetArtJson;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener,
		OnPageChangeListener {

	/** �Զ���HorizontalScrollView */
	private ColumnHorizontalScrollView m_ColumnHorizontalScrollView; // ��ʾ�������ScrollView

	private ImageView m_ImageViewAddColumns; // ��ø���Ƶ���İ�ť
	private ImageView m_ImageViewMore; // ��ò໬�˵��İ�ť

	// �໬�˵���
	private SlidingMenu m_SlidingMenu;

	private long mExitTime;// ��¼��һ�ΰ����ؼ��˳���ʱ��

	// ����Ŀ��ʾ��صı���
	private LinearLayout m_RadioGroupContent;
	private LinearLayout m_llMoreColumns;
	private RelativeLayout m_rlColumn;
	private List<Column> mUserChannelList;
	private int m_ScreenWidth; // �����Ļ�Ŀ��
	private int m_ColumnSelectIndex;// ��ǰѡ�е���Ŀ
	private ImageView m_ShadeLeft;// ����Ӱ����
	private ImageView m_ShadeRight;// ����Ӱ����

	private ViewPager m_ViewPager;
	private ArtMainViewPagerAdapter m_ViewPagerAdapter;
	private List<Fragment> m_Fragments;

	private String mImei;
	private DatabaseManager mDatabaseManger;
	private List<Column> mColumns;

	private String photo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		mImei = telephonyManager.getDeviceId();
		mGetArtHttp = ArtApplication.getArtApplication().getGetArtHttp();
		mDatabaseManger = ArtApplication.getArtApplication()
				.getDatabaseManger();
		mColumns = new ArrayList<Column>();

		// ��ʼ���������͹���
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		startWelcome();
		new initDataTask().execute();

		m_ScreenWidth = BaseTools.getWindowsWidth(this);

		initWidget();
		initListener();
		initData();
		initSlidingMenu();
		getLoginInfor();

		registerBoradcastReceiver();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}

	private void getLoginInfor() {
		ArtCookieStore cookieStore = new ArtCookieStore();
		Login login = new Login();
		cookieStore = BaseTools.getLoginCookie();
		login = BaseTools.getLoginInf();
		if (cookieStore != null) {
			ArtApplication.mArtCookieStore = cookieStore;
			ArtApplication.mLogin = login;
			if (photo != null && !photo.equals(""))
				ImageLoader.getInstance().displayImage("file://" + photo,
						DrawerView.mHead);
			else
				ImageLoader.getInstance().displayImage(
						ArtApplication.mLogin.getFace(), DrawerView.mHead);
			DrawerView.mLogin.setText(ArtApplication.mLogin.getUname());
		}
	}

	private void startWelcome() {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, Welcome.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case ArtContants.CHANNEL_REQUEST:
			if (resultCode == ArtContants.CHANNEL_RESULT) {
				setChangelView();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (m_SlidingMenu.isMenuShowing()
					|| m_SlidingMenu.isSecondaryMenuShowing()) {
				m_SlidingMenu.showContent();
			} else {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "�ڰ�һ���˳�", Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					finish();
				}
			}
			return true;
		}
		// ����MENU��ť����¼����������κβ���
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// ��ʼ����Ӧ�Ŀؼ�
	private void initWidget() {
		m_ColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
		m_RadioGroupContent = (LinearLayout) findViewById(R.id.mRadioGroup_content);
		m_ImageViewAddColumns = (ImageView) findViewById(R.id.button_more_columns);
		m_ImageViewMore = (ImageView) findViewById(R.id.top_head);
		m_ShadeLeft = (ImageView) findViewById(R.id.shade_left);
		m_ShadeRight = (ImageView) findViewById(R.id.shade_right);
		m_llMoreColumns = (LinearLayout) findViewById(R.id.ll_more_columns);
		m_rlColumn = (RelativeLayout) findViewById(R.id.rl_column);

		m_ViewPager = (ViewPager) findViewById(R.id.viewPager);
	}

	// Ϊ�ؼ������Ӧ�ļ�����
	private void initListener() {
		m_ImageViewAddColumns.setOnClickListener(this);
		m_ImageViewMore.setOnClickListener(this);

		m_ViewPager.setOnPageChangeListener(this);
	}

	// ��ʼ������
	private void initData() {
		mUserChannelList = new ArrayList<Column>();
		m_ColumnSelectIndex = 0;
	}

	private void initSlidingMenu() {
		m_SlidingMenu = new DrawerView(this).initSlidingMenu();
	}

	// ����Ŀ����仯ʱ�����
	private void setChangelView() {
		initColumnData();
		initTabColumn();
		initFragment();
	}

	// ��ȡColumn��Ŀ ����
	private void initColumnData() {
		if (ArtApplication.getArtApplication().getDatabaseManger().isNull()) {
			mUserChannelList = ArtApplication.mUserChannelList;
		} else {
			mUserChannelList = ArtApplication.getArtApplication()
					.getDatabaseManger().queryColumnBySelected(1);
		}
	}

	// ��ʼ��Column��Ŀ��
	private void initTabColumn() {
		m_RadioGroupContent.removeAllViews();
		int count = mUserChannelList.size();
		m_ColumnHorizontalScrollView.setParam(this, m_ScreenWidth,
				m_RadioGroupContent, m_ShadeLeft, m_ShadeRight,
				m_llMoreColumns, m_rlColumn);
		for (int i = 0; i < count; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			if (i == 0) {
				params.leftMargin = 5;
				params.rightMargin = 0;
			} else {
				params.leftMargin = 5;
				params.rightMargin = 5;
			}
			TextView columnTextView = new TextView(this);
			columnTextView.setTextAppearance(this,
					R.style.top_category_scroll_view_item_text);
			columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
			columnTextView.setGravity(Gravity.CENTER);
			if (i == 0) {
				columnTextView.setPadding(5, 5, 3, 5);
			} else {
				columnTextView.setPadding(5, 5, 5, 5);
			}
			columnTextView.setId(i);
			if (i == 1 && isReceiver) {
				columnTextView.setText(ArtApplication.mLogin.getUname());
				isReceiver = false;
			} else {
				columnTextView.setText(mUserChannelList.get(i).getTypeName());
			}
			columnTextView.setTextColor(getResources().getColorStateList(
					R.color.top_category_scroll_text_color_day));
			if (m_ColumnSelectIndex == i) {
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					for (int i = 0; i < m_RadioGroupContent.getChildCount(); i++) {
						View localView = m_RadioGroupContent.getChildAt(i);
						if (localView != v)
							localView.setSelected(false);
						else {
							localView.setSelected(true);
							m_ViewPager.setCurrentItem(i);
						}
					}
				}
			});
			m_RadioGroupContent.addView(columnTextView, i, params);
		}
	}

	private boolean isFrist = true;

	// ��ʼ��Fragment
	private void initFragment() {
		m_Fragments = new ArrayList<Fragment>();
		for (Column column : mUserChannelList) {
			int tag = column.getTypeId();
			if (tag == ArtContants.WEI_GUAN_WANG) {
				ArtWeiGuangWangFragment guanwangFragment;
				if (ArtApplication.mLogin == null)
					guanwangFragment = new ArtWeiGuangWangFragment(tag,
							GetArtJson.mid);
				else {
					guanwangFragment = new ArtWeiGuangWangFragment(tag,
							ArtApplication.mLogin.getMid());
				}
				m_Fragments.add(guanwangFragment);
			} else if (tag == ArtContants.HUA_SHI) {
				ArtMemberFragment memberFragment = new ArtMemberFragment();
				m_Fragments.add(memberFragment);
			} else if (tag == ArtContants.ZHAN_LAN
					|| tag == ArtContants.PAI_MAI) {
				ArtShowMainFragment fragment = new ArtShowMainFragment(tag);
				m_Fragments.add(fragment);
			} else {
				ArtFragment artFragment = new ArtFragment(tag);
				m_Fragments.add(artFragment);
			}
		}
		if (isFrist) {
			m_ViewPagerAdapter = new ArtMainViewPagerAdapter(
					getSupportFragmentManager(), m_Fragments);
			m_ViewPager.setAdapter(m_ViewPagerAdapter);
			isFrist = false;
		} else {
			m_ViewPagerAdapter.setPagerItems(m_Fragments);
			m_ViewPagerAdapter.notifyDataSetChanged();
		}
	}

	// ѡ���Column�����Tab
	private void selectTab(int tab_postion) {
		m_ColumnSelectIndex = tab_postion;
		for (int i = 0; i < m_RadioGroupContent.getChildCount(); i++) {
			View checkView = m_RadioGroupContent.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - m_ScreenWidth / 2;
			m_ColumnHorizontalScrollView.smoothScrollTo(i2, 0);
		}
		// �ж��Ƿ�ѡ��
		for (int j = 0; j < m_RadioGroupContent.getChildCount(); j++) {
			View checkView = m_RadioGroupContent.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_more_columns:
			Intent intent_channel = new Intent(getApplicationContext(),
					ChannelActivity.class);
			startActivityForResult(intent_channel, ArtContants.CHANNEL_REQUEST);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.top_head:
			if (m_SlidingMenu.isMenuShowing())
				m_SlidingMenu.showContent();
			else
				m_SlidingMenu.showMenu();
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		m_ViewPager.setCurrentItem(position);
		selectTab(position);
	}

	private boolean isReceiver = false;

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ArtContants.LOGIN_BROADCAST)) {
				setChangelView();
				isReceiver = true;
			}
		}

	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(ArtContants.LOGIN_BROADCAST);
		// ע��㲥
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private GetArtHttp mGetArtHttp;

	private class initDataTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			mGetArtHttp.getColumnHttp(mImei);
			return "OK";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			mUserChannelList = ArtApplication.getArtApplication()
					.getDatabaseManger().queryColumnBySelected(1);
			if ("OK".equals(result)) {
				List<Column> columns = new ArrayList<Column>();
				mColumns = mGetArtHttp.getM_ColumnList();
				for (int i = 0; i < mColumns.size(); i++) {
					Column column = mColumns.get(i);
					if (mUserChannelList.size() != 0) {
						ArtApplication.mUserChannelList = mUserChannelList;
						if (i < 2)
							column.setSelected(1);
						else
							for (int j = 0; j < mUserChannelList.size(); j++) {
								if (column.getChannelName().equals(
										mUserChannelList.get(j)
												.getChannelName())) {
									column.setSelected(1);
									break;
								}
								column.setSelected(0);
							}
					} else {
						if (i < 8) {
							column.setSelected(1);
							ArtApplication.mUserChannelList.add(column);
						} else
							column.setSelected(0);
					}
					column.setOrderId(i);
					columns.add(column);
				}
				if (columns.size() > 0 && mDatabaseManger.isNull()) {
					mDatabaseManger.deleteColumn();
					mDatabaseManger.addColumn(columns);
				}
			}
			setChangelView();
			super.onPostExecute(result);
		}

	}
}
