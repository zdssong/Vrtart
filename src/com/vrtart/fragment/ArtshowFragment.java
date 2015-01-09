package com.vrtart.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vrtart.ArtAuctionActivity;
import com.vrtart.ArtShowActivity;
import com.vrtart.R;
import com.vrtart.adapter.ArtShowListAdapter;
import com.vrtart.application.ArtApplication;
import com.vrtart.contants.ArtContants;
import com.vrtart.models.Information;
import com.vrtart.webServe.GetArtHttp;
import com.wt.common.view.DropDownListView;
import com.wt.common.view.DropDownListView.OnDropDownListener;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ArtshowFragment extends Fragment implements OnItemClickListener,
		OnClickListener, OnDropDownListener {

	private GetArtHttp m_GetArtHttp;

	private DropDownListView m_ListView;

	private List<Information> m_OnShowList; // װ������չ��ҳ�����Ϣ
	private List<Information> m_WillShowList;// װ�ؽ�Ҫչ������Ϣ

	private LinearLayout m_WillShowLayout;
	private LinearLayout m_OnShowLayout;
	private TextView mWillShowTextView;
	private TextView mOnShowTextView;

	private ArtShowListAdapter m_adapter;

	private int m_which;// �����´������Ǹ�Fragment
	private int mTag; // ��������������չ��

	private View m_MainView;
	private LayoutInflater m_Inflater;

	private boolean isFrist = true;

	public ArtshowFragment(int which, int tag) {
		this.m_which = which;
		this.mTag = tag;
		m_GetArtHttp = new GetArtHttp();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		m_Inflater = LayoutInflater.from(ArtApplication.getArtApplication());
		m_MainView = m_Inflater.inflate(R.layout.activity_art_show,
				(ViewGroup) getActivity().findViewById(R.layout.activity_main),
				false);

		m_ListView = (DropDownListView) m_MainView.findViewById(R.id.show_list);
		m_WillShowLayout = (LinearLayout) m_MainView
				.findViewById(R.id.will_show_layout);
		m_OnShowLayout = (LinearLayout) m_MainView
				.findViewById(R.id.showing_layout);
		mWillShowTextView = (TextView) m_MainView
				.findViewById(R.id.show_Text_C);
		mOnShowTextView = (TextView) m_MainView.findViewById(R.id.showTextC);
		if (mTag == ArtContants.ZHAN_LAN) {
			mWillShowTextView.setText("��Ҫչ��");
			mOnShowTextView.setText("����չ��");
		}
		if (mTag == ArtContants.PAI_MAI) {
			mWillShowTextView.setText("��Ҫ����");
			mOnShowTextView.setText("��������");
		}
		if (m_which == 0)
			m_WillShowLayout.setVisibility(View.GONE);
		if (m_which == 1)
			m_OnShowLayout.setVisibility(View.GONE);

		m_OnShowList = new ArrayList<Information>();
		m_WillShowList = new ArrayList<Information>();

		new getShowItem().execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup p = (ViewGroup) m_MainView.getParent();
		if (p != null) {
			p.removeAllViewsInLayout();
		}
		return m_MainView;
	}

	private void initWillShowList() {
		m_adapter = new ArtShowListAdapter(m_WillShowList, getActivity());
		m_ListView.setAdapter(m_adapter);
	}

	private void initOnShowList() {
		m_adapter = new ArtShowListAdapter(m_OnShowList, getActivity());
		m_ListView.setAdapter(m_adapter);
	}

	private void addListener() {
		m_ListView.setOnItemClickListener(this);
		if (m_WillShowList.size() > 4 || m_OnShowList.size() > 4) {
			m_ListView.setOnBottomListener(this);
			m_ListView.setOnDropDownListener(this);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		if (mTag == ArtContants.ZHAN_LAN) {
			Intent intent = new Intent();
			if (m_which == 0) {
				intent.putExtra(ArtContants.ID, m_OnShowList.get(position - 1)
						.getId());
				intent.putExtra(ArtContants.TITLE,
						m_OnShowList.get(position - 1).getTitle());
				intent.putExtra(ArtContants.CHANNEL,
						m_OnShowList.get(position - 1).getChannel());
				intent.putExtra(ArtContants.CHANNEL_NAME,
						m_OnShowList.get(position - 1).getChannelName());
			}
			if (m_which == 1) {
				intent.putExtra(ArtContants.ID, m_WillShowList
						.get(position - 1).getId());
				intent.putExtra(ArtContants.TITLE,
						m_WillShowList.get(position - 1).getTitle());
				intent.putExtra(ArtContants.CHANNEL,
						m_WillShowList.get(position - 1).getChannel());
				intent.putExtra(ArtContants.CHANNEL_NAME,
						m_WillShowList.get(position - 1).getChannelName());
			}
			intent.setClass(getActivity(), ArtShowActivity.class);
			startActivity(intent);
		}
		if (mTag == ArtContants.PAI_MAI) {
			Intent intent = new Intent();
			if (m_which == 0)
				intent.putExtra(ArtContants.ZHAN_LAN_OR_PAI_MAI_AID,
						m_OnShowList.get(position).getId());
			if (m_which == 1)
				intent.putExtra(ArtContants.ZHAN_LAN_OR_PAI_MAI_AID,
						m_WillShowList.get(position).getId());
			intent.setClass(getActivity(), ArtAuctionActivity.class);
			startActivity(intent);
		}

	}

	private class getShowItem extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (m_which == 0) {
				if (mTag == ArtContants.ZHAN_LAN)
					m_GetArtHttp.getShowItemHttp("going", mTag + "");
				if (mTag == ArtContants.PAI_MAI)
					m_GetArtHttp.getShowItemHttp("going", mTag + "");
			}
			if (m_which == 1) {
				if (mTag == ArtContants.ZHAN_LAN)
					m_GetArtHttp.getShowItemHttp("coming", mTag + "");
				if (mTag == ArtContants.PAI_MAI)
					m_GetArtHttp.getShowItemHttp("coming", mTag + "");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			if (isFrist) {
				if (m_which == 0) {
					m_OnShowList = m_GetArtHttp.getM_InformationList();
					if (m_OnShowList.size() <= 4) {
						m_ListView.setDropDownStyle(false);
						m_ListView.setOnBottomStyle(false);
						m_ListView.setAutoLoadOnBottom(false);
					} else {
						m_ListView.setDropDownStyle(true);
						m_ListView.setOnBottomStyle(true);
						m_ListView.setAutoLoadOnBottom(true);
					}
					initOnShowList();
				}
				if (m_which == 1) {
					m_WillShowList = m_GetArtHttp.getM_InformationList();
					if (m_WillShowList.size() <= 4) {
						m_ListView.setDropDownStyle(false);
						m_ListView.setOnBottomStyle(false);
						m_ListView.setAutoLoadOnBottom(false);
					} else {
						m_ListView.setDropDownStyle(true);
						m_ListView.setOnBottomStyle(true);
						m_ListView.setAutoLoadOnBottom(true);
					}
					initWillShowList();
				}
				isFrist = false;
			} else {
				if (m_which == 0)
					m_adapter.setM_Informations(m_OnShowList);
				if (m_which == 1)
					m_adapter.setM_Informations(m_WillShowList);
				m_adapter.notifyDataSetChanged();
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"MM-dd HH:mm:ss");
				m_ListView.onDropDownComplete(getString(R.string.update_at)
						+ dateFormat.format(new Date()));
				Toast.makeText(getActivity(), "ˢ�³ɹ�", Toast.LENGTH_SHORT)
						.show();
			}
			addListener();
			super.onPostExecute(result);
		}

	}

	@Override
	public void onDropDown() {
		// TODO Auto-generated method stub
		new getShowItem().execute();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		m_ListView.onBottomComplete();
		Toast.makeText(getActivity(), "û�и��������", Toast.LENGTH_SHORT).show();
		// m_ListView.setOnBottomStyle(false);
	}
}
