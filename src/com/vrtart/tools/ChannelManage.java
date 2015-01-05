package com.vrtart.tools;

import java.util.ArrayList;
import java.util.List;

import com.vrtart.application.ArtApplication;
import com.vrtart.db.DatabaseManager;
import com.vrtart.models.Column;

import android.content.Context;
import android.util.Log;

public class ChannelManage {

	private List<Column> m_Columns;
	private DatabaseManager m_DatabaseManager;
	/**
	 * Ĭ�ϵ��û�ѡ��Ƶ���б�
	 * */
	public static List<Column> defaultUserChannels;
	/**
	 * Ĭ�ϵ�����Ƶ���б�
	 * */
	public static List<Column> defaultOtherChannels;
	/** �ж����ݿ����Ƿ�����û����� */
	private boolean userExist = false;

	public ChannelManage(Context context) {
		m_DatabaseManager = ArtApplication.getArtApplication().getDatabaseManger();
		m_Columns = new ArrayList<Column>();
		defaultUserChannels = new ArrayList<Column>();
		defaultOtherChannels = new ArrayList<Column>();

		getChannel();
	}

	private void getChannel() {
		m_Columns = m_DatabaseManager.queryAllColumns();
		if (m_Columns.size() == 0) {
			Log.e("size", m_Columns.size() + " 1");
		} else {
			initUserChannel();
			initOtherChannel();
		}
	}

	private void initUserChannel() {
		for (int i = 0; i < m_Columns.size() - 1; i++) {
			Column column = new Column();
			column = m_Columns.get(i);
			column.setSelected(1);
			column.setOrderId(i);
			defaultUserChannels.add(column);
		}
	}

	private void initOtherChannel() {
		for (int i = m_Columns.size() - 1; i < m_Columns.size(); i++) {
			Column column = new Column();
			column = m_Columns.get(i);
			column.setSelected(0);
			column.setOrderId(i);
			defaultOtherChannels.add(column);
		}
	}

	/**
	 * ��ȡ������Ƶ��
	 * 
	 * @return ���ݿ�����û����� ? ���ݿ��ڵ��û�ѡ��Ƶ�� : Ĭ���û�ѡ��Ƶ�� ;
	 */
	public List<Column> getUserChannel() {
		List<Column> userChannel = new ArrayList<Column>();
		userChannel = m_DatabaseManager.queryColumnBySelected(1);
		if (userChannel.size() != 0) {
			userExist = true;
			return userChannel;
		}
		initDefaultChannel();
		return defaultUserChannels;
	}

	/**
	 * ��ȡ������Ƶ��
	 * 
	 * @return ���ݿ�����û����� ? ���ݿ��ڵ�����Ƶ�� : Ĭ������Ƶ�� ;
	 */
	public List<Column> getOtherChannel() {
		List<Column> otherChannel = new ArrayList<Column>();
		otherChannel = m_DatabaseManager.queryColumnBySelected(0);
		if (otherChannel.size() != 0) {
			return otherChannel;
		}
		if (userExist) {
			return otherChannel;
		}
		return defaultOtherChannels;
	}

	public void saveUserChannel(List<Column> userChannel) {
		List<Column> columns = new ArrayList<Column>();
		for (int i = 0; i < userChannel.size(); i++) {
			Column column = userChannel.get(i);
			column.setOrderId(i);
			column.setSelected(1);
			columns.add(column);
		}
		m_DatabaseManager.deleteColumn();
		m_DatabaseManager.addColumn(columns);
	}

	public void saveOtherChannel(List<Column> otherChannel) {
		List<Column> columns = new ArrayList<Column>();
		for (int i = 0; i < otherChannel.size(); i++) {
			Column column = otherChannel.get(i);
			column.setOrderId(i);
			column.setSelected(0);
			columns.add(column);
		}
		m_DatabaseManager.addColumn(columns);
	}

	/**
	 * ��ʼ�����ݿ��ڵ�Ƶ������
	 */
	private void initDefaultChannel() {
		m_DatabaseManager.deleteColumn();
		saveUserChannel(defaultUserChannels);
		saveOtherChannel(defaultOtherChannels);
	}
}
