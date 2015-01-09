package com.vrtart.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.vrtart.ArtAuctionActivity;
import com.vrtart.ArtDetailsActivity;
import com.vrtart.ArtMemberActivity;
import com.vrtart.ArtShowActivity;
import com.vrtart.PictureActivity;
import com.vrtart.R;
import com.vrtart.adapter.ArtMainListAdapter;
import com.vrtart.adapter.ArtViewPagerAdapter;
import com.vrtart.application.ArtApplication;
import com.vrtart.contants.ArtContants;
import com.vrtart.models.Commend;
import com.vrtart.models.Information;
import com.vrtart.tools.DataTools;
import com.vrtart.webServe.GetArtHttp;
import com.wt.common.view.DropDownListView;
import com.wt.common.view.DropDownListView.OnDropDownListener;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ArtFragment extends Fragment implements OnItemClickListener,
		OnClickListener, OnDropDownListener {

	private List<Fragment> mPictureFragments;
	private ViewPager mViewPager;
	// private LayoutInflater
	private ArtViewPagerAdapter mArtViewPagerAdapter;

	private GetArtHttp m_GetArtHttp;
	private List<Information> mInformations;
	private List<Commend> mCommends;
	private int m_Tag;

	private LayoutInflater m_Inflater;
	private View mMainView;

	private DropDownListView m_ListView;
	private ArtMainListAdapter m_ListAdapter;
	private LinearLayout mInitLayout;
	private TextView failTextView;

	private int mMoreNum = 2;

	// private ArtScrollView mScrollView;

	private boolean isFrist;

	public ArtFragment(int Tag) {
		m_Tag = Tag;
		m_GetArtHttp = new GetArtHttp();
		isFrist = true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		m_Inflater = LayoutInflater.from(ArtApplication.getArtApplication());
		mMainView = m_Inflater.inflate(R.layout.art_main_fragment,
				(ViewGroup) getActivity().findViewById(R.layout.activity_main),
				false);

		initWidget();

		initLisenter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup p = (ViewGroup) mMainView.getParent();
		if (p != null) {
			p.removeAllViewsInLayout();
		}
		return mMainView;

	}

	private void initWidget() {
		mInitLayout = (LinearLayout) mMainView.findViewById(R.id.initLayout);
		failTextView = (TextView) mMainView.findViewById(R.id.fail);
		m_ListView = (DropDownListView) mMainView.findViewById(R.id.list_view);
		m_ListView.setAutoLoadOnBottom(true);
		if (isFrist) {
			new initArtInformationTask()
					.executeOnExecutor((ExecutorService) Executors
							.newFixedThreadPool(5));
		}
	}

	private void initListHeader() {
		if (mCommends.size() != 0 && isFrist) {
			View view = LayoutInflater.from(getActivity()).inflate(
					R.layout.view_pager, null);
			final TextView mTitle = (TextView) view.findViewById(R.id.title);
			final TextView mNum = (TextView) view.findViewById(R.id.num);
			mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
			view.setLayoutParams(new AbsListView.LayoutParams(
					AbsListView.LayoutParams.MATCH_PARENT, DataTools.dip2px(
							getActivity(), 200)));
			mPictureFragments = new ArrayList<Fragment>();
			for (int i = 0; i < mCommends.size(); i++) {
				ArtHeaderFragment artHeaderFragment = new ArtHeaderFragment(
						mCommends.get(i));
				mPictureFragments.add(artHeaderFragment);
			}
			mTitle.setText(mCommends.get(0).getTitle());
			mNum.setText(1 + "/" + mCommends.size());
			mArtViewPagerAdapter = new ArtViewPagerAdapter(
					getChildFragmentManager(), mPictureFragments);
			mViewPager.setAdapter(mArtViewPagerAdapter);
			mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int position) {
					// TODO Auto-generated method stub
					mTitle.setText(mCommends.get(position).getTitle());
					mNum.setText((position + 1) + "/" + mCommends.size());
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPageScrollStateChanged(int position) {
					// TODO Auto-generated method stub
				}
			});
			m_ListView.addHeaderView(view);
		}
	}

	private void initData() {
		// ��ʼ��ͷ������
		m_GetArtHttp.getInformationHttp(m_Tag, 1);

	}

	private void getMoreData() {
		// ��ʼ��ͷ������
		m_GetArtHttp.getInformationHttp(m_Tag, mMoreNum);
	}

	private void initLisenter() {
		m_ListView.setOnItemClickListener(this);
		failTextView.setOnClickListener(this);
		m_ListView.setOnDropDownListener(this);
		m_ListView.setOnBottomListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AddMoreTask().execute();
			}
		});

		m_ListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), true, true));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		if (mCommends.size() != 0) {
			if (mInformations.get(position - 2).getChannel() == ArtContants.ESSAY_CHANNEL
					|| mInformations.get(position - 2).getChannel()
							.equals(ArtContants.ESSAY_CHANNEL)) {
				intent.putExtra(ArtContants.ID, mInformations.get(position - 2)
						.getId());
				intent.putExtra(ArtContants.TITLE,
						mInformations.get(position - 2).getTitle());
				intent.putExtra(ArtContants.CHANNEL,
						mInformations.get(position - 2).getChannel());
				intent.putExtra(ArtContants.CHANNEL_NAME,
						mInformations.get(position - 2).getChannelName());
				intent.setClass(getActivity(), ArtDetailsActivity.class);
				startActivity(intent);
			}
			if (mInformations.get(position - 2).getChannel() == ArtContants.PICTURE_CHANNEL
					|| mInformations.get(position - 2).getChannel()
							.equals(ArtContants.PICTURE_CHANNEL)) {
				intent.setClass(getActivity(), PictureActivity.class);
				intent.putExtra(ArtContants.ID, mInformations.get(position - 2)
						.getId());
				intent.putExtra(ArtContants.TITLE,
						mInformations.get(position - 2).getTitle());
				intent.putExtra(ArtContants.CHANNEL,
						mInformations.get(position - 2).getChannel());
				intent.putExtra(ArtContants.CHANNEL_NAME,
						mInformations.get(position - 2).getChannelName());
				startActivity(intent);
			}
			if (mInformations.get(position - 2).getChannel() == ArtContants.MEMBER_CHANNEL
					|| mInformations.get(position - 2).getChannel()
							.equals(ArtContants.MEMBER_CHANNEL)) {
				intent.setClass(getActivity(), ArtMemberActivity.class);

			}
			if (mInformations.get(position - 2).getChannel() == ArtContants.SHOW_CHANNEL
					|| mInformations.get(position - 2).getChannel()
							.equals(ArtContants.SHOW_CHANNEL)) {
				intent.setClass(getActivity(), ArtShowActivity.class);
				intent.putExtra(ArtContants.ID, mInformations.get(position - 2)
						.getId());
				intent.putExtra(ArtContants.TITLE,
						mInformations.get(position - 2).getTitle());
				intent.putExtra(ArtContants.CHANNEL,
						mInformations.get(position - 2).getChannel());
				intent.putExtra(ArtContants.CHANNEL_NAME,
						mInformations.get(position - 2).getChannelName());
				startActivity(intent);
			}
			if (mInformations.get(position - 2).getChannel() == ArtContants.AUCTION_CHANNEL
					|| mInformations.get(position - 2).getChannel()
							.equals(ArtContants.AUCTION_CHANNEL)) {
				intent.setClass(getActivity(), ArtAuctionActivity.class);
				intent.putExtra(ArtContants.ZHAN_LAN_OR_PAI_MAI_AID,
						mInformations.get(position - 2).getId());
				startActivity(intent);
			}
		} else {
			if (mInformations.get(position - 1).getChannel() == ArtContants.ESSAY_CHANNEL
					|| mInformations.get(position - 1).getChannel()
							.equals(ArtContants.ESSAY_CHANNEL)) {
				intent.putExtra(ArtContants.ID, mInformations.get(position - 1)
						.getId());
				intent.putExtra(ArtContants.TITLE,
						mInformations.get(position - 1).getTitle());
				intent.putExtra(ArtContants.CHANNEL,
						mInformations.get(position - 1).getChannel());
				intent.putExtra(ArtContants.CHANNEL_NAME,
						mInformations.get(position - 1).getChannelName());
				intent.setClass(getActivity(), ArtDetailsActivity.class);
				startActivity(intent);
			}
			if (mInformations.get(position - 1).getChannel() == ArtContants.PICTURE_CHANNEL
					|| mInformations.get(position - 1).getChannel()
							.equals(ArtContants.PICTURE_CHANNEL)) {
				intent.setClass(getActivity(), PictureActivity.class);
				intent.putExtra(ArtContants.ID, mInformations.get(position - 1)
						.getId());
				intent.putExtra(ArtContants.TITLE,
						mInformations.get(position - 1).getTitle());
				intent.putExtra(ArtContants.CHANNEL,
						mInformations.get(position - 1).getChannel());
				intent.putExtra(ArtContants.CHANNEL_NAME,
						mInformations.get(position - 1).getChannelName());
				startActivity(intent);
			}
			if (mInformations.get(position - 1).getChannel() == ArtContants.MEMBER_CHANNEL
					|| mInformations.get(position - 1).getChannel()
							.equals(ArtContants.MEMBER_CHANNEL)) {

			}
			if (mInformations.get(position - 1).getChannel() == ArtContants.SHOW_CHANNEL
					|| mInformations.get(position - 1).getChannel()
							.equals(ArtContants.SHOW_CHANNEL)) {
				intent.setClass(getActivity(), ArtShowActivity.class);
				intent.putExtra(ArtContants.ID, mInformations.get(position - 1)
						.getId());
				intent.putExtra(ArtContants.TITLE,
						mInformations.get(position - 1).getTitle());
				intent.putExtra(ArtContants.CHANNEL,
						mInformations.get(position - 1).getChannel());
				intent.putExtra(ArtContants.CHANNEL_NAME,
						mInformations.get(position - 1).getChannelName());
				startActivity(intent);

			}
			if (mInformations.get(position - 1).getChannel() == ArtContants.AUCTION_CHANNEL
					|| mInformations.get(position - 1).getChannel()
							.equals(ArtContants.AUCTION_CHANNEL)) {
				intent.setClass(getActivity(), ArtAuctionActivity.class);
				intent.putExtra(ArtContants.ZHAN_LAN_OR_PAI_MAI_AID,
						mInformations.get(position - 1).getId());
				startActivity(intent);

			}
		}
	}

	private class initArtInformationTask extends
			AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			initData();
			return ArtContants.TASK_TAG;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (ArtContants.TASK_TAG.equals(result)) {
				mInformations = new ArrayList<Information>();
				mCommends = new ArrayList<Commend>();
				mInformations = m_GetArtHttp.getM_InformationList();
				mCommends = m_GetArtHttp.getM_CommendList();
				if (mInformations.size() != 0) {
					initListHeader();
					mInitLayout.setVisibility(View.GONE);
					m_ListView.setVisibility(View.VISIBLE);
					if (isFrist) {
						m_ListAdapter = new ArtMainListAdapter(mInformations,
								ArtApplication.getArtApplication());
						m_ListView.setAdapter(m_ListAdapter);
						isFrist = false;
					} else {
						m_ListAdapter.setM_Informations(mInformations);
						m_ListAdapter.notifyDataSetChanged();
						SimpleDateFormat dateFormat = new SimpleDateFormat(
								"MM-dd HH:mm:ss");
						m_ListView
								.onDropDownComplete(getString(R.string.update_at)
										+ dateFormat.format(new Date()));
						Toast.makeText(getActivity(), "ˢ�³ɹ�",
								Toast.LENGTH_SHORT).show();
					}
				}
				if (mInformations.size() == 0) {
					failTextView.setVisibility(View.VISIBLE);
					mInitLayout.setVisibility(View.GONE);
				}
			}
			super.onPostExecute(result);
		}
	}

	private class AddMoreTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			getMoreData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			List<Information> moreInformations = m_GetArtHttp
					.getM_InformationList();
			if (moreInformations.size() != 0) {
				for (int i = 0; i < moreInformations.size(); i++) {
					mInformations.add(moreInformations.get(i));
				}
				m_ListAdapter.setM_Informations(mInformations);
				m_ListAdapter.notifyDataSetChanged();
				m_ListView.onBottomComplete();
				Toast.makeText(getActivity(), "���سɹ�", Toast.LENGTH_SHORT)
						.show();
				mMoreNum++;
			} else {
				m_ListView.onBottomComplete();
				Toast.makeText(getActivity(), "û�и��������", Toast.LENGTH_SHORT)
						.show();
			}
			super.onPostExecute(result);
		}

	}

	@Override
	public void onDropDown() {
		// TODO Auto-generated method stub
		new initArtInformationTask().execute();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fail:
			new initArtInformationTask().execute();
			break;

		case R.id.textView:
			System.out.println("click");
			break;
		}
	}
}
