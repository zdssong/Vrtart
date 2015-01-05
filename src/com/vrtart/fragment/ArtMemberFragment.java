package com.vrtart.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vrtart.ArtMemberActivity;
import com.vrtart.R;
import com.vrtart.adapter.ArtGridViewAdapter;
import com.vrtart.application.ArtApplication;
import com.vrtart.contants.ArtContants;
import com.vrtart.models.Member;
import com.vrtart.tools.CharacterParser;
import com.vrtart.tools.PinyinComparator;
import com.vrtart.tools.SortModel;
import com.vrtart.view.SideBar;
import com.vrtart.view.SideBar.OnTouchingLetterChangedListener;
import com.vrtart.webServe.GetArtHttp;

import android.R.integer;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class ArtMemberFragment extends Fragment implements OnItemClickListener {

	private SideBar sideBar;
	private TextView dialog;
	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;

	/**
	 * ����ƴ��������ListView�����������
	 */
	private PinyinComparator pinyinComparator;

	private GetArtHttp m_GetArtHttp;
	private List<Member> m_Members;
	private List<SortModel> sortModels;

	private LayoutInflater m_Inflater;
	private View m_MainView;

	private GridView mGridView;
	private ArtGridViewAdapter m_GridAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		m_Inflater = LayoutInflater.from(ArtApplication.getArtApplication());
		m_MainView = m_Inflater.inflate(R.layout.member_fragment,
				(ViewGroup) getActivity().findViewById(R.layout.activity_main),
				false);

		m_GetArtHttp = new GetArtHttp();
		m_Members = new ArrayList<Member>();

		// initListView();
		new getMemberTask().execute();
		addItemClickListener();
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

	private void initGridVIew() {
		// ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) m_MainView.findViewById(R.id.sidebar);
		dialog = (TextView) m_MainView.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);// �����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// ����ĸ�״γ��ֵ�λ��
				int position = m_GridAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					mGridView.setSelection(position);
				}

			}
		});

		mGridView = (GridView) m_MainView.findViewById(R.id.gridView);
		sortModels = filledData(m_Members);
		Collections.sort(sortModels, pinyinComparator);
		m_GridAdapter = new ArtGridViewAdapter(
				ArtApplication.getArtApplication(), sortModels);
		mGridView.setAdapter(m_GridAdapter);
		mGridView.setOnItemClickListener(this);

	}

	/**
	 * ΪListView�������
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(List<Member> members) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < members.size(); i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(members.get(i).getUname());
			sortModel.setImageUrl(members.get(i).getFace());
			sortModel.setMid(members.get(i).getMid());
			// ����ת����ƴ��
			String pinyin = characterParser.getSelling(members.get(i)
					.getUname());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;
	}

	private void addItemClickListener() {
		// m_ListView.setOnItemClickListener(mListViewItemListener);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(ArtContants.NAME, sortModels.get(position).getName());
		intent.putExtra(ArtContants.MID, sortModels.get(position).getMid());
		intent.setClass(getActivity(), ArtMemberActivity.class);
		startActivity(intent);
	}

	private class getMemberTask extends AsyncTask<Void, integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			m_GetArtHttp.getMember();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			m_Members = m_GetArtHttp.getM_Members();
			if (m_Members.size() != 0)
				initGridVIew();
		}

	}
}
