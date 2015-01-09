package com.vrtart;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.vrtart.R;
import com.vrtart.adapter.ArtListAdapter;
import com.vrtart.application.ArtApplication;
import com.vrtart.baseActicity.BaseActivity;
import com.vrtart.contants.ArtContants;
import com.vrtart.models.Commend;
import com.vrtart.models.Information;
import com.vrtart.webServe.GetArtHttp;
import com.wt.common.view.DropDownListView;

public class ArtSearchActivity extends BaseActivity implements
		OnItemClickListener {

	private GetArtHttp mGetArtHttp;

	private LinearLayout mInitLayout;
	private TextView mNothingTextView;
	private TextView mTitle;
	private DropDownListView mListView;
	private ArtListAdapter mListAdapter;

	private String mSearchKey;
	private String mSearchTag;
	private String mChannelId;

	private List<Information> mInformations;
	private List<Commend> mCommends;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_art_search);

		mSearchKey = getIntent().getStringExtra(ArtContants.SEARCH_KEY);
		mSearchTag = getIntent().getStringExtra(ArtContants.SEARCH_TAG);

		initView();
		initData();
		initListener();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		if (mInformations.get(position).getChannel() == ArtContants.ESSAY_CHANNEL
				|| mInformations.get(position).getChannel()
						.equals(ArtContants.ESSAY_CHANNEL)) {
			intent.putExtra(ArtContants.INGORMATION,
					mInformations.get(position));
			intent.setClass(this, ArtDetailsActivity.class);
			startActivity(intent);
		}
		if (mInformations.get(position).getChannel() == ArtContants.PICTURE_CHANNEL
				|| mInformations.get(position).getChannel()
						.equals(ArtContants.PICTURE_CHANNEL)) {
			intent.setClass(this, PictureActivity.class);
			intent.putExtra(ArtContants.INGORMATION,
					mInformations.get(position));
			startActivity(intent);
		}
		if (mInformations.get(position).getChannel() == ArtContants.MEMBER_CHANNEL
				|| mInformations.get(position).getChannel()
						.equals(ArtContants.MEMBER_CHANNEL)) {

		}
		if (mInformations.get(position).getChannel() == ArtContants.SHOW_CHANNEL
				|| mInformations.get(position).getChannel()
						.equals(ArtContants.SHOW_CHANNEL)) {
			intent.setClass(this, ArtShowActivity.class);
			intent.putExtra(ArtContants.ZHAN_LAN_OR_PAI_MAI_AID,
					mInformations.get(position));
			startActivity(intent);

		}
		if (mInformations.get(position).getChannel() == ArtContants.AUCTION_CHANNEL
				|| mInformations.get(position).getChannel()
						.equals(ArtContants.AUCTION_CHANNEL)) {
			intent.setClass(this, ArtAuctionActivity.class);
			intent.putExtra(ArtContants.ZHAN_LAN_OR_PAI_MAI_AID,
					mInformations.get(position));
			startActivity(intent);

		}

	}

	private void initView() {
		mInitLayout = (LinearLayout) findViewById(R.id.initLayout);
		mTitle = (TextView) findViewById(R.id.title);
		mListView = (DropDownListView) findViewById(R.id.listView);
		mNothingTextView = (TextView) findViewById(R.id.nothing);
	}

	private void initData() {
		mGetArtHttp = new GetArtHttp();
		mCommends = new ArrayList<Commend>();

		if (mSearchTag == ArtContants.NEW_TAG
				|| mSearchTag.equals(ArtContants.NEW_TAG)) {
			mChannelId = "1";
			mTitle.setText("�롰" + mSearchKey + "����ص�����");
		}
		if (mSearchTag == ArtContants.MEMBER_TAG
				|| mSearchTag.equals(ArtContants.MEMBER_TAG)) {
			mChannelId = "-2";
			mTitle.setText("�롰" + mSearchKey + "������");
		}
		if (mSearchTag == ArtContants.SHOW_TAG
				|| mSearchTag.equals(ArtContants.SHOW_TAG)) {
			mChannelId = "17";
			mTitle.setText("�롰" + mSearchKey + "����ص�չ��");
		}
		if (mSearchTag == ArtContants.AUCTION_TAG
				|| mSearchTag.equals(ArtContants.AUCTION_TAG)) {
			mChannelId = "18";
			mTitle.setText("�롰" + mSearchKey + "����ص�����");
		}

		new getSearchTask().execute();
	}

	private void initListener() {
		mListView.setOnItemClickListener(this);
	}

	private class getSearchTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			mGetArtHttp.getSearchResult(mChannelId, mSearchKey);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			mInformations = mGetArtHttp.getM_InformationList();
			if (mInformations.size() == 0) {
				Toast.makeText(ArtApplication.getArtApplication(), "û���ѵ���ص���Ϣ",
						Toast.LENGTH_LONG).show();
				mInitLayout.setVisibility(View.GONE);
				mNothingTextView.setVisibility(View.VISIBLE);
			} else {
				mInitLayout.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				mListAdapter = new ArtListAdapter(mInformations, mCommends,
						ArtApplication.getArtApplication());
				mListView.setAdapter(mListAdapter);
			}
			super.onPostExecute(result);
		}

	}
}
